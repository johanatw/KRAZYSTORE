/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.PrecioVentaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface PrecioVentaRepository extends JpaRepository<PrecioVentaEntity, Long> {
    @Query("SELECT pv FROM PrecioVentaEntity pv " +
       "WHERE pv.producto.id = :idProducto ORDER BY pv.fecha DESC ")
    List<PrecioVentaEntity> findPreciosByIdProducto(@Param("idProducto") Long id);
}
