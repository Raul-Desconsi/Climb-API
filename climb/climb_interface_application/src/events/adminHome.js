document.addEventListener("DOMContentLoaded", () => {
    const logoutBtn = document.getElementById("log_out");
    
    const nomelbl = document.getElementById("nome");
    const funcaolbl = document.getElementById("funcao");

    insertUserInfo();

    function insertUserInfo() {
        const nome = localStorage.getItem('nome');
        const funcao = localStorage.getItem('funcao');

        nomelbl.innerText = nome
        funcaolbl.innerText = funcao

    }


    // Verificação de autenticação
    if (!localStorage.getItem("jwtToken") || localStorage.getItem("nivelPermissao") !== "1") {
        location.href = 'login.html';
        return;
    }

    // Botão logout (só adiciona se existir no DOM)
    if (logoutBtn) {
        logoutBtn.addEventListener("click", () => {
            localStorage.removeItem('jwtToken');
            localStorage.removeItem('setor');
            localStorage.removeItem('id');
            localStorage.removeItem('nome');
            localStorage.removeItem('funcao');
            location.href = 'login.html';
        });
    }
});

// Função genérica para carregar iframes
function loadIframe(maincontent, src) {
    if (!maincontent) return;
    maincontent.innerHTML = "";
    const iframe = document.createElement("iframe");
    iframe.src = src;
    iframe.width = "100%";
    iframe.height = "100%";
    iframe.style.border = "none";
    maincontent.appendChild(iframe);
}

function dashboardbtn(maincontent) {
    loadIframe(maincontent, "/climb/climb_interface_application/src/page/adminDashboard");
}

function funcionariosbtn(maincontent) {
    loadIframe(maincontent, "/climb/climb_interface_application/src/page/adminFuncionarios.html");
}

function accountBtn(maincontent) {
    loadIframe(maincontent, "/climb/climb_interface_application/src/page/perfil.html");
}

function setoresBtn(maincontent) {
    loadIframe(maincontent, "/climb/climb_interface_application/src/page/adminSetores.html");
}

// Captura cliques nos botões
document.addEventListener("click", function (event) {
    const maincontent = document.getElementById("main-content");
    if (!maincontent) return;

    switch (event.target.id) {
        case "dashboardbtn":
            dashboardbtn(maincontent);
            break;
        case "funcionariosbtn":
            funcionariosbtn(maincontent);
            break;
        case "setoresbtn":
            setoresBtn(maincontent);
            break;
        case "accountBtn":
            accountBtn(maincontent);
            break;
    }
});


