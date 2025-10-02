package com.application.climb.Controller;

import com.application.climb.Model.Setor;
import com.application.climb.Service.SetorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/setores")
@CrossOrigin(origins = "*") // permite requisições externas (opcional)
public class SetorController {

    @Autowired
    private SetorService setorService;

    @GetMapping
    public ResponseEntity<List<Setor>> listarTodos() {
        List<Setor> setores = setorService.listarTodos();
        return ResponseEntity.ok(setores);
    }


    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<Setor>> listarPorEmpresa(@PathVariable Integer empresaId) {
        List<Setor> setores = setorService.listarPorEmpresaId(empresaId);
        return ResponseEntity.ok(setores);
    }

   


    @GetMapping("/{id}")
    public ResponseEntity<Setor> buscarPorId(@PathVariable Integer id) {
        Optional<Setor> setor = setorService.buscarPorId(id);
        return setor.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Setor> criarSetor(@RequestBody Setor setor) {
        Setor novoSetor = setorService.salvar(setor);
        return ResponseEntity.ok(novoSetor);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Setor> atualizarSetor(@PathVariable Integer id, @RequestBody Setor setorAtualizado) {
        try {
            Setor setor = setorService.atualizar(id, setorAtualizado);
            return ResponseEntity.ok(setor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSetor(@PathVariable Integer id) {
        try {
            setorService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
