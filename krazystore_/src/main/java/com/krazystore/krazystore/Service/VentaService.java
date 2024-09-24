/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Service;


import com.krazystore.krazystore.Entity.CategoriaEntity;
import com.krazystore.krazystore.Entity.DetalleVentaEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface VentaService {
    List<VentaEntity> findAll();
    Optional<VentaEntity> findById(Long id);
    VentaEntity saveVenta(VentaEntity ventaEntity, List<DetalleVentaEntity> detalle, List<PagoEntity> pagos);
    void anularFactura(Long id);
    VentaEntity updateVenta(VentaEntity VentaEntity, Long id);
    void deleteVenta(Long id);
}
