/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Repository;


import com.krazystore.krazystore.DTO.CategoriaVentasDTO;
import com.krazystore.krazystore.DTO.ProductoVentasDTO;
import com.krazystore.krazystore.Entity.VentaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface VentaRepository extends JpaRepository<VentaEntity, Long>{
    public List<VentaEntity> findAllByOrderByIdDesc();

    @Query(
    "SELECT new com.krazystore.krazystore.DTO.CategoriaVentasDTO(" +
        "c.descripcion, " +
        "SUM(COALESCE(d.cantidad,0)) as total " +
    ") " +
    "FROM VentaEntity v " +
    "LEFT JOIN DetalleVentaEntity d ON d.venta = v " +
    "LEFT JOIN d.producto p " +
    "LEFT JOIN p.subCategoria s " +
    "LEFT JOIN s.categoria c " +
    "WHERE v.estado <> 'A' AND to_char(v.fecha, 'YYYY-mm') = ?1 " +
    "GROUP BY c.descripcion " +
    "ORDER BY total DESC"
)
    public List<CategoriaVentasDTO> obtenerVentasPorCategoriaMes(String mes);

    @Query(
    "SELECT new com.krazystore.krazystore.DTO.ProductoVentasDTO(" +
        "p.nombre, " +
        "SUM(COALESCE(d.cantidad,0)) as total, " +
        "CASE WHEN p.cantStock < 1 THEN 'Sin Stock' ELSE null END as estado " +
    ") " +
    "FROM VentaEntity v " +
    "LEFT JOIN DetalleVentaEntity d ON d.venta = v " +
    "LEFT JOIN d.producto p " +
    "WHERE v.estado <> 'A' AND to_char(v.fecha, 'YYYY-mm') = ?1 " +
    "GROUP BY p.nombre, estado " +
    "ORDER BY total DESC "+
    "LIMIT 10 "
)
    public List<ProductoVentasDTO> obtenerTopProductosVendidos(String mes_);

    @Query(
    "SELECT new com.krazystore.krazystore.Entity.VentaEntity( " +
        "v.id, " +
        "v.nroFactura, " +
        "v.fecha " +
    ") " +
    "FROM VentaEntity v " +
    "LEFT JOIN v.pedido p " +
    "WHERE p.id = ?1 and v.estado <> 'A' " +
    "GROUP BY v.id " 
    )
    public List<VentaEntity> findByIdPedido(Long idPedido);

    @Query(
    "SELECT case when (count(e) > 0) then true else false end " +
    "FROM EntregaEntity e " +
    "LEFT JOIN e.venta v " +
    "WHERE v.id = ?1 and e.estado <> 'X' " 
    )
    public Boolean tieneProductosPreparadosParaEntrega(Long id);
}
