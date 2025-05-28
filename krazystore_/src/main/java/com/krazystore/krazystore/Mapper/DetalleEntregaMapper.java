/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Mapper;

import com.krazystore.krazystore.DTO.DetalleEntregaDTO;
import com.krazystore.krazystore.Entity.DetalleEntrega;
import com.krazystore.krazystore.Entity.DetallePedidoEntity;
import com.krazystore.krazystore.Entity.DetalleVentaEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class DetalleEntregaMapper implements Function<DetalleEntregaDTO, DetalleEntrega> {
    @Override
    public DetalleEntrega apply(DetalleEntregaDTO t) {
        System.out.println("DETALLENTREGAMAPPER");
        ProductoEntity producto = new ProductoEntity(t.getDetalleVenta().getProducto().getId(),
                t.getDetalleVenta().getProducto().getNombre());
 
       
        //Long id, int cantidad, int subTotal, ProductoEntity producto, int costoCompra
        return new DetalleEntrega(
                t.getId(),
                t.getDetalleVenta(),
                t.getCantidad()
        );
    }
}

