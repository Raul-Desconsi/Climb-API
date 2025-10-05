package com.application.climb.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.application.climb.Service.AuthService;
import com.application.climb.Service.EmpresaService;



@Controller
@RequestMapping("Empresa")


public class EmpresaController {

@Autowired
EmpresaService empresaService;

@Autowired
AuthService authService;




}
