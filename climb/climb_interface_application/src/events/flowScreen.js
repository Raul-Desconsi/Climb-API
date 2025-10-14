document.addEventListener("DOMContentLoaded", async () => {
    const empresaId = localStorage.getItem("empresaId");
    const token = localStorage.getItem("jwtToken");

    if (!empresaId) {
        console.error("Empresa não encontrada no localStorage. Faça login novamente.");
        alert("Empresa não encontrada. Faça login novamente.");
        return;
    }
    async function carregarDropdown(endpoint, dropdownSelector, nomeCampo = "nome") {
        try {
            const response = await fetch(`http://localhost:8080/api/${endpoint}/empresa/${empresaId}`, {
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                }
            });

            if (!response.ok) {
                console.error(`Erro ao buscar ${endpoint}:`, response.status, response.statusText);
                alert(`Erro ao buscar ${endpoint}. Veja o console.`);
                return;
            }

            const dados = await response.json();
            console.log(`${endpoint} retornados:`, dados);

            const dropdownMenu = document.querySelector(`${dropdownSelector} + .dropdown-menu`);
            dropdownMenu.innerHTML = "";

            dados.forEach(item => {
                dropdownMenu.innerHTML += `
                    <li>
                        <a class="dropdown-item" href="#" data-id="${item.id}" data-value="${item[nomeCampo]}">
                            ${item[nomeCampo]}
                        </a>
                    </li>`;
            });

            dropdownMenu.querySelectorAll(".dropdown-item").forEach(item => {
                item.addEventListener("click", (e) => {
                    e.preventDefault();
                    const button = e.target.closest("ul").previousElementSibling;
                    button.textContent = e.target.textContent;

                    const inputHidden = button.parentElement.querySelector("input[type='hidden']");
                    inputHidden.value = e.target.dataset.id;
                });
            });

        } catch (error) {
            console.error(`Erro inesperado ao buscar ${endpoint}:`, error);
            alert(`Erro ao carregar ${endpoint}. Veja o console.`);
        }
    }
    await carregarDropdown("setores", "#dropAreaAfetada");
    await carregarDropdown("urgencias", "#dropUrgencia");
});
