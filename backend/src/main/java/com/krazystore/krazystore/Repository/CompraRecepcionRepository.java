/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.CompraRecepcion;
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
public interface CompraRecepcionRepository extends JpaRepository<CompraRecepcion, Long>{

    @Query("SELECT p.id "
        + "FROM CompraRecepcion fr "
        + "LEFT JOIN fr.compra c "
        + "LEFT JOIN fr.recepcion r "
        + "LEFT JOIN c.pedido p "
        + "WHERE r.id = ?1 "
        + "ORDER BY p.id DESC ")
    public List<Long> getIdsPedidosComprasRecepcionadosByIdRecepcion(Long idRecepcion);

    @Transactional
    @Modifying
    @Query("DELETE "
        + "FROM CompraRecepcion fr "
        + "WHERE fr.recepcion.id = ?1 ")
    public void deleteComprasRecepcionadosByIdRecepcion(Long idRecepcion);

    @Query("SELECT fr "
        + "FROM CompraRecepcion fr "
        + "LEFT JOIN fr.compra c "
        + "WHERE c.id = ?1 "
        + "ORDER BY fr.id DESC ")
    public List<CompraRecepcion> getCompraRecepcionesByIdCompra(Long id);
    
}
