/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.Usuario;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface UsuarioService {
    public Optional<Usuario> findByUsername(String username);
    public List<String> getRolesByUsername(String username);
}
