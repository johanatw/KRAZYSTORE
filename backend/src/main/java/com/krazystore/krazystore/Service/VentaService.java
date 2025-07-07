/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Service;


import com.krazystore.krazystore.DTO.CategoriaVentasDTO;
import com.krazystore.krazystore.DTO.ProductoVentasDTO;
import com.krazystore.krazystore.DTO.VentaCreationDTO;
import com.krazystore.krazystore.DTO.VentaRecepcionarDTO;
import com.krazystore.krazystore.Entity.DetalleVentaEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface VentaService {
    public List<VentaEntity> findAll();
    public Optional<VentaEntity> findById(Long id);
    public VentaEntity saveVenta(VentaEntity ventaEntity, List<DetalleVentaEntity> detalle);
    public int anularFactura(Long id);
    public void getFacturaPdf(HttpServletResponse response, Long id);
    public String generarPdf(Long id) throws FileNotFoundException;
    public List<VentaCreationDTO> findByIdPedido(Long idPedido);
    public List<CategoriaVentasDTO> obtenerVentasPorCategoriaMes(String mes);
    public List<ProductoVentasDTO> obtenerTopProductosVendidos(String mes);
    public VentaRecepcionarDTO findFacturaPrepararById(Long id);
}
