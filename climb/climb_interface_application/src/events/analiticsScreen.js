document.addEventListener("DOMContentLoaded", async () => {
    const empresaId = localStorage.getItem("empresaId");
    const token = localStorage.getItem("jwtToken");
    const chamado = JSON.parse(localStorage.getItem("chamadoSelecionado"));

    if (!chamado) {
        alert("Nenhum chamado selecionado.");
        window.location.href = "/pages/gerenciamentoTickets.html";
        return;
    }

    // Função para carregar dropdowns
    async function carregarDropdown(endpoint, buttonSelector, nomeCampo = "nome") {
        try {
            const response = await fetch(`http://localhost:8080/api/${endpoint}/empresa/${empresaId}`, {
                headers: { "Authorization": `Bearer ${token}` }
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

            const valorAtual = hiddenInput.value || (chamado[endpoint.slice(0, -1)]?.id);
            if (valorAtual) {
                const selecionado = dados.find(d => d.id == valorAtual);
                if (selecionado) {
                    dropdownButton.textContent = selecionado[nomeCampo];
                    hiddenInput.value = selecionado.id;
                }
            }

            dropdownMenu.querySelectorAll(".dropdown-item").forEach(item => {
                item.addEventListener("click", e => {
                    e.preventDefault();
                    dropdownButton.textContent = item.textContent;
                    hiddenInput.value = item.dataset.id;
                });
            });
        } catch (error) {
            console.error(`Erro ao carregar ${endpoint}:`, error);
        }
    }

    // Carrega dropdowns (setores, urgencias, status)
    await carregarDropdown("setores", "#dropAreaAfetada");
    await carregarDropdown("urgencias", "#dropUrgencia");
    await carregarDropdown("status", "#dropStatus");
    await carregarDropdown("setores", "#dropSetorDirecionado")
});