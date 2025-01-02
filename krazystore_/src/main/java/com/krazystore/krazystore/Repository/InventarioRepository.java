/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.DetalleInventarioDTO;
import com.krazystore.krazystore.DTO.InventarioDTO;
import com.krazystore.krazystore.Entity.CategoriaEntity;
import com.krazystore.krazystore.Entity.InventarioEntity;
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
public interface InventarioRepository extends JpaRepository<InventarioEntity, Long>  {
    @Query("SELECT new com.krazystore.krazystore.DTO.InventarioDTO(i.id, i.estado, i.fecha) "
        + "FROM InventarioEntity i "
        + "WHERE i.id = ?1")
    Optional<InventarioDTO> findInventario(Long id);

    @Query("SELECT new com.krazystore.krazystore.DTO.DetalleInventarioDTO(d.id, p.id, p.nombre, p.categoria.id, p.categoria.descripcion, d.cantStock, d.cantContada, d.diferencia) "
            + "FROM DetalleInventario d "
            + "JOIN d.producto p "
            + "WHERE d.inventario.id = ?1")
    List<DetalleInventarioDTO> findDetallesByIdInventario(Long idInventario);
    
    @Query("SELECT new com.krazystore.krazystore.DTO.InventarioDTO(i.id, i.estado, i.fecha ) "
        + "FROM InventarioEntity i ")
    List<InventarioDTO> findAllInventarios();
    
    @Query("SELECT DISTINCT(c) "
            + "FROM DetalleInventario d "
            + "JOIN CategoriaEntity c ON d.producto.categoria = c "
            + "JOIN InventarioEntity i ON d.inventario = i "
            + "WHERE i.id = ?1 " )
    List<CategoriaEntity> obtenerFiltros(Long id);
}
