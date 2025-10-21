package com.application.climb.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.application.climb.Model.Cargo;
import com.application.climb.Service.CargoService;


@Controller
@RequestMapping("Cargo")
public class CargoController {

    @Autowired
    CargoService cargoService;

 @GetMapping("/Cargos/{empresaId}")
    public ResponseEntity<List<Cargo>> getCargosFromEmpresa(@PathVariable Long empresaId) {
        List<Cargo> Cargos = cargoService.findCargosByEmpresaId(empresaId);
        return ResponseEntity.ok(Cargos);
    }


}

    

