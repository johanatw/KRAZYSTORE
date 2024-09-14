/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.DTO.DetalleVentaCreationRequest;
import com.krazystore.krazystore.Entity.DetalleVentaEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import com.krazystore.krazystore.Repository.DetalleVentaRepository;
import com.krazystore.krazystore.Repository.VentaRepository;
import com.krazystore.krazystore.Service.DetalleVentaService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author HP
 */
@Service
public class DetalleVentaServiceImpl implements DetalleVentaService{
    private final DetalleVentaRepository detalleventarepository;
 

    public DetalleVentaServiceImpl(DetalleVentaRepository detalleventarepository) {
        this.detalleventarepository = detalleventarepository;
    }

    
    @Override
    public List<DetalleVentaEntity> findAll() {
        return detalleventarepository.findAll();
    }

    @Override
    public Optional<DetalleVentaEntity> findById(Long id) {
        return detalleventarepository.findById(id);
    }

    @Override
    public Iterable<DetalleVentaEntity> saveDetalleVenta(VentaEntity venta, List<DetalleVentaEntity> detalle) {

        detalle.forEach(d -> {
            d.setVenta(venta);
            
      
        });
        
        return detalleventarepository.saveAll(detalle);
    }

    @Override
    public DetalleVentaEntity updateDetalleVenta(DetalleVentaEntity detalleVentaEntity, Long id) {
        DetalleVentaEntity updatedDetalleVenta = detalleventarepository.findById(id).get();
        
        updatedDetalleVenta.setVenta(detalleVentaEntity.getVenta());
        updatedDetalleVenta.setProducto(detalleVentaEntity.getProducto());
        updatedDetalleVenta.setCantidad(detalleVentaEntity.getCantidad());
        updatedDetalleVenta.setPrecio(detalleVentaEntity.getPrecio());
       // updatedDetalleVenta.setMontoIva(detalleVentaEntity.getMontoIva());
        updatedDetalleVenta.setSubTotal(detalleVentaEntity.getSubTotal());
        
        return detalleventarepository.save(updatedDetalleVenta);
    }

    @Override
    public void deleteDetalleVenta(Long id) {
        detalleventarepository.deleteById(id);
    }
    
}
