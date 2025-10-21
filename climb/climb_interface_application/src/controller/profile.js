const userId = localStorage.getItem('id');
const empresaId = localStorage.getItem('empresaId');
const token = localStorage.getItem("jwtToken");

const userheader = document.getElementById("Userheader");
const NomeCompleto = document.getElementById("NomeCompleto");
const Email = document.getElementById("Email");
const Cpf = document.getElementById("Cpf");
const nivelPermissao = document.getElementById("nivelPermissao");
const Empresa = document.getElementById("Empresa");
const SetorSelect = document.getElementById("SetorDrop");
const CargoDrop = document.getElementById("CargoDrop");
const SaveBtn = document.getElementById("SaveUser");
const CancelUserBtn = document.getElementById("cancelUser");
let userDataGlobal = null;

const currentPassword = document.getElementById("currentPassword");
const confirmPassword = document.getElementById("confirmPassword");
const newPassword = document.getElementById("newPassword");

const cancelPasswordBtn = document.getElementById("cancelPasswordBtn");
const submitPasswordBtn = document.getElementById("submitPasswordBtn");

(async () => {
    await getFuncionario(userId);
    await Promise.all([
        getSetor(empresaId),
        getCargo(empresaId)
    ]);
})();

CancelUserBtn.addEventListener("click", reloadPage);
SaveBtn.addEventListener("click", (e) => {
    e.preventDefault();
    updateUser();
});
submitPasswordBtn.addEventListener("click", (e) => {
    e.preventDefault();
    updateUserPassword();
});
cancelPasswordBtn.addEventListener("click", reloadPage);

async function getFuncionario(userId) {
    const url = `http://localhost:8080/funcionario/GetFuncionario?id=${userId}`;
    try {
        const response = await fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json;charset=UTF-8",
                "Authorization": token
            }
        });
        if (!response.ok) throw new Error(`Erro na requisição: ${response.status}`);

        const userData = await response.json();
        userDataGlobal = userData;
        insertUser(userData);
    } catch (error) {
        console.error("Erro ao buscar funcionário:", error);
    }
}

async function getSetor(empresaId) {
    const url = `http://localhost:8080/api/setores/empresa/${empresaId}`;
    try {
        const response = await fetch(url, {
            method: "GET",
            headers: { "Content-Type": "application/json;charset=UTF-8" }
        });
        if (!response.ok) throw new Error(`Erro na requisição: ${response.status}`);
        const setores = await response.json();
        if (userDataGlobal) insertSetores(setores, userDataGlobal);
    } catch (error) {
        console.error("Erro ao buscar setores:", error);
    }
}

async function getCargo(empresaId) {
    const url = `http://localhost:8080/Cargo/Cargos/${empresaId}`;
    try {
        const response = await fetch(url, {
            method: "GET",
            headers: { "Content-Type": "application/json;charset=UTF-8" }
        });
        if (!response.ok) throw new Error(`Erro na requisição: ${response.status}`);
        const cargos = await response.json();
        if (userDataGlobal) insertCargos(cargos, userDataGlobal);
    } catch (error) {
        console.error("Erro ao buscar cargos:", error);
    }
}


function insertUser(user) {
    userheader.innerHTML = `
        <h1 class="display-5">${user.nome}</h1>
        <p class="lead">${user.email}</p>
        <p class="lead">${user.cargo}</p>
    `;
    NomeCompleto.value = user.nome || "";
    Email.value = user.email || "";
    Cpf.value = user.cpf || "";
    Empresa.value = user.empresa || "";

    const niveis = {
        1: "Administrador",
        2: "Operador",
        3: "Funcionário"
    };
    nivelPermissao.value = niveis[user.nivelPermissao] || "Não definido";
}

function insertSetores(setoresData, user) {
    SetorSelect.innerHTML = "";
    setoresData.forEach(setor => {
        const option = document.createElement("option");
        option.value = setor.id;
        option.textContent = setor.nome;
        SetorSelect.appendChild(option);
    });
    if (user?.setor) {
        const setorAtual = setoresData.find(s => s.nome === user.setor);
        if (setorAtual) SetorSelect.value = setorAtual.id;
    }
}

