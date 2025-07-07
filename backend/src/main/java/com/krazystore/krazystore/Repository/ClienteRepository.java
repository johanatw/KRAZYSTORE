/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.ClienteDTO;
import com.krazystore.krazystore.Entity.ClienteEntity;
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
public interface ClienteRepository extends JpaRepository<ClienteEntity,Long> {

    @Query(
    "SELECT new com.krazystore.krazystore.DTO.ClienteDTO(c.id, p)FROM ClienteEntity c "
            + "LEFT JOIN c.persona p "
            + "WHERE c.activo = TRUE "
           )
    public List<ClienteDTO> findAllDTO();

    @Query(
    "SELECT new com.krazystore.krazystore.DTO.ClienteDTO(c.id, p, d)FROM ClienteEntity c "
            + "LEFT JOIN c.persona p "
           + "LEFT JOIN DireccionEntity d "
            + "on d.persona = p and d.tipo = 'P' "
            + "WHERE c.id = ?1 "
           )
    public Optional<ClienteDTO> findClienteDTOById(Long id);
    
}
