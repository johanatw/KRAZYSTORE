/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Repository;


import com.krazystore.krazystore.Entity.DetalleVentaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVentaEntity, Long>{
    @Query(
  value = "SELECT * FROM detalle_ventas u WHERE u.id_venta = ?1 ", 
  nativeQuery = true)
    public List<DetalleVentaEntity> findByIdVenta(Long id);
}
