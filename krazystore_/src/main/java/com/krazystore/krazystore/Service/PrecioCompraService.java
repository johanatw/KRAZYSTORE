/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.CostoEntity;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface PrecioCompraService {
    List<CostoEntity> findPreciosByFechaAndProductos( Date fechaFactura, List<Long> productosIds);
    
    List<CostoEntity> findAll();
    Optional<CostoEntity> findById(Long id);
    List<CostoEntity> findPreciosByIdProducto(Long id);
    CostoEntity savePrecioCompra(CostoEntity precioCompra);
    CostoEntity updatePrecioCompra(CostoEntity precioCompra, Long id);
    void deletePrecioCompra(Long id);
}
