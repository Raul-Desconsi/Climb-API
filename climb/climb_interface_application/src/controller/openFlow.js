document.getElementById("fluxoForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const token = localStorage.getItem("jwtToken");
    const responsavelId = localStorage.getItem("id");
    const setorId = localStorage.getItem("setor");

    const payload = {
        motivo: document.getElementById("motivo").value.trim(),
        descricao: document.getElementById("problema").value.trim(),
        areaAfetadaId: parseInt(document.getElementById("areas-afetadas").value),
        urgenciaId: parseInt(document.getElementById("inputUrgencia").value),
        responsavelAberturaId: parseInt(responsavelId),
        setorId: setorId ? parseInt(setorId) : undefined
    };

    try {
        const response = await fetch("http://localhost:8080/chamado/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": token
            },
            body: JSON.stringify(payload)
        });

        const text = await response.text();
        const body = text ? JSON.parse(text) : {};

        if (response.ok) {
            alert("Chamado cadastrado com sucesso! ID: " + (body.id || ""));
            document.getElementById("fluxoForm").reset();
        } else {
            alert(body.message || `Erro ${response.status}`);
        }
    } catch (err) {
        console.error(err);
        alert("Erro de conexão ao cadastrar chamado.");
    }
});
