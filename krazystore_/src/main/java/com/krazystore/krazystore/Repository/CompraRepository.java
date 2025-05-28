/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.CompraEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface CompraRepository extends JpaRepository<CompraEntity, Long> {
        @Query(
        "SELECT c FROM CompraEntity c "
               + "LEFT JOIN FETCH c.pedido p "
                + "WHERE p.id = ?1 AND c.tipoFactura = 'PROD'"
               )
        public List<CompraEntity> findByIdPedido(Long id);

        @Query(
        "SELECT c FROM CompraEntity c "
               + "LEFT JOIN c.pedido p "
                + "WHERE p.id IN ?1 AND c.tipoFactura = 'PROD'"
               )
    public List<CompraEntity> findFacturasProductosByIdsPedidos(List<Long> ids);

    @Query(
        "SELECT c FROM CompraEntity c "
               + "LEFT JOIN c.pedido p "
                + "WHERE p.id = ?1 AND c.tipoFactura = 'PROD'"
               )
    public List<CompraEntity> findFacturasByIdPedido(Long id);
}
