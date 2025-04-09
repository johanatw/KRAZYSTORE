/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.PrecioVentaEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface PrecioVentaService {
    List<PrecioVentaEntity> findAll();
    Optional<PrecioVentaEntity> findById(Long id);
    List<PrecioVentaEntity> findPreciosByIdProducto(Long id);
    PrecioVentaEntity savePrecioVenta(PrecioVentaEntity precioVenta);
    PrecioVentaEntity updatePrecioVenta(PrecioVentaEntity precioVenta, Long id);
    void deletePrecioVenta(Long id);
}
