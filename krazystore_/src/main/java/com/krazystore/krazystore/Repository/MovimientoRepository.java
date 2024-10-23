/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.MovimientosDTO;
import com.krazystore.krazystore.DTO.PRUEBADTO;
import com.krazystore.krazystore.Entity.MovimientoEntity;
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
public interface MovimientoRepository extends JpaRepository<MovimientoEntity, Long> {
    
    public List<MovimientoEntity> findAllByOrderByIdDesc();
    
    /*
    @Query(
   "SELECT m FROM MovimientoEntity m LEFT JOIN FETCH m.formaPago f "
           + "LEFT JOIN FETCH m.anticipo a LEFT JOIN FETCH a.cliente ac "
           + "LEFT JOIN FETCH m.reembolso r LEFT JOIN FETCH"
           + " m.venta v LEFT JOIN FETCH m.concepto c "
           + "WHERE m.id = 31")*/
    @Query(
    "SELECT new com.krazystore.krazystore.DTO.PRUEBADTO(m.id, c.descripcion"
            
            + ", m.fecha"
            + ", f.descripcion, pagos.importe, v.nroFactura) FROM MovimientoEntity m "
           + "LEFT JOIN m.concepto as c "
            + "LEFT JOIN m.anticipo a "
            + "LEFT JOIN m.reembolso r "
            + "LEFT JOIN m.venta v "
            + "LEFT JOIN a.pedido p "
            + "LEFT JOIN v.pedido pv "
            + "LEFT JOIN r.anticipo ra "
            + "LEFT JOIN ra.pedido pr "
            + "LEFT JOIN PagoEntity pagos "
            + "ON pagos.movimiento = m "
            + "LEFT JOIN pagos.formaPago f "
            + "WHERE p.id = ?1 OR pv.id = ?1 OR pr.id = ?1 "
            + "ORDER BY m.id DESC"
           )
    public List<PRUEBADTO> prueba(Long id);
     
  
    @Query(
    "SELECT new com.krazystore.krazystore.DTO.MovimientosDTO(m.id, m.fecha, c.descripcion, f.descripcion"
            + ", p.importe, CASE WHEN p.anticipo IS NULL AND c.tipo = 'I' THEN p.importe ELSE 0 END"
            + ", CASE WHEN c.tipo = 'E' THEN p.importe ELSE 0 END, m.nroDocumento) FROM MovimientoEntity m "
           + "LEFT JOIN m.concepto as c "
            + "LEFT JOIN PagoEntity p "
            + "ON p.movimiento = m "
            + "LEFT JOIN p.formaPago f "
            + "ORDER BY m.id DESC"
           )
    public List<MovimientosDTO> findMovimientosDTO();
    
    @Query(
    "SELECT new com.krazystore.krazystore.DTO.MovimientosDTO(m.id, m.fecha, c.descripcion, COALESCE(f.descripcion, '')"
            + ", p.importe"
            + ", m.nroDocumento, m.estado, c.tipo ) FROM MovimientoEntity m "
           + "LEFT JOIN m.concepto as c "
            + "LEFT JOIN m.caja as ca "
            + "LEFT JOIN PagoEntity p "
            + "ON p.movimiento = m "
            + "LEFT JOIN p.formaPago f "
            + "WHERE ca.id = ?1 "
            + "ORDER BY m.id DESC"
           )
    public List<MovimientosDTO> findByIdCaja(Long id);
    
    @Query(
    "SELECT m FROM MovimientoEntity m "
           + "LEFT JOIN FETCH m.anticipo a "
            + "WHERE a.id = ?1 "
           )
    public MovimientoEntity findByIdAnticipo(Long id);
    
    @Query(
    "SELECT m FROM MovimientoEntity m "
           + "LEFT JOIN FETCH m.venta v "
            + "WHERE v.id = ?1 "
           )
    public MovimientoEntity findByIdVenta(Long id);
    
    @Query(
    "SELECT m FROM MovimientoEntity m "
           + "LEFT JOIN FETCH m.reembolso r "
            + "WHERE r.id = ?1 "
           )
    public MovimientoEntity findByIdReembolso(Long id);
    
    @Query(
  value = "SELECT m.id FROM movimientos m WHERE m.id_reembolso IN ?1 ", 
  nativeQuery = true)
    List<Long> findByIdsReembolsos(List<Long> reembolsosIds);
    
    @Transactional
    @Modifying
    @Query(
  value = "DELETE FROM movimientos m WHERE m.id IN ?1 ", 
  nativeQuery = true)
    void deleteByIds(List<Long> movimientos);
    
    
    @Query(
  value = """
          SELECT COUNT(DISTINCT(m.id)) FROM movimientos m
          left join anticipos a
          on a.id = m.id_anticipo
          left join ventas v
          on v.id_venta = m.id_venta
          left join pedidos p
          on p.id_pedido = a.id_pedido
          or p.id_pedido = v.id_pedido 
          where (m.id_anticipo is not null OR (m.id_venta is not null AND v.id_pedido is not null)) 
          AND p.id_pedido = ?1""", 
  nativeQuery = true)
    Long validateOrderDeletion(Long idPedido);
    
    @Query(
  value = """
          SELECT * FROM movimientos m 
          where m.estado = false 
          """, 
  nativeQuery = true)
    List<MovimientoEntity> getFacturasPendientes();
}
