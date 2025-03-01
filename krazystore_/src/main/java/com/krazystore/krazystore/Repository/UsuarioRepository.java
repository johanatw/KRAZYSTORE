/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.PersonaEntity;
import com.krazystore.krazystore.Entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    @Query("SELECT u FROM Usuario u WHERE u.username = ?1 ")
    public Optional<Usuario> findByUsername(String username);
    
    @Query("SELECT r.descripcion FROM Rol r "
            + "JOIN UsuarioRol ur ON ur.rol = r "
            + "JOIN Usuario u ON u = ur.usuario "
            + "WHERE u.username = ?1 ")
    public List<String> getRolesByUsername(String username);
    
    @Query("SELECT p FROM PersonaEntity p "
            + "JOIN Usuario u on u.persona = p "
            + "WHERE u.username = ?1 ")
    Optional<PersonaEntity> getPersonaByUsername(String username);
}