function insertCargos(cargosData, user) {
    CargoDrop.innerHTML = "";
    cargosData.forEach(cargo => {
        const option = document.createElement("option");
        option.value = cargo.id;
        option.textContent = cargo.nome;
        CargoDrop.appendChild(option);
    });
    if (user?.cargo) {
        const cargoAtual = cargosData.find(c => c.nome === user.cargo);
        if (cargoAtual) CargoDrop.value = cargoAtual.id;
    }
}


async function updateUser() {
    const url = "http://localhost:8080/funcionario/Update";
    if (!token) {
        alert("Token de autenticação não encontrado. Faça login novamente.");
        return;
    }

    const userData = {
        cpf: Cpf.value.trim(),
        email: Email.value.trim(),
        nome: NomeCompleto.value.trim(),
        cargo: parseInt(CargoDrop.value),
        setor: parseInt(SetorSelect.value)
    };

    for (const [campo, valor] of Object.entries(userData)) {
        if (!valor || valor === "" || (typeof valor === "number" && isNaN(valor))) {
            alert(`O campo "${campo}" não pode estar vazio.`);
            return;
        }
    }

    try {
        const response = await fetch(url, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json;charset=UTF-8",
                "Authorization": token
            },
            body: JSON.stringify(userData)
        });

        const txt = await response.text();
        let body;
        try { body = JSON.parse(txt); } catch { body = txt; }

        switch (response.status) {
            case 200:
            case 201:
                alert("Funcionário atualizado com sucesso!");
                reloadPage();
                break;
            case 400:
                alert(body.message || "Dados inválidos.");
                break;
            case 401:
                alert("Não autorizado. Faça login novamente.");
                localStorage.removeItem("jwtToken");
                window.location.href = "/login";
                break;
            case 403:
                alert(body || "Sem permissão para esta ação.");
                break;
            case 404:
                alert("Funcionário não encontrado.");
                break;
            default:
                alert(`Erro ${response.status}: ${body.message || body || "Erro desconhecido"}`);
        }
    } catch (error) {
        console.error("Erro na atualização:", error);
        alert("Erro ao atualizar o funcionário");
    }
}

function reloadPage() {
    location.reload();
}


async function updateUserPassword() {
    const url = "http://localhost:8080/funcionario/UpdatePassword";
    if (!token) {
        alert("Token de autenticação não encontrado. Faça login novamente.");
        return;
    }

    const oldPass = currentPassword.value.trim();
    const newPass = newPassword.value.trim();
    const confirmPass = confirmPassword.value.trim();

    if (!oldPass || !newPass || !confirmPass) {
        alert("Todos os campos de senha devem ser preenchidos.");
        return;
    }

    if (newPass !== confirmPass) {
        alert("A nova senha e a confirmação não coincidem.");
        return;
    }

    const userPasswordData = {
        oldPassword: oldPass,
        newPassword: newPass
    };

    try {
        const response = await fetch(url, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json;charset=UTF-8",
                "Authorization": token
            },
            body: JSON.stringify(userPasswordData)
        });

        const txt = await response.text();
        let body;
        try { body = JSON.parse(txt); } catch { body = txt; }

        switch (response.status) {
            case 200:
                alert("Senha atualizada com sucesso!");
                reloadPage();
                break;
            case 400:
                alert(body.message || "Erro desconhecido");
                break;
            case 403:
                alert(body.message || "Senha inválida.");
                break;
            case 401:
                alert("Não autorizado. Faça login novamente.");
                localStorage.removeItem("jwtToken");
                window.location.href = "/login";
                break;
            default:
                alert(`Erro ${response.status}: ${body.message || body || "Erro desconhecido"}`);
        }
    } catch (error) {
        console.error("Erro na atualização:", error);
        alert("Erro ao atualizar a senha");
    }
}
