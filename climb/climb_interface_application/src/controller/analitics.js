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
        // Substitui espaço por 'T' para compatibilidade com LocalDateTime Java
        const data = new Date(chamado.data.replace(" ", "T"));

        const dia = String(data.getDate()).padStart(2, "0");
        const mes = String(data.getMonth() + 1).padStart(2, "0");
        const ano = data.getFullYear();
        const hora = String(data.getHours()).padStart(2, "0");
        const minuto = String(data.getMinutes()).padStart(2, "0");

        // Exibe no formato dd/MM/yyyy HH:mm
        document.getElementById("dataAberturaTexto").value = `${dia}/${mes}/${ano} ${hora}:${minuto}`;
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
                const conclusaoChamado = at.conclusao_chamado === 1 ? "Sim" : "Não";
                const responsavel = at.responsavelAtendimento?.nome || "N/A";
                const setor = at.setorAtendimento?.nome || "N/A";
                const data = at.data_atendimento ? new Date(at.data_atendimento) : null;
                const setorDirecionado = at.setorDirecionado?.nome || "N/A";
                const dataFormatada = data

                    ? `${String(data.getDate()).padStart(2, "0")}/${String(data.getMonth() + 1).padStart(2, "0")}/${data.getFullYear()}`
                    : "N/A";

                div.innerHTML = `
                <form class="atendimentoExistente">
                <h2 class="mb-4 text-center text-primary color-primary">Resposta do chamado</h2>
        <label class="form-label">
        Responsável: <button class="borda" disabled style="border-radius: 8px; color: #000000ff; border-color: #fff" >${(responsavel || "")}</button> 
                                        &nbsp;&nbsp;&nbsp;&nbsp;    |                    Setor: <button class="borda " disabled style="border-radius: 8px; color: #000000ff; border-color: #fff" >${(setor || "")}</button> 
&nbsp;&nbsp;&nbsp;&nbsp;     |   Chamado Concluído? <button class="borda " disabled style="border-radius: 8px; color: #000000ff; border-color: #fff" >${(conclusaoChamado || "")}</button><br>
            </label>

          <div class="mb-3">
            <label class="form-label">Resposta do atendimento</label>
            <textarea class="form-control" rows="4" readonly>${at.resposta || ""}</textarea>
          </div>

          <div class="mb-3">
            <label class="form-label">Data do atendimento</label>
            <textarea class="form-control" rows="1" readonly>${dataFormatada || ""}</textarea>
          </div>


        <div class="mb-3">
            <label class="form-label">Setor que o atendimento foi direcionado</label>
            <textarea class="form-control" rows="1" readonly>${setorDirecionado || ""}</textarea>
          </div>

                
                <div class="seta-container"><i class="bi bi-arrow-down color-primary"></i></div>

        </form>
            `;
                containerForm.parentNode.insertBefore(div, containerForm);
            }

            const temConclusao = atendimentos.some(a => a.conclusao_chamado === 1);



            if (atendimentos.length === 0) {
                atendimentoForm.style.display = "block";
            } else if (temConclusao) {
                atendimentos.forEach(renderAtendimentoReadonly);
                atendimentoForm.style.display = "none";

                const div = document.createElement("div");
                div.className = "form-card mb-3 text-center p-3";

                div.innerHTML = `
            <div class="alert alert-success" role="alert" 
                 style="font-size:18px; font-weight:bold;">
                Chamado Concluído
            </div>
        `;

                containerForm.parentNode.insertBefore(div, containerForm);

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

    async function atualizarStatusChamado() {
        const token = localStorage.getItem("jwtToken");
        const chamado = JSON.parse(localStorage.getItem("chamadoSelecionado"));
        const statusId = document.getElementById("inputStatus").value;

        if (!statusId) {
            alert("Selecione um novo status.");
            return;
        }

        try {
            const resp = await fetch(`http://localhost:8080/chamado/atualizarStatus?id=${chamado.id}&statusId=${statusId}`, {
                method: "PUT",
                headers: {
                    "Authorization": `Bearer ${token}`
                },
                keepalive: true
            });

            if (!resp.ok) throw new Error("Falha ao atualizar");

            console.log("Status atualizado com sucesso.");

        } catch (err) {
            console.error("Erro ao atualizar status:", err);
        }
    }



    async function enviarAtendimento(concluir = false) {
        const resposta = document.getElementById("resposta").value;
        const setorDirecionadoId = document.getElementById("setor-direcionado")?.value || null;
        const setorAtendimentoId = localStorage.getItem("setor");
        const responsavelAtendimentoId = localStorage.getItem("id");


        if (!resposta.trim()) {
            alert("Digite a resposta do atendimento");
            return;
        }

        if (!setorAtendimentoId || !responsavelAtendimentoId) {
            alert(setorAtendimentoId, responsavelAtendimentoId);

            return;
        }

        const payload = {
            chamadoId: chamado.id,
            resposta,
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
                mode: "cors",
                keepalive: true
            });



            if (!resp.ok) throw new Error("Erro ao enviar atendimento");



            alert(concluir ? "Chamado concluído!" : "Atendimento enviado!");
            await carregarAtendimentos();

        } catch (e) {
            console.error(e);
        }
    }


    if (btnEnviar) {
    btnEnviar.addEventListener("click", async () => {
        try {
            // Atualiza status e envia atendimento (sem concluir)
            await atualizarStatusChamado();
            await enviarAtendimento(false);
        } catch (err) {
            console.error("Erro ao enviar etapa:", err);
        }
    });
}

if (btnConcluir) {
    btnConcluir.addEventListener("click", async () => {
        try {
            // Atualiza status e envia atendimento (concluindo)
            await atualizarStatusChamado();
            await enviarAtendimento(true);
        } catch (err) {
            console.error("Erro ao concluir chamado:", err);
        }
    });
}

});
