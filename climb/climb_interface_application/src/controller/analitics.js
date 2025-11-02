document.addEventListener("DOMContentLoaded", async () => {
    const empresaId = localStorage.getItem("empresaId");
    const token = localStorage.getItem("jwtToken");
    const chamado = JSON.parse(localStorage.getItem("chamadoSelecionado"));


    // Preenche campos do chamado
    document.getElementById("motivo").value = chamado.motivo || "";
    document.getElementById("problema").value = chamado.descricao || "";
    document.getElementById("responsavelAbertura").value = chamado.responsavelAbertura?.nome || "N/A";
    document.getElementById("setorAbertura").value = chamado.setorAbertura?.nome || chamado.setor?.nome || "N/A";
    if (chamado.data) {
        const data = new Date(chamado.data);
        const dia = String(data.getDate()).padStart(2, "0");
        const mes = String(data.getMonth() + 1).padStart(2, "0");
        const ano = data.getFullYear();
        document.getElementById("dataAberturaTexto").value = `${dia}/${mes}/${ano}`;
    }

    // Dropdowns selecionados
    if (chamado.areaAfetada) {
        document.getElementById("dropAreaAfetada").textContent = chamado.areaAfetada.nome;
        document.getElementById("areas-afetadas").value = chamado.areaAfetada.id;
    }
    if (chamado.urgencia) {
        document.getElementById("dropUrgencia").textContent = chamado.urgencia.nome;
        document.getElementById("inputUrgencia").value = chamado.urgencia.id;
    }
    if (chamado.status) {
        document.getElementById("dropStatus").textContent = chamado.status.nome;
        document.getElementById("inputStatus").value = chamado.status.id;
    }

    // ---------- ATENDIMENTOS ----------
    const containerForm = document.querySelector(".page-container:last-of-type .form-card ");
    const atendimentoForm = document.getElementById("atendimentoForm");

    async function carregarAtendimentos() {
        try {
            const resp = await fetch(`http://localhost:8080/atendimento/listarPorChamado?chamadoId=${chamado.id}`, {
                headers: { "Authorization": `Bearer ${token}` }
            });
            if (!resp.ok) throw new Error("Não foi possível carregar atendimentos");
            const atendimentos = await resp.json();

            function renderAtendimentoReadonly(at) {
                const div = document.createElement("div");
                div.className = "form-card mb-3";

                // --- Preenchendo campos com base na estrutura correta ---
                const responsavel = at.responsavelAtendimento?.nome || "N/A";
                const setor = at.setorAtendimento?.nome || "N/A";
                const data = at.data_atendimento ? new Date(at.data_atendimento) : null;
                const dataFormatada = data
                    ? `${String(data.getDate()).padStart(2, "0")}/${String(data.getMonth() + 1).padStart(2, "0")}/${data.getFullYear()}`
                    : "N/A";

                div.innerHTML = `
                <h5>Atendimento realizado por: <b>${responsavel}</b></h5>
                <p><b>Setor:</b> ${setor}</p>
                <p><b>Data:</b> ${dataFormatada}</p>
                <textarea class="form-control" rows="3" readonly>${at.resposta || ""}</textarea>
                <hr>
            
                <div class="seta-container"><i class="bi bi-arrow-down color-primary"></i></div>
            `;
                containerForm.parentNode.insertBefore(div, containerForm);
            }

            const temConclusao = atendimentos.some(a => a.conclusao_chamado === 1);

            if (atendimentos.length === 0) {
                atendimentoForm.style.display = "block";
            } else if (temConclusao) {
                atendimentos.forEach(renderAtendimentoReadonly);
                atendimentoForm.style.display = "none";
            } else {
                atendimentos.forEach(renderAtendimentoReadonly);
                atendimentoForm.style.display = "block";
            }

        } catch (err) {
            console.error(err);
        }
    }


    await carregarAtendimentos();

    // ----------- BOTÕES DE AÇÃO -----------
    const btnEnviar = document.querySelector("#btnEnviarEtapa");
    const btnConcluir = document.querySelector("#btnConcluirChamado");

    async function enviarAtendimento(concluir = false) {
        const resposta = document.getElementById("resposta").value;
        const statusId = document.getElementById("inputStatus").value;
        const setorDirecionadoId = document.getElementById("setor-direcionado")?.value || null;
        const setorAtendimentoId = localStorage.getItem("setor");
        const responsavelAtendimentoId = localStorage.getItem("id");


        if (!resposta.trim()) {
            alert("Digite a resposta do atendimento");
            return;
        }

        if (!statusId) {
            alert("Selecione o novo status");
            return;
        }

        if (!setorAtendimentoId || !responsavelAtendimentoId) {
            alert(setorAtendimentoId, responsavelAtendimentoId);

            return;
        }

        const payload = {
            chamadoId: chamado.id,
            resposta,
            statusId,
            setorDirecionadoId,
            setorAtendimentoId,
            responsavelAtendimentoId,
            conclusaoChamado: concluir ? 1 : 0
        };

        try {
            const token = localStorage.getItem("jwtToken");
            const resp = await fetch("http://localhost:8080/atendimento/create", {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload),
                mode: "cors"
            });


            if (!resp.ok) throw new Error("Erro ao enviar atendimento");

            alert(concluir ? "Chamado concluído!" : "Atendimento enviado!");
            window.location.href = "/pages/gerenciamentoTickets.html";

        } catch (e) {
            console.error(e);
            alert("Erro ao processar atendimento.");
        }
    }


    if (btnEnviar) btnEnviar.addEventListener("click", () => enviarAtendimento(false));
    if (btnConcluir) btnConcluir.addEventListener("click", () => enviarAtendimento(true));
});
