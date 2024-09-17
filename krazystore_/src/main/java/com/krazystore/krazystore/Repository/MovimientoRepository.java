/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.MovimientosDTO;
import com.krazystore.krazystore.DTO.PRUEBADTO;
import com.krazystore.krazystore.Entity.MovimientoEntity;
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
public interface MovimientoRepository extends JpaRepository<MovimientoEntity, Long> {
    @Transactional
    @Modifying
    @Query(
  value = "DELETE FROM movimientos m WHERE m.id_reembolso IN ?1 ", 
  nativeQuery = true)
    void deleteMovimientosByReembolsos(List<Long> ids);
    
    @Transactional
    @Modifying
    @Query(
  value = "DELETE FROM movimientos m WHERE m.id_anticipo IN ?1 ", 
  nativeQuery = true)
    void deleteMovimientosByAnticipos(List<Long> ids);
    
    @Transactional
    @Modifying
    @Query(
  value = "DELETE FROM movimientos m WHERE m.id_anticipo = ?1 ", 
  nativeQuery = true)
    void deleteMovimientosByAnticipo(Long id);
    
    @Transactional
    @Modifying
    @Query(
  value = "DELETE FROM movimientos m WHERE m.id_reembolso = ?1 ", 
  nativeQuery = true)
    void deleteMovimientosByReembolso(Long id);
    
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
            + ", f.descripcion, pagos.importe) FROM MovimientoEntity m "
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
    
    //MovimientosDTO(Long id, Date fecha, String concepto, String formaPago
    
    //MovimientosDTO(Long id, Date fecha, String concepto, String formaPago, int total, int anticipo, int ingreso, int egreso, String factura)
    
}
