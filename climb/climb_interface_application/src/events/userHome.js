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
