package com.application.climb.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.application.climb.Model.Urgencia;
import com.application.climb.Service.UrgenciaService;

@RestController
@RequestMapping("/api/urgencias")
@CrossOrigin(origins = "*")
public class UrgenciaController {

    @Autowired
    private UrgenciaService urgenciaService;

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<Urgencia>> listarPorEmpresa(@PathVariable Integer empresaId) {
        List<Urgencia> urgencias = urgenciaService.listarPorEmpresa(empresaId);
        return ResponseEntity.ok(urgencias);
    }
}
