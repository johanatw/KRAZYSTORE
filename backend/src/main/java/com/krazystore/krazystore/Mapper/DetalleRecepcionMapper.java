/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Mapper;

import com.krazystore.krazystore.DTO.DetalleRecepcionDTO;
import com.krazystore.krazystore.Entity.DetalleCompra;
import com.krazystore.krazystore.Entity.DetallePedidoCompra;
import com.krazystore.krazystore.Entity.DetalleRecepcion;
import com.krazystore.krazystore.Entity.ProductoEntity;
import com.krazystore.krazystore.Entity.RecepcionEntity;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class DetalleRecepcionMapper implements Function<DetalleRecepcionDTO, DetalleRecepcion>{
    @Override
    public DetalleRecepcion apply(DetalleRecepcionDTO detalleDTO) {
        RecepcionEntity recepcion = new RecepcionEntity(detalleDTO.getIdRecepcion());
    
        ProductoEntity producto = new ProductoEntity();
        producto.setId(detalleDTO.getDetalleCompra().getProducto().getId());
        
        DetalleCompra detalleCompra = new DetalleCompra(detalleDTO.getDetalleCompra().getId(), producto);

        return new DetalleRecepcion(
                detalleDTO.getId(),
                detalleDTO.getCantRecepcionado(),
                detalleDTO.getCantAceptada(),
                detalleDTO.getCantRechazada(),
                recepcion,
                detalleCompra
        );
    }
    
}
