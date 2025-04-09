/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.PreciosCompraActualizadosEvent;
import com.krazystore.krazystore.Entity.CostoEntity;
import com.krazystore.krazystore.Repository.PrecioCompraRepository;
import com.krazystore.krazystore.Service.PrecioCompraService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class PrecioCompraServiceImpl implements PrecioCompraService {
    private final PrecioCompraRepository precioCompraRepository;
    
    public PrecioCompraServiceImpl(PrecioCompraRepository precioCompraRepository) {
        this.precioCompraRepository = precioCompraRepository;
    }
    @Override
    public List<CostoEntity> findPreciosByFechaAndProductos(Date fechaFactura, List<Long> productosIds) {
        return precioCompraRepository.findPreciosByFechaAndProductos(fechaFactura, productosIds);
    }
    
            
    @EventListener
    public void handleCambioExistencias(PreciosCompraActualizadosEvent event) {
        precioCompraRepository.saveAll(event.getPreciosActualizados());
    }

    @Override
    public List<CostoEntity> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<CostoEntity> findById(Long id) {
        return precioCompraRepository.findById(id);
    }

    @Override
    public CostoEntity savePrecioCompra(CostoEntity precioCompra) {
        return precioCompraRepository.save(precioCompra);
    }

    @Override
    public CostoEntity updatePrecioCompra(CostoEntity precioCompra, Long id) {
        CostoEntity updated = precioCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontro registro"));
        
        updated.setFecha(precioCompra.getFecha());
        updated.setCosto(precioCompra.getCosto());
        updated.setProducto(precioCompra.getProducto());
        
        return precioCompraRepository.save(updated);
    }

    @Override
    public void deletePrecioCompra(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CostoEntity> findPreciosByIdProducto(Long id) {
        return precioCompraRepository.findPreciosByIdProducto(id);
    }
}
