document.getElementById("fluxoForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const motivo = document.getElementById("motivo").value.trim();
    const areaAfetada = document.getElementById("areas-afetadas").value;
    const problema = document.getElementById("problema").value.trim();
    const urgencia = document.querySelector('input[name="urgencia"]:checked')?.value || "baixa";


    const token = localStorage.getItem('jwtToken');
    const responsavelIdStr = localStorage.getItem('id');
    const setorLocalStr = localStorage.getItem('setor');

    if (!token) {
        window.alert("Você precisa estar logado para cadastrar um chamado.");
        return;
    }

    if (!responsavelIdStr) {
        window.alert("ID do usuário não encontrado (faça login novamente).");
        return;
    }

    const payload = {
        motivo: motivo,
        areaAfetada: areaAfetada,
        descricao: problema,
        urgencia: urgencia,
        responsavelAberturaId: parseInt(responsavelIdStr),
        setorId: setorLocalStr ? parseInt(setorLocalStr) : undefined
    };

    fetch("http://localhost:8080/chamado/create", {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=UTF-8",
            "Authorization": token
        },
        body: JSON.stringify(payload)
    })
    .then(async response => {
        const txt = await response.text();
        let body;
        try { body = JSON.parse(txt); } catch (e) { body = txt; }

        if (response.ok) {

            window.alert("Chamado cadastrado com sucesso! ID: " + (body.id || ""));
            document.getElementById("fluxoForm").reset();
        } else if (response.status === 403) {
            window.alert(body || "Sem permissão");
        } else if (response.status === 404) {
            window.alert(body || "Recurso não encontrado");
        } else {
            window.alert("Erro ao cadastrar chamado: " + (body || response.status));
        }
    })
    .catch(err => {
        console.error(err);
        window.alert("Erro de conexão ao cadastrar chamado.");
    });
});
function configurarDropdown(buttonId, inputId) {
            const button = document.getElementById(buttonId);
            const inputHidden = document.getElementById(inputId);

            document.querySelectorAll(`#${buttonId} + .dropdown-menu .dropdown-item`).forEach(item => {
                item.addEventListener("click", function (e) {
                    e.preventDefault();
                    const valor = this.getAttribute("data-value");
                    const texto = this.textContent;

                    inputHidden.value = valor;     
                    button.textContent = texto;   
                });
            });
        }

        configurarDropdown("dropAreaGuiada", "direcionamento");
        configurarDropdown("dropAreaAfetada", "areas-afetadas");

        document.querySelectorAll(".dropdown").forEach(dropdown => {
            const button = dropdown.querySelector("button");
            const hiddenInput = dropdown.querySelector("input[type=hidden]");
            const items = dropdown.querySelectorAll(".dropdown-item");

            items.forEach(item => {
                item.addEventListener("click", function (e) {
                    e.preventDefault();

                    const value = this.getAttribute("data-value");
                    const text = this.textContent;

                   
                    button.textContent = text;
                    hiddenInput.value = value;

                    items.forEach(i => i.classList.remove("active"));
                    this.classList.add("active");
                });
            });
        });

