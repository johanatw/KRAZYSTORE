/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Mapper;

import com.krazystore.krazystore.DTO.DetallePedidoCompraDTO;
import com.krazystore.krazystore.Entity.DetallePedidoCompra;
import com.krazystore.krazystore.Entity.PedidoCompraEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class DetallePedidoCompraMapper implements Function<DetallePedidoCompraDTO, DetallePedidoCompra> {

    @Override
    public DetallePedidoCompra apply(DetallePedidoCompraDTO t) {
        ProductoEntity producto = new ProductoEntity();
        producto.setId(t.getIdProducto());
        
        PedidoCompraEntity pedido = new PedidoCompraEntity();
        pedido.setId(t.getIdPedidoCompra());
        return new DetallePedidoCompra(
                t.getId(),
                producto,
                pedido,
                t.getCantidad(),
                t.getCantRecepcionada(),
                t.getSubTotal(),
                t.getCostoCompra()
        );
    }
    
}
