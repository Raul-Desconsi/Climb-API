const emailInput = document.getElementById("email");
const senhaInput = document.getElementById("senha");
const submitLoginBtn = document.getElementById("submitLogin");

submitLoginBtn.addEventListener('click', submitLogin);

function submitLogin(event) {
    event.preventDefault(); 

    const url = "http://localhost:8080/funcionario/login";
    const request = new XMLHttpRequest();

    try {
        const email = emailInput.value.trim().toLowerCase();
        const senha = senhaInput.value.trim();

        if (!email || senha === '') {
            window.alert("Por favor, preencha todos os campos");
            return;
        }

        const LoginData = {
            email: email,
            senha: senha
        };

        const LoginForm = JSON.stringify(LoginData);

        request.open('POST', url, true);
        request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

        request.onreadystatechange = function () {
            if (request.readyState === XMLHttpRequest.DONE) {
                const status = request.status;
                let response = {};
                try {
                    response = JSON.parse(request.responseText);
                } catch (e) {
                    
                }

                if (status === 200 && response.token) {
                    console.log("Login bem-sucedido:", response);
                    localStorage.setItem('jwtToken', response.token);
                    localStorage.setItem('setor', response.setor);
                    localStorage.setItem('id', response.id);

                    
                   

                    switch (response.nivelPermissao) {
                        case 1:
                        location.href = 'adminHome.html';
                        break;
                    
                        case 2:
                        location.href = 'userHome.html';
                        break;

                        default:
                            break;
                    } 



                } else if (status === 401) {
                    window.alert(response.message || "Email ou senha incorretos");

                } else if (status === 403) {
                    window.alert(response.message || "Usuário sem permissão para acessar o sistema");
                
                } else {
                    window.alert("Erro desconhecido: " + status);
                }
            }
        };

        request.send(LoginForm);

    } catch (error) {
        console.log(error);
        window.alert("Ocorreu um erro inesperado");
    }
}