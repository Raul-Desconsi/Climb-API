package com.application.climb.Controller;

import com.application.climb.Model.Status;
import com.application.climb.Service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status")
@CrossOrigin(origins = "*") // permite requisições externas (opcional)
public class StatusController {

    @Autowired
    private StatusService statusService;

    @GetMapping
    public ResponseEntity<List<Status>> listarTodos() {
        List<Status> status = statusService.findAll();
        return ResponseEntity.ok(status);
    }


    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<Status>> listarPorEmpresa(@PathVariable Integer empresaId) {
        List<Status> status = statusService.listarPorEmpresaId(empresaId);
        return ResponseEntity.ok(status);}
    }