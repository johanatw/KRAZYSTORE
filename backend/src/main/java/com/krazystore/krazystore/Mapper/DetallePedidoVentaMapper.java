/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Mapper;

import com.krazystore.krazystore.DTO.DetallePedidoDTO;
import com.krazystore.krazystore.Entity.DetallePedidoEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */

@Service
public class DetallePedidoVentaMapper implements Function<DetallePedidoDTO, DetallePedidoEntity> {

    @Override
    public DetallePedidoEntity apply(DetallePedidoDTO t) {
        ProductoEntity producto = new ProductoEntity();
        producto.setId(t.getProducto().getId());
        
        return new DetallePedidoEntity(
                t.getId(),
                producto,
                t.getCantSolicitado(),
                t.getSubTotal(),
                t.getPrecio()
        );
    }
    
}