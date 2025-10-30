function getBadgeClass(status) {
      switch (status) {
        case "Analise":
          return "bg-warning text-dark";
        case "Aberto":
          return "bg-primary";
        case "Fechado":
          return "bg-success";
        case "Cancelado":
          return "bg-danger";
        default:
          return "bg-secondary";
      }
    }

    async function carregarChamados() {
      try {
        const token = localStorage.getItem("jwtToken");

        if (!token) {
          alert("Token não encontrado. Faça login novamente.");
          return;
        }

        const response = await fetch("http://localhost:8080/chamado/all", {
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token.trim(),
          },
        });

        if (!response.ok) {
          if (response.status === 403) {
            alert("Acesso negado (403). Verifique o token de autenticação.");
          }
          throw new Error("Erro ao buscar chamados: " + response.status);
        }

        const chamados = await response.json();
        const container = document.getElementById("tickets-container");
        container.innerHTML = "";

        if (chamados.length === 0) {
          container.innerHTML = '<p class="text-center text-muted">Nenhum chamado encontrado.</p>';
          return;
        }

        chamados.forEach((ch) => {
          const statusNome = ch.status?.nome || ch.status || "Desconhecido";

          const card = document.createElement("div");
          card.className = "col-md-6 col-lg-4";
          card.innerHTML = `
            <div class="card ticket-card">
              <div class="card-body">
                <div class="d-flex justify-content-between align-items-start">
                  <div>
                    <h5 class="card-title mb-1">${ch.motivo}</h5>
                    <span class="badge badge-id">#${ch.id}</span>
                  </div>
                  <span class="badge ${getBadgeClass(statusNome)}">${statusNome}</span>
                </div>
                <p class="card-text mt-3">${ch.descricao}</p>
                <p class="small text-muted">
                  Aberto por: ${ch.responsavelAbertura ? ch.responsavelAbertura.nome : "Desconhecido"} 
                  | Urgência: ${ch.urgencia.nome}
                </p>
              </div>
            </div>
          `;

          // Clique no card → salva e vai para /flowAnalytics
          card.addEventListener("click", () => {
            localStorage.setItem("chamadoSelecionado", JSON.stringify(ch));
            window.location.href = "../page/flowAnalytics.html";
          });

          container.appendChild(card);
        });
      } catch (error) {
        console.error("Erro:", error);
      }
    }

    window.addEventListener("DOMContentLoaded", carregarChamados);