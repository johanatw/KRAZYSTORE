/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.EstadoPagoPedidoDTO;
import com.krazystore.krazystore.DTO.MovimientosDTO;
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
    
    @Query(
    "SELECT new com.krazystore.krazystore.DTO.EstadoPagoPedidoDTO( " +
    "    p, p.total, " +
    "    SUM(COALESCE(a.total, 0)) - COALESCE(r.totalReembolsado, 0) " +
    ") " +
    "FROM PedidoEntity p " +
    "LEFT JOIN AnticipoEntity a ON a.pedido.id = p.id " +
    "LEFT JOIN ( " +
    "    SELECT t.anticipo.pedido.id AS pedidoId, SUM(t.monto) AS totalReembolsado " +
    "    FROM ReembolsoAnticipo t " +
    "    GROUP BY t.anticipo.pedido.id " +
    ") r ON r.pedidoId = p.id " +
    "WHERE p.id = ?1 " +
    "GROUP BY p.id, p.total, r.totalReembolsado"
)
public Optional<EstadoPagoPedidoDTO> getEstadoPagoPedidoVenta(Long id);

    
    @Query(
    "SELECT new com.krazystore.krazystore.DTO.MovimientosDTO(" +
        "m.id, " +
        "m.fecha, " +
        "c.descripcion, " +
        "COALESCE(f.descripcion, f1.descripcion, ''), " +
        "COALESCE(p.importe, p1.importe, 0), " +
        "m.nroDocumento, " +
        "m.estado, " +
        "c.tipo, " +
        "cli, " +
        "pr, " +
        "m.observacion " +
    ") " +
    "FROM MovimientoEntity m " +
    "LEFT JOIN m.concepto c " +
    "LEFT JOIN m.caja caja " +
    "LEFT JOIN FormaPagoEntity p ON p.movimiento = m " +
    "LEFT JOIN p.medioPago f " +
    "LEFT JOIN FormaCobroEntity p1 ON p1.movimiento = m " +
    "LEFT JOIN p1.medioCobro f1 " + // Ajustado: p1.medio en lugar de p.medio
    "LEFT JOIN m.cliente cli " +
    "LEFT JOIN m.proveedor pr " +
    "WHERE caja.id = ?1 " +
    "ORDER BY m.fecha DESC"
)
public List<MovimientosDTO> findByIdCaja(Long id);

