document.addEventListener("DOMContentLoaded", () => {
    const logoutBtn = document.getElementById("log_out");

   const nomelbl = document.getElementById("nome");
    const funcaolbl = document.getElementById("funcao");

    insertUserInfo();

    function insertUserInfo() {
        const nome = localStorage.getItem('nome');
        const funcao = localStorage.getItem('cargo');

        nomelbl.innerText = nome
        funcaolbl.innerText = funcao

    }


    if (!localStorage.getItem("jwtToken")) {
    location.href = 'login.html';
}


    logoutBtn.addEventListener("click", callLogout);

    

    function callLogout() {
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('setor');
        localStorage.removeItem('nivelPermissao');
        localStorage.removeItem('id');
        localStorage.removeItem('cargo');
        location.href = 'login.html';
    }
});
function callRegisterFlow(maincontent, iframe) {
  maincontent.innerHTML = "";
  iframe.src = "../page/registerFlow.html"; 
  maincontent.appendChild(iframe);
}

function chamadosBtn(maincontent) {
  maincontent.innerHTML = "";
  const iframe = document.createElement("iframe"); // cria aqui, porque é novo
  iframe.src = "../page/tickets.html"; 
  maincontent.appendChild(iframe);
}

function accountBtn(maincontent) {
    maincontent.innerHTML = "";
    const iframe = document.createElement("iframe"); 
    iframe.src = "../page/profile.html"; 
    maincontent.appendChild(iframe);}

function dashboardBtn(maincontent) {
    maincontent.innerHTML = "";
    const iframe = document.createElement("iframe"); 
    iframe.src = "../page/dashboard.html"; 
    maincontent.appendChild(iframe);}

document.addEventListener("click", function (event) {
  const maincontent = document.getElementById("main-content");
  const iframe = document.createElement("iframe");

  switch (event.target.id) {
    case "adicionarBtn":
      callRegisterFlow(maincontent, iframe);
      break;
    case "chamadosBtn":
      chamadosBtn(maincontent, iframe); // chama a função correta
      break;
    case "accountBtn":
        accountBtn(maincontent, iframe);
        break; // chama a função correta
    case "dashboardBtn":
        dashboardBtn(maincontent, iframe);
        break; // chama a função correta
       
  }
});

