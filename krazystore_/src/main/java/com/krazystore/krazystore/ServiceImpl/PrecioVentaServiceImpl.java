/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.PreciosVentaEvent;
import com.krazystore.krazystore.Entity.PrecioVentaEntity;
import com.krazystore.krazystore.Repository.PrecioVentaRepository;
import com.krazystore.krazystore.Service.PrecioVentaService;
import java.util.List;
import java.util.Optional;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class PrecioVentaServiceImpl implements PrecioVentaService{

    private final PrecioVentaRepository precioVentaRepository;
    
    public PrecioVentaServiceImpl(PrecioVentaRepository precioVentaRepository) {
        this.precioVentaRepository = precioVentaRepository;
    }
    
    @Override
    public List<PrecioVentaEntity> findAll() {
        return precioVentaRepository.findAll();
    }

    @Override
    public Optional<PrecioVentaEntity> findById(Long id) {
        return precioVentaRepository.findById(id);
    }
    
    
    @Transactional
    @Override
    public PrecioVentaEntity savePrecioVenta(PrecioVentaEntity precioVenta) {
        return precioVentaRepository.save(precioVenta);
    }

    @Override
    public PrecioVentaEntity updatePrecioVenta(PrecioVentaEntity precioVenta, Long id) {
        PrecioVentaEntity updated = precioVentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontro registro"));
        
        updated.setFecha(precioVenta.getFecha());
        updated.setPrecio(precioVenta.getPrecio());
        updated.setProducto(precioVenta.getProducto());
        
        return precioVentaRepository.save(updated);
        
    }

    @Override
    public void deletePrecioVenta(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<PrecioVentaEntity> findPreciosByIdProducto(Long id) {
        return precioVentaRepository.findPreciosByIdProducto(id);
    }
    
    @EventListener
    public void handleNuevoPrevioVenta(PreciosVentaEvent event) {
        precioVentaRepository.saveAll(event.getPreciosActualizados());
    }
    
}