@Query(
    "SELECT new com.krazystore.krazystore.DTO.MovimientosDTO(" +
        "m.id, " +
        "m.fecha, " +
        "c.descripcion, " +
        "CONCAT('Anticipo #', aa.anticipo.id, ' Aplicado')," +
        "COALESCE(aa.monto,0), " +
        "m.nroDocumento, " +
        "m.estado, " +
        "cli, " +
        "pr " +
    ") " +
    "FROM MovimientoEntity m " +
    "LEFT JOIN m.concepto c " +
    "LEFT JOIN m.caja caja " +
    "LEFT JOIN m.cliente cli " +
    "LEFT JOIN m.proveedor pr " +
    "JOIN AplicacionAnticipo aa ON aa.movimiento = m " +
    "WHERE caja.id = ?1 " +
    "ORDER BY m.fecha DESC"
)
public List<MovimientosDTO> findAnticiposAplicadosByIdCaja(Long id);
    
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
           + "LEFT JOIN FETCH m.compra c "
            + "WHERE c.id = ?1 "
           )
    public MovimientoEntity findByIdCompra(Long id);
    
    @Query(
    "SELECT m FROM MovimientoEntity m "
           + "LEFT JOIN FETCH m.reembolso r "
            + "WHERE r.id = ?1 "
           )
    public MovimientoEntity findByIdReembolso(Long id);
    
    @Query(
  value = "SELECT m.id FROM movimientos m WHERE m.id_reembolso_anticipo IN ?1 ", 
  nativeQuery = true)
    List<Long> findByIdsReembolsos(List<Long> reembolsosIds);
    
    @Transactional
    @Modifying
    @Query(
  value = "DELETE FROM movimientos m WHERE m.id IN ?1 ", 
  nativeQuery = true)
    void deleteByIds(List<Long> movimientos);
    
    
    @Query(
        "SELECT m FROM MovimientoEntity m " +
        "JOIN m.venta c " +
        "WHERE m.estado = 'P' " +
        "ORDER BY m.id DESC")
    List<MovimientoEntity> getMovimientosPendientesDeCobro();
    
    @Query(
        "SELECT m FROM MovimientoEntity m " +
        "JOIN m.compra c " +
        "WHERE m.estado = 'P' " +
        "ORDER BY m.id DESC")
      List<MovimientoEntity> getMovimientosPendientesDePago();
    
    @Query(value = "SELECT TO_CHAR(m.fecha, 'YYYY-mm') AS mes, " +
               "SUM(CASE WHEN c.descripcion = 'Anticipo cliente' THEN COALESCE(fc.importe,0) ELSE 0 END) AS ingresos_anticipos, " +
                "SUM(CASE WHEN c.descripcion = 'Venta' THEN COALESCE(fc.importe,0) ELSE 0 END) AS ingresos_ventas, " +
                "SUM(CASE WHEN c.descripcion <> 'Venta' "
            +   "           AND c.descripcion <> 'Anticipo cliente' "
            + "             AND c.tipo = 'I' THEN COALESCE(fc.importe,0) ELSE 0 END) AS ingresos_varios " +
               "FROM movimientos m " +
                "LEFT JOIN conceptos c ON m.id_concepto = c.id " +
                "LEFT JOIN formas_pago fp ON m.id = fp.id_movimiento " +
             "LEFT JOIN formas_cobro fc ON m.id = fc.id_movimiento " +
               "WHERE EXTRACT(YEAR FROM m.fecha) = ?1 " +
               "GROUP BY TO_CHAR(m.fecha, 'YYYY-mm') " +
               "ORDER BY mes ASC", nativeQuery = true)
    List<Object[]> obtenerIngresosPorAño(Integer año);
    
    @Query("SELECT DISTINCT EXTRACT(YEAR FROM m.fecha) FROM MovimientoEntity m ORDER BY EXTRACT(YEAR FROM m.fecha) ASC")
    List<Integer> obtenerAñosDisponibles();

    @Query(value = "SELECT TO_CHAR(m.fecha, 'YYYY-mm') AS mes, " +
                "SUM(CASE WHEN c.descripcion = 'Compra' THEN COALESCE(fp.importe,0) ELSE 0 END) AS egresos_compras, " +
            "SUM(CASE WHEN c.descripcion <> 'Compra' "
            + "         AND c.tipo = 'E' THEN COALESCE(fp.importe,0) ELSE 0 END) AS egresos_varios " +
               "FROM movimientos m " +
                "LEFT JOIN conceptos c ON m.id_concepto = c.id " +
                "LEFT JOIN formas_pago fp ON m.id = fp.id_movimiento " +
               "WHERE EXTRACT(YEAR FROM m.fecha) = ?1 " +
               "GROUP BY TO_CHAR(m.fecha, 'YYYY-mm') " +
               "ORDER BY mes ASC", nativeQuery = true)
    public List<Object[]> obtenerEgresosPorAño(Integer año);

    @Query(value = "SELECT TO_CHAR(m.fecha, 'YYYY-mm') AS mes, " +
               "SUM(CASE WHEN c.descripcion = 'Anticipo cliente' THEN COALESCE(fc.importe,0) ELSE 0 END) AS ingresos_anticipos, " +
                "SUM(CASE WHEN c.descripcion = 'Venta' THEN COALESCE(fc.importe,0) ELSE 0 END) AS ingresos_ventas, " +
                "SUM(CASE WHEN c.descripcion <> 'Venta' "
            +   "           AND c.descripcion <> 'Anticipo cliente' "
            + "             AND c.tipo = 'I' THEN COALESCE(fc.importe,0) ELSE 0 END) AS ingresos_varios, " +
                "SUM(CASE WHEN c.descripcion = 'Compra' THEN COALESCE(fp.importe,0) ELSE 0 END) AS egresos_compras, " +
            "SUM(CASE WHEN c.descripcion <> 'Compra' "
            + "         AND c.tipo = 'E' THEN COALESCE(fp.importe,0) ELSE 0 END) AS egresos_varios " +
               "FROM movimientos m " +
                "LEFT JOIN conceptos c ON m.id_concepto = c.id " +
                "LEFT JOIN formas_pago fp ON m.id = fp.id_movimiento " +
             "LEFT JOIN formas_cobro fc ON m.id = fc.id_movimiento " +
               "WHERE EXTRACT(YEAR FROM m.fecha) = ?1 " +
               "GROUP BY TO_CHAR(m.fecha, 'YYYY-mm') " +
               "ORDER BY mes ASC", nativeQuery = true)
    public List<Object[]> obtenerIngresosYEgresosPorAño(Integer año);
}



