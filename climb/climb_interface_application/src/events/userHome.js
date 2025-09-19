document.addEventListener("DOMContentLoaded", () => {
    const adicionarBtn = document.getElementById("adicionarBtn");
    const maincontent = document.getElementById("main-content");

    adicionarBtn.addEventListener("click", callRegisterFlow);

    function callRegisterFlow() {
        maincontent.innerHTML = "";

        const iframe = document.createElement("iframe");
        iframe.src = "/climb/climb_interface_application/src/page/registerFlow.html"; 
       
        

        maincontent.appendChild(iframe);
    }
});


