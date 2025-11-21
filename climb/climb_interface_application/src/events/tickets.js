let todosChamados = [];
let chamadosFiltrados = [];
let chamadoSelecionado = null;

// Paginação
let paginaAtual = 1;
const itensPorPagina = 9;
let endpointAPI = "";

const meusChamadosBtn = document.getElementById("meusChamadosBtn");

async function carregarChamados() {
  try {
    const token = localStorage.getItem("jwtToken");
    const permissao = localStorage.getItem("nivelPermissao");
    const setorId = localStorage.getItem("setor");
    const idUsuario = localStorage.getItem("id");
    if (!token) {
      alert("Token não encontrado. Faça login novamente.");
      return;
    }

    if (meusChamadosBtn.classList.contains("btn-meus-active")) {
      endpointAPI = `http://localhost:8080/chamado/meus-chamados?responsavelId=${idUsuario}`;

    } else {

      // 2. Senão, verifica a permissão normalmente
      switch (permissao) {
        case "1": // Admin
          endpointAPI = "http://localhost:8080/chamado/all";
          break;

        case "2": // Responsável de setor
          endpointAPI = `http://localhost:8080/chamado/setorId/nao-concluidos?setorId=${setorId}`;
          break;

        case "3": // Funcionário comum
          endpointAPI = "http://localhost:8080/chamado/setorId/all-nao-concluidos";
          break;

        default:
          alert("Nível de permissão inválido.");
          return;
      }
    }

    console.log("Endpoint usado →", endpointAPI);



    const response = await fetch(endpointAPI, {
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

function calcularTempoDesdeAbertura(dataString) {
  if (!dataString) return "Data inválida";

  // Converter a string "yyyy-MM-dd HH:mm:ss" → Date
  const abertura = new Date(dataString.replace(" ", "T"));
  const agora = new Date();

  const diffMs = agora - abertura;
  const diffMin = Math.floor(diffMs / 60000);
  const diffHoras = Math.floor(diffMin / 60);
  const dias = Math.floor(diffHoras / 24);

  const horas = diffHoras % 24;
  const minutos = diffMin % 60;

  return `${dias}d ${horas}h ${minutos}min`;
}

function arrumarStringData(dataString) {
  if (!dataString) return "Data inválida";

  const data = new Date(dataString.replace(" ", "T"));

  if (isNaN(data.getTime())) return "Data inválida";

  const dia = String(data.getDate()).padStart(2, "0");
  const mes = String(data.getMonth() + 1).padStart(2, "0");
  const ano = data.getFullYear();

  const hora = String(data.getHours()).padStart(2, "0");
  const minuto = String(data.getMinutes()).padStart(2, "0");

  return `${dia}/${mes}/${ano} ${hora}:${minuto}`;
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
            ">
            ${ch.urgencia?.nome || "N/A"}
            <h6 class="card-title mb-1">Aberto em: ${arrumarStringData(ch.data)}   |   Tempo desde a abertura: ${calcularTempoDesdeAbertura(ch.data)}</h6>
        </p>
        </div>
        <span class="badge badge-id bg-primary fs-6">${statusNome}</span>
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
// 🔹 Carregar filtros (AGORA USANDO DROPDOWN)
// 🔹 Carregar filtros utilizando os ENDPOINTS CORRETOS
async function carregarFiltros() {
  const token = localStorage.getItem("jwtToken");
  const empresaId = localStorage.getItem("empresaId");

  async function carregarDropdownAPI(endpoint, buttonId, hiddenInputId) {
    try {
      const response = await fetch(`http://localhost:8080/api/${endpoint}/empresa/${empresaId}`, {
        headers: {
          "Authorization": `Bearer ${token.trim()}`,
          "Content-Type": "application/json"
        }
      });

      if (!response.ok) {
        console.error("Erro ao buscar dados do endpoint:", endpoint);
        return;
      }

      const dados = await response.json();

      const btn = document.getElementById(buttonId);
      const menu = btn.nextElementSibling;
      const hiddenInput = document.getElementById(hiddenInputId);

      // Limpa e adiciona "Todos"
      menu.innerHTML = `
                <li><a class="dropdown-item" href="#" data-value="">Todos</a></li>
            `;

      dados.forEach(item => {
        const li = document.createElement("li");
        li.innerHTML = `
                    <a class="dropdown-item" href="#" data-value="${item.nome}">
                        ${item.nome}
                    </a>
                `;
        menu.appendChild(li);
      });

      // Eventos
      menu.querySelectorAll(".dropdown-item").forEach(el => {
        el.addEventListener("click", (e) => {
          e.preventDefault();
          btn.textContent = el.textContent;
          hiddenInput.value = el.dataset.value;
        });
      });

    } catch (err) {
      console.error("Erro ao carregar dropdown:", err);
    }
  }

  // 🔸 Carregar STATUS
  await carregarDropdownAPI("status", "dropStatusFilter", "inputStatusFilter");

  // 🔸 Carregar URGÊNCIA
  await carregarDropdownAPI("urgencias", "dropUrgenciaFilter", "inputUrgenciaFilter");
}



// 🔹 Aplicar filtros
function aplicarFiltros() {
  const urgenciaFiltro = document.getElementById("inputUrgenciaFilter").value.toLowerCase();
  const statusFiltro = document.getElementById("inputStatusFilter").value.toLowerCase();

  chamadosFiltrados = todosChamados.filter((ch) => {
    const urg = ch.urgencia?.nome?.toLowerCase() || "";
    const sts = (ch.status?.nome || ch.status || "").toLowerCase();

    return (
      (urgenciaFiltro === "" || urg.includes(urgenciaFiltro)) &&
      (statusFiltro === "" || sts.includes(statusFiltro))

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
        ch.descricao.toLowerCase().includes(termo)
    );
    paginaAtual = 1;
    exibirChamados(resultados);
  });
});