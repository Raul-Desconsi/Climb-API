document.addEventListener("DOMContentLoaded", async () => {
  const token = localStorage.getItem("jwtToken");
  const chamado = JSON.parse(localStorage.getItem("chamadoSelecionado"));
  const chamadoId = chamado?.id;
  const container = document.querySelector(".page-container:last-of-type"); // container dos formulários
  const seta = document.querySelector(".seta-container");

  if (!chamado) {
    alert("Nenhum chamado selecionado.");
    window.location.href = "/pages/gerenciamentoTickets.html";
    return;
  }

  try {
    // 🔹 Busca os atendimentos do chamado
    const resp = await fetch(`http://localhost:8080/api/atendimento/chamado/${chamadoId}`, {
      headers: { "Authorization": `${token}` }
    });
    if (!resp.ok) throw new Error("Erro ao buscar atendimentos");
    const atendimentos = await resp.json();

    // 🔹 Limpa o container antes de inserir novos formulários
    container.innerHTML = "";

    // 🔹 Cria um formulário para cada atendimento existente
    atendimentos.forEach((atendimento, index) => {
      const formCard = document.createElement("div");
      formCard.classList.add("form-card");

      formCard.innerHTML = ` 
        <form class="atendimentoExistente">
    
          <div class="mb-3">
            <h2 class="mb-4 text-center text-primary color-primary">
            Atendimento ${index + 1}
          </h2>
            <label class="form-label">Resposta do atendimento</label>
            <textarea class="form-control" rows="4" readonly>${atendimento.resposta || ""}</textarea>
          </div>

          <div class="dropdown mb-3">
            <label class="form-label">Qual setor esse fluxo será direcionado?</label>
            <button class="btn btn-primary bg-primary dropdown-toggle w-100" type="button"
              id="dropSetorDirecionado" data-bs-toggle="dropdown"
              data-input="#setor-direcionado" aria-expanded="false" readonly value="${atendimento.setorDirecionado?.nome || "N/A"}">
            </button>
            <ul class="dropdown-menu w-100" id="menuSetorDirecionado"
              aria-labelledby="dropSetorDirecionado"></ul>
            <input type="hidden" id="setor-direcionado" name="setorDirecionadoId">
          </div>


        </form>
      `;
      container.appendChild(formCard);

      // adiciona seta entre formulários
      if (index < atendimentos.length - 1) {
        const setaClone = seta.cloneNode(true);
        container.appendChild(setaClone);
      }
    });

    // 🔹 Verifica se o chamado está concluído
    if (chamado.conclusao_chamado === 0) {
      // ainda não concluído → adiciona novo formulário de atendimento
      const formCardNovo = document.createElement("div");
      formCardNovo.classList.add("form-card");

      formCardNovo.innerHTML = `
        <form id="novoAtendimentoForm">
          <div class="mb-3">
            <label class="form-label">Resposta do atendimento</label>
            <textarea class="form-control" id="resposta" name="resposta" rows="4"
              placeholder="Descreva o que você fez nesse fluxo..."></textarea>
          </div>

          <div class="dropdown mb-3">
            <label class="form-label">Qual setor esse fluxo será direcionado?</label>
            <button class="btn btn-primary bg-primary dropdown-toggle w-100" type="button"
              id="dropSetorDirecionado" data-bs-toggle="dropdown"
              data-input="#setor-direcionado" aria-expanded="false">
              Selecione uma opção
            </button>
            <ul class="dropdown-menu w-100" id="menuSetorDirecionado"
              aria-labelledby="dropSetorDirecionado"></ul>
            <input type="hidden" id="setor-direcionado" name="setorDirecionadoId">
          </div>
        </form>
      `;
      container.appendChild(seta.cloneNode(true));
      container.appendChild(formCardNovo);

      // Reutiliza sua função existente para carregar o dropdown
      if (typeof carregarDropdown === "function")
        await carregarDropdown("setores", "#dropSetorDirecionado");
    } else {
      // chamado já concluído
      const finalMsg = document.createElement("h2");
      finalMsg.classList.add("text-center", "text-success", "mt-4");
      finalMsg.textContent = "Chamado Concluído / Fechado ✅";
      container.appendChild(finalMsg);
    }

  } catch (err) {
    console.error(err);
    alert("Erro ao carregar atendimentos do chamado.");
  }
});
