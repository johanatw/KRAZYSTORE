/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.EntregaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Repository
public interface EntregaRepository extends JpaRepository<EntregaEntity, Long> {

    @Transactional
    @Modifying
    @Query(
        "UPDATE EntregaEntity e SET e.venta = NULL, e.estado = ?2 WHERE e.venta.id = ?1 ")
    public void desasociarVentaEntrega(Long id, Character estadoEntrega);
    
    
    
}
