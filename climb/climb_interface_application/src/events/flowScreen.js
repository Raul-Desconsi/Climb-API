document.addEventListener("DOMContentLoaded", async () => {
    // Pega o ID da empresa e o token do localStorage
    const empresaId = localStorage.getItem("empresaId");
    const token = localStorage.getItem("jwtToken");

    alert("Empresa: " + empresaId);

    if (!empresaId) {
        console.error("Empresa não encontrada no localStorage. Faça login novamente.");
        return;
    }

    try {
        // Requisição para buscar os setores da empresa
        const response = await fetch(`http://localhost:8080/api/setores/empresa/${empresaId}`, {
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) {
            console.error("Erro ao buscar setores:", response.status, response.statusText);
            alert("Erro ao buscar setores. Verifique o console.") + response;
            return;
        }

        // Converte a resposta em JSON
        const setores = await response.json();
        console.log("Setores retornados:", setores);

        // Dropdown de Área Afetada
        const dropAfetada = document.querySelector("#dropAreaAfetada + .dropdown-menu");
        dropAfetada.innerHTML = "";

        // Preenche o dropdown com os setores
        setores.forEach(setor => {
            dropAfetada.innerHTML += `<li><a class="dropdown-item" href="#" data-value="${setor.nome}">${setor.nome}</a></li>`;
        });

        // Adiciona evento de click para selecionar opção
        document.querySelectorAll(".dropdown-item").forEach(item => {
            item.addEventListener("click", (e) => {
                e.preventDefault();
                const button = e.target.closest("ul").previousElementSibling;
                button.textContent = e.target.textContent;

                const inputHidden = button.parentElement.querySelector("input[type='hidden']");
                inputHidden.value = e.target.dataset.value;
            });
        });

    } catch (error) {
        console.error("Erro inesperado ao buscar setores:", error);
        alert("Ocorreu um erro ao buscar os setores. Veja o console para detalhes.");
    }
});
