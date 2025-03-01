/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.AuthRequest;
import com.krazystore.krazystore.DTO.AuthResponse;
import com.krazystore.krazystore.Entity.PersonaEntity;
import com.krazystore.krazystore.Entity.Usuario;
import com.krazystore.krazystore.Service.UsuarioService;
import com.krazystore.krazystore.security.JwtTokenUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
  AuthenticationManager authenticationManager;
    
    @Autowired
  JwtTokenUtil jwtTokenUtil;
    
    @Autowired
  UsuarioService usuarioService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            System.out.println("autenticacion");
            // Autenticación del usuario
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), authRequest.getPassword()
                )
            );
            System.out.println("despues autenticacion");
            // Obtener detalles del usuario autenticado
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println("token");
            // Generación del token
            String token = jwtTokenUtil.generateToken(userDetails.getUsername());
            System.out.println("userDetails");
            System.out.println(userDetails.getUsername());
            System.out.println("tokenn");
            System.out.println(token);
            // Obtener roles
            List<String> roles = userDetails.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList());

            // Construir respuesta con token e información del usuario
            AuthResponse response = new AuthResponse(
                token,
                userDetails.getUsername(),
                roles
            );
            
            PersonaEntity persona = usuarioService.getPersonaByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));
            
            response.setNombre(persona.getNombre()+' '+persona.getApellido());

            System.out.println(response.getToken());
            System.out.println(response.getUsername());
            response.getRoles().forEach(r -> {System.out.println(r);});
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas.");
        }
    }
    
}
