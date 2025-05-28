/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.CostoEntity;
import java.util.Date;
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
public interface PrecioCompraRepository extends JpaRepository<CostoEntity, Long> {
    @Query("SELECT pc FROM CostoEntity pc " +
       "WHERE pc.fecha <= :fechaFactura " +
       "AND pc.producto.id IN :productosIds")
    List<CostoEntity> findPreciosByFechaAndProductos(@Param("fechaFactura") Date fechaFactura, 
                                              @Param("productosIds") List<Long> productosIds);
    
    @Query("SELECT pc FROM CostoEntity pc " +
       "WHERE pc.producto.id = :idProducto ORDER BY pc.fecha DESC ")
    List<CostoEntity> findPreciosByIdProducto(@Param("idProducto") Long id);

    @Query("SELECT pc FROM CostoEntity pc " +
       "WHERE pc.producto.id = :idProducto AND pc.fecha = :fecha ORDER BY pc.fecha DESC LIMIT 1 ")
    public Optional<CostoEntity> findByProductoIdAndFecha(@Param("idProducto") Long id,@Param("fecha") Date fecha);

    @Query("SELECT pc FROM CostoEntity pc " +
       "WHERE pc.producto.id = :idProducto AND pc.fecha <= :fecha ORDER BY pc.fecha DESC LIMIT 1 ")
    public Optional<CostoEntity> findCostoCercano(@Param("idProducto") Long id,@Param("fecha") Date fecha);
}

