document.addEventListener("DOMContentLoaded", async () => {
    const empresaId = localStorage.getItem("empresaId");
    const token = localStorage.getItem("jwtToken");
    const chamado = JSON.parse(localStorage.getItem("chamadoSelecionado"));
    const usuarioAtual = JSON.parse(localStorage.getItem("usuarioLogado"));

    if (!chamado) {
        alert("Nenhum chamado selecionado.");
        window.location.href = "/pages/gerenciamentoTickets.html";
        return;
    }

    // Função para carregar dropdowns
    async function carregarDropdown(endpoint, buttonSelector, nomeCampo = "nome") {
        try {
            const response = await fetch(`http://localhost:8080/api/${endpoint}/empresa/${empresaId}`, {
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                }
            });

            if (!response.ok) throw new Error(`Erro ao buscar ${endpoint}`);
            const dados = await response.json();

            const dropdownButton = document.querySelector(buttonSelector);
            const dropdownMenu = dropdownButton.nextElementSibling;
            const hiddenInput = document.querySelector(dropdownButton.getAttribute("data-input"));
            dropdownMenu.innerHTML = "";

            dados.forEach(item => {
                const li = document.createElement("li");
                li.innerHTML = `<a class="dropdown-item" href="#" data-id="${item.id}">${item[nomeCampo]}</a>`;
                dropdownMenu.appendChild(li);
            });

            // Selecionar item existente do chamado se houver
            const valorAtual = hiddenInput.value || (chamado[endpoint.slice(0, -1)]?.id);
            if (valorAtual) {
                const selecionado = dados.find(d => d.id == valorAtual);
                if (selecionado) {
                    dropdownButton.textContent = selecionado[nomeCampo];
                    hiddenInput.value = selecionado.id;
                }
            }

            dropdownMenu.querySelectorAll(".dropdown-item").forEach(item => {
                item.addEventListener("click", (e) => {
                    e.preventDefault();
                    dropdownButton.textContent = item.textContent;
                    hiddenInput.value = item.dataset.id;
                });
            });
        } catch (error) {
            console.error(`Erro ao carregar ${endpoint}:`, error);
        }
    }

    // Carregar dropdowns
    await carregarDropdown("setores", "#dropAreaAfetada");
    await carregarDropdown("urgencias", "#dropUrgencia");
    await carregarDropdown("status", "#dropStatus");

    // Preenche dados do chamado principal
    document.getElementById("motivo").value = chamado.motivo || "";
    document.getElementById("problema").value = chamado.descricao || "";
    document.getElementById("responsavelAbertura").value = chamado.responsavelAbertura?.nome || "N/A";



    // Preenche setor que abriu o fluxo
    if (chamado.setorAbertura && chamado.setorAbertura.nome) {
        document.getElementById("setorAbertura").value = chamado.setorAbertura.nome;
    } else if (chamado.setor && chamado.setor.nome) {
        // Caso o JSON tenha outro nome de chave
        document.getElementById("setorAbertura").value = chamado.setor.nome;
    } else {
        document.getElementById("setorAbertura").value = "N/A";
    }


    // Preenche data de abertura
    if (chamado.data) {
        const data = new Date(chamado.data); // converte para Date
        const dia = String(data.getDate()).padStart(2, '0');
        const mes = String(data.getMonth() + 1).padStart(2, '0'); // Janeiro = 0
        const ano = data.getFullYear();
        document.getElementById("dataAberturaTexto").value = `${dia}/${mes}/${ano}`;
    } else {
        document.getElementById("dataAberturaTexto").value = "N/A";
    }





    // Preencher dropdowns já selecionados se houver no chamado
    if (chamado.areaAfetada) {
        document.getElementById("dropAreaAfetada").textContent = chamado.areaAfetada.nome;
        document.getElementById("areas-afetadas").value = chamado.areaAfetada.id;
    }
    if (chamado.urgencia) {
        document.getElementById("dropUrgencia").textContent = chamado.urgencia.nome;
        document.getElementById("inputUrgencia").value = chamado.urgencia.id;
    }
    if (chamado.status) {
        document.getElementById("dropStatus").textContent = chamado.status.nome;
        document.getElementById("inputStatus").value = chamado.status.id;
    }
});
