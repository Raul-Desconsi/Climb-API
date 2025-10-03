async function preencherDropdownSetores() {
    const dropdownMenu = document.getElementById('setorDropdown');
    const dropdownButton = document.getElementById('setorSelect');
    const hiddenInputId = document.getElementById('setorSelecionadoId');
    const hiddenInputNome = document.getElementById('setorSelecionadoNome');

    try {
        dropdownMenu.innerHTML = '<li><span class="dropdown-item-text">Carregando setores...</span></li>';
        dropdownButton.disabled = true;

        const setores = await getSetor();
        
        dropdownMenu.innerHTML = '';

        if (setores.length === 0) {
            dropdownMenu.innerHTML = '<li><span class="dropdown-item-text">Nenhum setor encontrado</span></li>';
            return;
        }

        setores.forEach(setor => {
            const listItem = document.createElement('li');
            listItem.innerHTML = `
                <a class="dropdown-item" href="#" data-setor-id="${setor.id}" data-setor-nome="${setor.nome}">
                    ${setor.nome}
                </a>
            `;
            dropdownMenu.appendChild(listItem);
        });

        dropdownMenu.querySelectorAll('.dropdown-item').forEach(item => {
            item.addEventListener('click', function(e) {
                e.preventDefault();
                
                const setId = this.getAttribute('data-setor-id');
                const setNome = this.getAttribute('data-setor-nome');
                
                dropdownButton.textContent = setNome;
                dropdownButton.classList.remove('btn-secondary');
                dropdownButton.classList.add('btn-primary');
                
                hiddenInputId.value = setId;
                hiddenInputNome.value = setNome;
                
                const bootstrapDropdown = bootstrap.Dropdown.getInstance(dropdownButton) || new bootstrap.Dropdown(dropdownButton);
                bootstrapDropdown.hide();
                
                console.log('Setor selecionado:', { id: setId, nome: setNome });
            });
        });

        dropdownButton.disabled = false;

    } catch (error) {
        console.error('Erro ao carregar setores:', error);
        dropdownMenu.innerHTML = `
            <li>
                <span class="dropdown-item-text text-danger">
                    Erro ao carregar setores
                </span>
            </li>
        `;
        dropdownButton.disabled = false;
    }
}

async function getSetor() {
    let empresa = localStorage.getItem('empresaId');
    
    if (!empresa || empresa === "null" || empresa === "undefined") {
        throw new Error('ID da empresa não encontrado. Faça login novamente.');
    }
    
    empresa = empresa.toString().trim();
    if (!/^\d+$/.test(empresa)) {
        throw new Error('ID da empresa inválido.');
    }

    const url = `http://localhost:8080/api/setores/empresa/${empresa}`;

    const response = await fetch(url, {
        method: "GET",
        headers: {
            "Content-Type": "application/json;charset=UTF-8"
        }
    });

    if (!response.ok) {
        throw new Error(`Erro ${response.status} ao carregar setores`);
    }

    return await response.json();
}

function getSetorSelecionado() {
    return {
        id: document.getElementById('setorSelecionadoId').value,
        nome: document.getElementById('setorSelecionadoNome').value
    };
}

function validarFormSetor() {
    const setId = document.getElementById('setorSelecionadoId').value;
    const dropdownButton = document.getElementById('setorSelect');
    
    if (!setId) {
        dropdownButton.classList.add('is-invalid');
        return false;
    }
    
    dropdownButton.classList.remove('is-invalid');
    return true;
}

function resetarDropdownSetores() {
    const dropdownButton = document.getElementById('setorSelect');
    const hiddenInputId = document.getElementById('setorSelecionadoId');
    const hiddenInputNome = document.getElementById('setorSelecionadoNome');
    
    dropdownButton.textContent = 'Selecione uma opção';
    dropdownButton.classList.remove('btn-primary', 'is-invalid');
    dropdownButton.classList.add('btn-primary');
    hiddenInputId.value = '';
    hiddenInputNome.value = '';
}

document.addEventListener('DOMContentLoaded', function() {
    preencherDropdownSetores();
    
    const form = document.querySelector('form');
    if (form) {
        form.addEventListener('submit', function(e) {
            if (!validarFormSetor()) {
                e.preventDefault();
                alert('Por favor, selecione um setor.');
            }
        });
    }

    const btnSubmit = document.getElementById("btnSubmitAdd");
    if (btnSubmit) {
        btnSubmit.addEventListener("click", submitCadastro);
    } else {
        console.error("Botão btnSubmitAdd não encontrado");
    }
});

function submitCadastro(event) {
    event.preventDefault(); 

    const url = "http://localhost:8080/funcionario/Create";
    const request = new XMLHttpRequest();
    
    try {
        

        const inputEmail = document.getElementById("inputEmail").value.trim();
        const inputNome = document.getElementById("inputNome").value.trim();
        const inputCPF = document.getElementById("inputCPF").value.trim();
        const inputSenha = document.getElementById("inputSenha").value.trim();
        const inputFuncao = document.getElementById("inputFuncao").value.trim();
        const nivelUsuario = document.getElementById("nivelUsuario").value.trim();
        const setorSelecionado = document.getElementById('setorSelecionadoId').value;

        if (!inputEmail || !inputNome || !inputCPF || !inputSenha || !inputFuncao || !nivelUsuario || !setorSelecionado) {
            window.alert("Por favor, preencha todos os campos");
            return;
        }

        if (inputCPF.length > 14) {
            window.alert("CPF inválido");
            return;
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(inputEmail)) {
            window.alert("Por favor, insira um email válido");
            return;
        }

        if (nivelUsuario === "") {
            window.alert("Selecione um nível de funcionário")
            return
        }

        const userData = {
            cpf: inputCPF,
            email: inputEmail,
            funcao: inputFuncao,
            nivelPermissao: nivelUsuario,
            nome: inputNome,
            senha: inputSenha,
            empresa: localStorage.getItem("empresaId"),
            setor: setorSelecionado
        };

        const jsonUser = JSON.stringify(userData);
        console.log("Enviando dados:", userData);

        request.open('POST', url, true);
        request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        request.setRequestHeader("Authorization", localStorage.getItem("jwtToken") || "");

        request.onreadystatechange = function () {
            if (request.readyState === XMLHttpRequest.DONE) {
                const status = request.status;
                let response = {};
                
                try {
                    if (request.responseText) {
                        response = JSON.parse(request.responseText);
                    }
                } catch (e) {
                    console.error("Erro ao parsear resposta:", e);
                }
            
                if (status === 201) {
                    console.log("Cadastro bem-sucedido:", response);
                    window.alert("Funcionário cadastrado com sucesso!");
                    
                    const modal = bootstrap.Modal.getInstance(document.getElementById('addEmployeeModal'));
                    if (modal) {
                        modal.hide();
                    }
                    
                    document.querySelector('form').reset();
                    resetarDropdownSetores();
                    
                } else if (status === 400) {
                    window.alert(response.message || "Erro ao cadastrar funcionário");
                } else if (status === 401) {
                    window.alert("Token de autenticação inválido. Faça login novamente.");
                } else if (status === 403) {
                    window.alert("Você não tem permissão para realizar esta ação.");
                } else {
                    window.alert("Erro inesperado. Tente novamente.");
                }
            }
        };

        request.onerror = function() {
            window.alert("Erro de conexão. Verifique sua internet.");
        };

        request.send(jsonUser);

    } catch (error) {
        console.error("Erro no cadastro:", error);
        window.alert("Ocorreu um erro inesperado");
    }
}