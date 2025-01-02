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
    @Query(
  value = "SELECT * FROM detalle_inventario u WHERE u.id_inventario = ?1 ORDER BY u.id DESC", 
  nativeQuery = true)
    List<DetalleInventario> findAllByIdInventario(Long idInventario); 
    
    @Query("SELECT new com.krazystore.krazystore.DTO.DetalleInventarioDTO(p.id, p.nombre, p.categoria.id, p.categoria.descripcion, p.cantStock, 0) "
            + "FROM ProductoEntity p " )
    List<DetalleInventarioDTO> getDetallesInventarioIniciales();
    
    @Query("SELECT new com.krazystore.krazystore.Entity.DetalleInventario(d.id, p.id, p.nombre, p.categoria.id, p.categoria.descripcion, p.cantStock, COALESCE(d.cantContada, 0)) "
            + "FROM ProductoEntity p "
            + "LEFT JOIN DetalleInventario d ON d.producto = p AND "
            + "d.inventario.id = ?1 " )
    List<DetalleInventario> obtenerDetallesCompletos(Long id);
}
