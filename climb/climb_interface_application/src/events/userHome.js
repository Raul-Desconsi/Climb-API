document.addEventListener("DOMContentLoaded", () => {
    const logoutBtn = document.getElementById("logout");

    if (!localStorage.getItem("jwtToken") || localStorage.getItem("nivelPermissao") !== "2") {
    location.href = 'login.html';
}


    logoutBtn.addEventListener("click", callLogout);

    

    function callLogout() {
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('setor');
        localStorage.removeItem('id');
        location.href = 'login.html';
    }
});
function callRegisterFlow(maincontent, iframe) {
  maincontent.innerHTML = "";
  iframe.src = "/Climb-API/climb/climb_interface_application/src/page/registerFlow.html"; 
  maincontent.appendChild(iframe);
}

function chamadosBtn(maincontent) {
  maincontent.innerHTML = "";
  const iframe = document.createElement("iframe"); // cria aqui, porque é novo
  iframe.src = "/Climb-API/climb/climb_interface_application/src/page/ticket.html"; 
  maincontent.appendChild(iframe);
}

function accountBtn(maincontent) {
    maincontent.innerHTML = "";
    const iframe = document.createElement("iframe"); 
    iframe.src = "/Climb-API/climb/climb_interface_application/src/page/account.html"; 
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
  }
});
