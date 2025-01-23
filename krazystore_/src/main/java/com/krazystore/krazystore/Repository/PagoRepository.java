/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.DetallePagoPedidoDTO;
import com.krazystore.krazystore.DTO.PRUEBADTO;
import com.krazystore.krazystore.DTO.PedidoMontoPagadoDTO;
import com.krazystore.krazystore.Entity.PagoEntity;
import java.util.List;
import java.util.Optional;
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
public interface PagoRepository extends JpaRepository<PagoEntity,Long> {
    
    @Transactional
    @Modifying
    @Query(
  value = "DELETE FROM pagos p USING movimientos m WHERE p.id_movimiento IN ?1 ", 
  nativeQuery = true)
    void deletePagosByMovimientos(List<Long> ids);
    
    @Transactional
    @Modifying
    @Query(
  value = "DELETE FROM pagos p USING movimientos m WHERE p.id_movimiento = m.id AND m.id = ?1 ", 
  nativeQuery = true)
    void deletePagosByMovimiento(Long id);
    /*
    @Query(
    "SELECT new com.krazystore.krazystore.DTO.DetallePagoPedidoDTO(p.id, p.total, SUM(CASE WHEN f.descripcion <> 'Anticipo' AND c.descripcion <> 'Reembolso' THEN e.importe "
            + " ELSE 0 END) - SUM(CASE WHEN c.descripcion = 'Reembolso' THEN e.importe ELSE 0 END)) "
            
        
            + " FROM PagoEntity e "
            + "LEFT JOIN e.formaPago f "
            + "LEFT JOIN e.movimiento m "
            + "LEFT JOIN m.concepto c "
            + "LEFT JOIN m.anticipo a "
            + "LEFT JOIN a.pedido p "
            + "LEFT JOIN m.venta v "
            + "LEFT JOIN v.pedido pv "
            + "WHERE p.id = ?1 OR pv.id = ?1 "
            + "GROUP BY p.id"
           )*/
    /*
     @Query(
    "SELECT new com.krazystore.krazystore.DTO.DetallePagoPedidoDTO(p.id, p.total, SUM(CASE WHEN f.descripcion <> 'Anticipo' AND c.descripcion <> 'Reembolso' THEN e.importe "
            + " ELSE 0 END) - SUM(CASE WHEN c.descripcion = 'Reembolso' THEN e.importe ELSE 0 END)) "
            
        
            + " FROM PagoEntity e "
            + "LEFT JOIN e.formaPago f "
            + "LEFT JOIN e.movimiento m "
            + "LEFT JOIN m.concepto c "
            + "LEFT JOIN m.anticipo a "
            + "LEFT JOIN a.pedido p "
            + "LEFT JOIN m.venta v "
            + "LEFT JOIN v.pedido pv "
            + "WHERE p.id = ?1 OR pv.id = ?1 "
            + "GROUP BY p.id"
           )*/
     
      /*@Query(
    "SELECT new com.krazystore.krazystore.DTO.PedidoMontoPagadoDTO(p.id as id, p.total, SUM(CASE WHEN f.descripcion <> 'Anticipo' AND c.descripcion <> 'Reembolso' THEN p1.importe ELSE 0 END) - SUM(CASE WHEN c.descripcion = 'Reembolso' THEN p1.importe ELSE 0 END) ) "
            
        
            + " FROM PedidoEntity p "
            + "LEFT JOIN AnticipoEntity a "
            + "ON a.pedido = p "
            + "LEFT JOIN VentaEntity v "
            + "ON v.pedido = p "
            + "LEFT JOIN ReembolsoEntity r "
            + "ON r.anticipo = a "
           + "LEFT JOIN MovimientoEntity m "
            + "ON m.anticipo = a OR m.venta = v OR m.reembolso = r "
            + "LEFT JOIN PagoEntity p1 "
            + "ON p1.movimiento = m "
            + "LEFT JOIN m.concepto c "
            + "LEFT JOIN p1.formaPago f "
            + "WHERE p.id = ?1  "
            + "GROUP BY p.id"
           )*/
   /* @Query(
    "SELECT new com.krazystore.krazystore.DTO.PedidoMontoPagadoDTO(p.id as id, p.total, SUM(CASE WHEN f.descripcion <> 'Anticipo' AND c.descripcion <> 'Reembolso' AND c.descripcion <> 'Anulación de Factura' THEN p1.importe ELSE 0 END) - SUM(CASE WHEN f.descripcion <> 'Anticipo' AND (c.descripcion = 'Reembolso' OR c.descripcion = 'Anulación de Factura') THEN p1.importe ELSE 0 END) ) "
            + " FROM PagoEntity p1 "
            + "LEFT JOIN p1.formaPago f "
            + "LEFT JOIN p1.movimiento m "
            + "LEFT JOIN m.concepto c "
            + "LEFT JOIN AnticipoEntity a "
            + "ON m.anticipo = a "
            + "LEFT JOIN VentaEntity v "
            + "ON m.venta = v "
            + "LEFT JOIN ReembolsoEntity r "
            + "ON m.reembolso = r "
            + "LEFT JOIN r.anticipo a1 "
            + "LEFT JOIN PedidoEntity p "
            + "ON a.pedido = p OR v.pedido = p OR a1.pedido = p "
            + "WHERE p.id = ?1  "
            + "GROUP BY p.id"
           )
    public Optional<PedidoMontoPagadoDTO> getPagosPedido(Long id);*/
    
    @Query(
    "SELECT p FROM PagoEntity p "
           + "LEFT JOIN FETCH p.movimiento m "
            + "WHERE m.id = ?1 "
           )
    public List<PagoEntity> findByIdMovimiento(Long id);
}
