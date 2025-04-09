/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Mapper;

import com.krazystore.krazystore.DTO.DetalleEntregaDTO;
import com.krazystore.krazystore.Entity.DetalleEntrega;
import com.krazystore.krazystore.Entity.DetallePedidoEntity;
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
        ProductoEntity producto = new ProductoEntity(t.getDetallePedido().getProducto().getId(),
                t.getDetallePedido().getProducto().getNombre());
        System.out.println(t.getDetallePedido().getId());
        System.out.println(t.getDetallePedido().getProducto().getNombre());
        DetallePedidoEntity detallePedido = new DetallePedidoEntity(t.getDetallePedido().getId(), 
                producto, 
                t.getDetallePedido().getCantSolicitado());
        //Long id, int cantidad, int subTotal, ProductoEntity producto, int costoCompra
        return new DetalleEntrega(
                t.getId(),
                detallePedido,
                t.getCantidad()
        );
    }
}

