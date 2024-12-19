/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.DetalleCompra;
import java.util.List;
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
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {
    @Query(
  value = "SELECT * FROM detalle_compra u WHERE u.id_compra = ?1 ORDER BY u.id DESC", 
  nativeQuery = true)
    List<DetalleCompra> findAllByIdCompra(Long idCompra); 
    
    @Transactional
     @Modifying
     @Query(
  value = "DELETE FROM detalle_compra u WHERE u.id_compra = ?1 ", 
  nativeQuery = true)
    void deleteAllByIdCompra(Long idCompra);
}
