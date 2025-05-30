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
import com.krazystore.krazystore.Entity.CategoriaEntity;
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
    List<VentaEntity> findAll();
    Optional<VentaEntity> findById(Long id);
    VentaEntity saveVenta(VentaEntity ventaEntity, List<DetalleVentaEntity> detalle);
    VentaEntity updateVenta(VentaCreationDTO ventaDTO, Long id)throws Exception;
    int anularFactura(Long id);
    VentaEntity updateVenta(VentaEntity VentaEntity, Long id);
    void deleteVenta(Long id);
    void getFacturaPdf(HttpServletResponse response, Long id);
    String generarPdf(Long id) throws FileNotFoundException;
    public List<VentaCreationDTO> findByIdPedido(Long idPedido);

    public List<CategoriaVentasDTO> obtenerVentasPorCategoriaMes(String mes);

    public List<ProductoVentasDTO> obtenerTopProductosVendidos(String mes);

    public VentaRecepcionarDTO findFacturaPrepararById(Long id);
}
