/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.DetalleAjusteDTO;
import com.krazystore.krazystore.Entity.DetalleAjuste;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface DetalleAjusteRepository extends JpaRepository<DetalleAjuste,Long> {
    /*@Query("SELECT new com.krazystore.krazystore.DTO.DetalleAjusteDTO(d.id, d.ajuste, p.id, p.nombre, p.categoria.id, p.categoria.descripcion, d.cantidadAjustada) "
            + "FROM DetalleAjuste d "
            + "LEFT JOIN ProductoEntity p ON d.producto = p "
            + "AND d.ajuste.id = ?1 " )*/
    @Query("SELECT d "
            + "FROM DetalleAjuste d "
            + "WHERE d.ajuste.id = ?1 " )
    List<DetalleAjuste> findAllByIdAjuste(Long idAjuste); 
    
    /*@Query("SELECT new com.krazystore.krazystore.DTO.DetalleAjusteDTO(d.id, d.ajuste, p.id, p.nombre, p.categoria.id, p.categoria.descripcion, p.cantStock, 0) "
            + "FROM ProductoEntity p "
            + "LEFT JOIN DetalleAjuste d ON d.producto = p " )*/
    @Query("SELECT new com.krazystore.krazystore.DTO.DetalleAjusteDTO(p.id, p.nombre, p.categoria.id, p.categoria.descripcion, p.cantStock, 0) "
            + "FROM ProductoEntity p " )
    List<DetalleAjusteDTO> obtenerProductosParaAjuste();
    
}
