let todosChamados = [];
let chamadosFiltrados = [];
let chamadoSelecionado = null;

// Paginação
let paginaAtual = 1;
const itensPorPagina = 9;

// 🔹 Carregar chamados da API
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

    if (!response.ok)
      throw new Error("Erro ao buscar chamados: " + response.status);

    todosChamados = await response.json();
    chamadosFiltrados = [...todosChamados];
    exibirChamados(chamadosFiltrados);
  } catch (error) {
    console.error("Erro:", error);
  }
}

// 🔹 Badge de status
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

// 🔹 Exibir chamados com paginação
function exibirChamados(lista) {
  const container = document.getElementById("tickets-container");
  container.innerHTML = "";

  if (lista.length === 0) {
    container.innerHTML =
      '<p class="text-center text-muted">Nenhum chamado encontrado.</p>';
    document.getElementById("paginationContainer").innerHTML = "";
    return;
  }

  const inicio = (paginaAtual - 1) * itensPorPagina;
  const fim = inicio + itensPorPagina;
  const paginaAtualChamados = lista.slice(inicio, fim);

  paginaAtualChamados.forEach((ch) => {
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
            Aberto por: ${ch.responsavelAbertura?.nome || "Desconhecido"} 
          </p>
          <p class="small fw-bold" style="
            background: ${ch.urgencia?.cor};
            color: #fff;
            padding: 3px 8px;
            border-radius: 6px;
            display: inline-block;
            text-shadow: 
                0px 0px 6px black,
                0px 0px 6px black;
            ">
            ${ch.urgencia?.nome || "N/A"}
        </p>
        </div>
      </div>
    `;

    card.addEventListener("click", () => {
      localStorage.setItem("chamadoSelecionado", JSON.stringify(ch));
      window.location.href = "../page/flowAnalytics.html";
    });

    card.addEventListener("contextmenu", (e) => {
      e.preventDefault();
      chamadoSelecionado = ch;
      mostrarContextMenu(e.pageX, e.pageY);
    });

    container.appendChild(card);
  });

  renderizarPaginacao(lista);
}

// 🔹 Paginação
function renderizarPaginacao(lista) {
  const totalPaginas = Math.ceil(lista.length / itensPorPagina);
  const paginacao = document.getElementById("paginationContainer");
  paginacao.innerHTML = "";

  if (totalPaginas <= 1) return;

  const nav = document.createElement("nav");
  const ul = document.createElement("ul");
  ul.className = "pagination justify-content-center";

  const liAnterior = document.createElement("li");
  liAnterior.className = `page-item ${paginaAtual === 1 ? "disabled" : ""}`;
  liAnterior.innerHTML = `<a class="page-link" href="#">Anterior</a>`;
  liAnterior.addEventListener("click", (e) => {
    e.preventDefault();
    if (paginaAtual > 1) {
      paginaAtual--;
      exibirChamados(chamadosFiltrados);
    }
  });
  ul.appendChild(liAnterior);

  for (let i = 1; i <= totalPaginas; i++) {
    const li = document.createElement("li");
    li.className = `page-item ${i === paginaAtual ? "active" : ""}`;
    li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
    li.addEventListener("click", (e) => {
      e.preventDefault();
      paginaAtual = i;
      exibirChamados(chamadosFiltrados);
    });
    ul.appendChild(li);
  }

  const liProximo = document.createElement("li");
  liProximo.className = `page-item ${paginaAtual === totalPaginas ? "disabled" : ""}`;
  liProximo.innerHTML = `<a class="page-link" href="#">Próximo</a>`;
  liProximo.addEventListener("click", (e) => {
    e.preventDefault();
    if (paginaAtual < totalPaginas) {
      paginaAtual++;
      exibirChamados(chamadosFiltrados);
    }
  });
  ul.appendChild(liProximo);

  nav.appendChild(ul);
  paginacao.appendChild(nav);
}

// 🔹 Menu de contexto
function mostrarContextMenu(x, y) {
  const menu = document.getElementById("contextMenu");
  menu.style.display = "block";

  const largura = window.innerWidth;
  const altura = window.innerHeight;
  const menuWidth = menu.offsetWidth || 180;
  const menuHeight = menu.offsetHeight || 100;

  const posX = x + menuWidth > largura ? largura - menuWidth - 10 : x;
  const posY = y + menuHeight > altura ? altura - menuHeight - 10 : y;

  menu.style.left = `${posX}px`;
  menu.style.top = `${posY}px`;
}

document.addEventListener("click", () => {
  document.getElementById("contextMenu").style.display = "none";
});

document.getElementById("abrirFluxo").addEventListener("click", () => {
  if (!chamadoSelecionado) return;
  localStorage.setItem("chamadoSelecionado", JSON.stringify(chamadoSelecionado));
  window.location.href = "../page/flowAnalytics.html";
});

document.getElementById("verHistorico").addEventListener("click", () => {
  if (!chamadoSelecionado) return;
  alert(`Histórico do chamado #${chamadoSelecionado.id}`);
});

// 🔹 Carregar filtros
async function carregarFiltros() {
  const token = localStorage.getItem("jwtToken");
  const endpoints = [
    { id: "filtroStatus", url: "http://localhost:8080/status/all" },
    { id: "filtroUrgencia", url: "http://localhost:8080/urgencia/all" },
  ];

  for (const e of endpoints) {
    try {
      const response = await fetch(e.url, {
        headers: { Authorization: "Bearer " + token.trim() },
      });
      if (!response.ok) continue;

      const data = await response.json();
      const select = document.getElementById(e.id);

      data.forEach((item) => {
        const opt = document.createElement("option");
        opt.value = item.nome;
        opt.textContent = item.nome;
        select.appendChild(opt);
      });
    } catch (err) {
      console.error("Erro ao carregar " + e.id, err);
    }
  }
}

// 🔹 Aplicar filtros
function aplicarFiltros() {
  const urgencia = document.getElementById("filtroUrgencia").value.toLowerCase();
  const status = document.getElementById("filtroStatus").value.toLowerCase();
  const responsavel = document.getElementById("filtroResponsavel").value.toLowerCase();

  chamadosFiltrados = todosChamados.filter((ch) => {
    const urg = ch.urgencia?.nome?.toLowerCase() || "";
    const sts = (ch.status?.nome || ch.status || "").toLowerCase();
    const resp = ch.responsavelAbertura?.nome?.toLowerCase() || "";

    return (
      (urgencia === "" || urg.includes(urgencia)) &&
      (status === "" || sts.includes(status)) &&
      (responsavel === "" || resp.includes(responsavel))
    );
  });

  paginaAtual = 1;
  exibirChamados(chamadosFiltrados);
  const modal = bootstrap.Modal.getInstance(document.getElementById("filterModal"));
  modal.hide();
}

// 🔹 Inicialização
document.addEventListener("DOMContentLoaded", () => {
  carregarChamados();
  carregarFiltros();

  document.getElementById("btnAplicarFiltros").addEventListener("click", aplicarFiltros);

  document.getElementById("searchInput").addEventListener("input", (e) => {
    const termo = e.target.value.toLowerCase();
    const resultados = chamadosFiltrados.filter(
      (ch) =>
        ch.motivo.toLowerCase().includes(termo) ||
        ch.descricao.toLowerCase().includes(termo) ||
        ch.responsavelAbertura?.nome?.toLowerCase().includes(termo)
    );
    paginaAtual = 1;
    exibirChamados(resultados);
  });
});