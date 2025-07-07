/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.InventarioDTO;
import com.krazystore.krazystore.Entity.InventarioEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface InventarioRepository extends JpaRepository<InventarioEntity, Long>  {
    
    @Query("SELECT new com.krazystore.krazystore.DTO.InventarioDTO(i.id, i.estado, i.fecha ) "
        + "FROM InventarioEntity i ORDER BY i.id DESC ")
    List<InventarioDTO> findAllInventarios();
    
}
