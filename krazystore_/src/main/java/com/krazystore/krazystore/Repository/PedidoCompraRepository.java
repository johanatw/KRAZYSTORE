/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.DetallePedidoRecepcionDTO;
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.PedidoCompraEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface PedidoCompraRepository extends JpaRepository<PedidoCompraEntity, Long> {
    @Query(
    "SELECT a "
            + "FROM AnticipoEntity a "
            + "WHERE a.idPedido = ?1 AND a.tipoPedido = 'C' "
           )
        List<AnticipoEntity> getAnticipos(Long id);
}
