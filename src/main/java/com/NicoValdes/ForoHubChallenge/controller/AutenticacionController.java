package com.NicoValdes.ForoHubChallenge.controller;

import com.NicoValdes.ForoHubChallenge.DTOs.DatosAutenticacionUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;

@RestController
@RequestMapping("/login")
public class AutenticacionController {
    @Autowired
    private AuthenticationManager autenticationManager;

    @PostMapping
    public ResponseEntity Autenticar(DatosAutenticacionUsuario datosAutenticacionUsuario){
        Authentication token = new UsernamePasswordAuthenticationToken(datosAutenticacionUsuario.correo(), datosAutenticacionUsuario.contrasenia());
        autenticationManager.authenticate(token);
        return ResponseEntity.ok().build();
    }
}
