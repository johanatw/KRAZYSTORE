/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.DetalleInventarioDTO;
import com.krazystore.krazystore.Entity.DetalleInventario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface DetalleInventarioRepository extends JpaRepository<DetalleInventario, Long>  {
    @Query("SELECT new com.krazystore.krazystore.Entity.DetalleInventario(d.id, d.inventario, d.producto, COALESCE(d.cantStock, 0), COALESCE(d.cantContada, 0), COALESCE(d.diferencia, 0)) "
            + "FROM DetalleInventario d "
            + "WHERE d.inventario.id = ?1 " )
    List<DetalleInventario> findAllByIdInventario(Long idInventario); 
    
    @Query("SELECT new com.krazystore.krazystore.Entity.DetalleInventario(p) "
            + "FROM ProductoEntity p " )
    List<DetalleInventario> getDetallesInventarioIniciales();
    
    @Query("SELECT new com.krazystore.krazystore.Entity.DetalleInventario(d.id, i, p, COALESCE(d.cantStock, 0), COALESCE(d.cantContada, 0), COALESCE(d.diferencia, 0)) "
            + "FROM ProductoEntity p "
            + "LEFT JOIN InventarioEntity i ON i.id = ?1 "
            + "LEFT JOIN DetalleInventario d ON d.producto = p AND d.inventario = i "
 
             )
    List<DetalleInventario> obtenerDetallesCompletos(Long id);
}
