/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.RecepcionDTO;
import com.krazystore.krazystore.Entity.RecepcionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface RecepcionRepository extends JpaRepository<RecepcionEntity, Long> {

    @Query("SELECT new com.krazystore.krazystore.DTO.RecepcionDTO(r.id, r.fecha, r.estado ) "
        + "FROM RecepcionEntity r "
        + "WHERE r.id = ?1")
    Optional<RecepcionDTO> findRecepcion(Long idRecepcion);    
    
    @Query("SELECT new com.krazystore.krazystore.DTO.RecepcionDTO(r.id, r.fecha, p.id, r.estado ) "
        + "FROM RecepcionEntity r "
        + "LEFT JOIN CompraRecepcion fr ON fr.recepcion = r "
        + "LEFT JOIN fr.compra c "
        + "LEFT JOIN c.pedido p "
        + "ORDER BY r.id DESC ")
    List<RecepcionDTO> findAllRecepciones();
        
    @Query("SELECT new com.krazystore.krazystore.DTO.RecepcionDTO( " +
       "r.id, r.fecha, r.estado ) " +
       "FROM RecepcionEntity r " 
       + "LEFT JOIN CompraRecepcion fr ON fr.recepcion = r "
        + "LEFT JOIN fr.compra c "
        + "LEFT JOIN c.pedido p " +
       "WHERE p.id = :idPedido " +
        "GROUP BY r.id, r.fecha, p.id ")
    List<RecepcionDTO> findRecepcionesByIdPedido(@Param("idPedido") Long idPedido);
    
    
}
