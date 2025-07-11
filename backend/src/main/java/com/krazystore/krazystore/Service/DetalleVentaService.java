/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Service;



import com.krazystore.krazystore.DTO.DetalleVentaPrepararDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.DetalleVentaEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */

public interface DetalleVentaService {
    List<DetalleVentaEntity> findAll();
    Optional<DetalleVentaEntity> findById(Long id);
    List<ProductoExistenciasDTO> saveDetalleVenta(VentaEntity venta, List<DetalleVentaEntity> detalle);
    List<DetalleVentaEntity> findByIdVenta(Long id);
    List<ProductoExistenciasDTO> anularDetalleVenta(Long idVenta);
    public List<DetalleVentaPrepararDTO> findDetallesFacturaRecepcionarByIdVenta(Long id);
}
