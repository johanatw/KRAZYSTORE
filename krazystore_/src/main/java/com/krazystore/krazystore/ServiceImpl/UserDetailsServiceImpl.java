/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.Usuario;
import com.krazystore.krazystore.Service.UsuarioService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("useerdateilsimpl");
        System.out.println(username);
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        System.out.println(usuario.getUsername());
        List<String> roles = usuarioService.getRolesByUsername(username);
        
        roles.forEach(a ->{System.out.println(a);});
        
        List<GrantedAuthority> authorities = roles.stream()
                               .map(role -> new SimpleGrantedAuthority(role))
                               .collect(Collectors.toList());
        return new User(
            usuario.getUsername(),
            usuario.getPassword(),
            authorities
        );
    }
    
}
