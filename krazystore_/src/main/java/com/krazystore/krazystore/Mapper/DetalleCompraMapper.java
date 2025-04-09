/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Mapper;

import com.krazystore.krazystore.DTO.DetalleCompraDTO;
import com.krazystore.krazystore.Entity.DetalleCompra;
import com.krazystore.krazystore.Entity.ProductoEntity;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class DetalleCompraMapper implements Function<DetalleCompraDTO, DetalleCompra> {

    @Override
    public DetalleCompra apply(DetalleCompraDTO t) {
        ProductoEntity producto = new ProductoEntity();
        producto.setId(t.getProducto().getId());
        //Long id, int cantidad, int subTotal, ProductoEntity producto, int costoCompra
        return new DetalleCompra(
                t.getId(),
                t.getCantidad(),
                t.getSubTotal(),
                producto,
                t.getCostoCompra(),
                t.getIvaAplicado()
        );
    }
    
}
