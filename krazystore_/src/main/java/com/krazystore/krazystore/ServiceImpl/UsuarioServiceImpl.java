/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.Usuario;
import com.krazystore.krazystore.Repository.UsuarioRepository;
import com.krazystore.krazystore.Service.UsuarioService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    public UsuarioServiceImpl(com.krazystore.krazystore.Repository.UsuarioRepository usuarioRespository) {
        this.usuarioRespository = usuarioRespository;
    }
    private final UsuarioRepository usuarioRespository;

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRespository.findByUsername(username);
    }

    @Override
    public List<String> getRolesByUsername(String username) {
        return usuarioRespository.getRolesByUsername(username);
    }
    
}
