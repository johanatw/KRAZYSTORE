/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.DetalleEntregaDTO;
import com.krazystore.krazystore.Entity.DetalleEntrega;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Repository
public interface DetalleEntregaRepository extends JpaRepository<DetalleEntrega, Long> {
    @Query("SELECT new com.krazystore.krazystore.DTO.DetalleEntregaDTO( " +
       "d.id, dv.id,dv.producto, " +
       "dv.cantidad, d.cantidad ) " +
       "FROM DetalleEntrega d " +
       "JOIN d.entrega e " +
       "LEFT JOIN d.detalleVenta dv " +
       "WHERE e.id = :idEntrega")
    List<DetalleEntregaDTO> findDetallesByIdEntrega(@Param("idEntrega") Long idEntrega);

    @Modifying
    @Transactional
    @Query("DELETE FROM DetalleEntrega de  " +
       "WHERE de.entrega.id = ?1")
    public void deleteByIdEntrega(Long id);
    
}
