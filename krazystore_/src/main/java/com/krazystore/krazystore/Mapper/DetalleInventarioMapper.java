/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Mapper;

import com.krazystore.krazystore.DTO.DetalleInventarioDTO;
import com.krazystore.krazystore.Entity.DetalleInventario;
import com.krazystore.krazystore.Entity.ProductoEntity;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class DetalleInventarioMapper implements Function<DetalleInventarioDTO, DetalleInventario>{
    @Override
    public DetalleInventario apply(DetalleInventarioDTO detalleDTO){
        ProductoEntity productoEntity = new ProductoEntity();
        productoEntity.setId(detalleDTO.getProducto().getId());
        return new DetalleInventario(
                    detalleDTO.getId(),
                    null,
                    productoEntity,
                    detalleDTO.getStockInicialInventario(),
                    detalleDTO.getCantContada(),
                    detalleDTO.getDiferencia()
        );
        
    }
    
}
