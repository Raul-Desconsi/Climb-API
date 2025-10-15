document.addEventListener("DOMContentLoaded", async () => {
    const empresaId = localStorage.getItem("empresaId");
    const token = localStorage.getItem("jwtToken");

    if (!empresaId) {
        alert("Empresa não encontrada. Faça login novamente.");
        return;
    }

    async function carregarDropdown(endpoint, buttonSelector, nomeCampo = "nome") {
        try {
            const response = await fetch(`http://localhost:8080/api/${endpoint}/empresa/${empresaId}`, {
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                }
            });

            if (!response.ok) {
                alert(`Erro ao buscar ${endpoint}.`);
                return;
            }

            const dados = await response.json();
            const dropdownButton = document.querySelector(buttonSelector);
            const dropdownMenu = dropdownButton.nextElementSibling;
            const hiddenInput = document.querySelector(dropdownButton.getAttribute("data-input"));

            dropdownMenu.innerHTML = "";

            dados.forEach(item => {
                const li = document.createElement("li");
                li.innerHTML = `
                    <a class="dropdown-item" href="#" data-id="${item.id}">
                        ${item[nomeCampo]}
                    </a>
                `;
                dropdownMenu.appendChild(li);
            });

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

    await carregarDropdown("setores", "#dropAreaAfetada");
    await carregarDropdown("urgencias", "#dropUrgencia");
});