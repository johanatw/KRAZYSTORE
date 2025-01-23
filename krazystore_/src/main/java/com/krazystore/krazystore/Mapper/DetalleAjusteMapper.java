/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Mapper;

import com.krazystore.krazystore.DTO.DetalleAjusteDTO;
import com.krazystore.krazystore.Entity.DetalleAjuste;
import com.krazystore.krazystore.Entity.ProductoEntity;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class DetalleAjusteMapper implements Function<DetalleAjusteDTO, DetalleAjuste>{
    @Override
    public DetalleAjuste apply(DetalleAjusteDTO detalleDTO){
        ProductoEntity productoEntity = new ProductoEntity();
        productoEntity.setId(detalleDTO.getIdProducto());
        return new DetalleAjuste(
                    detalleDTO.getId(),
                    detalleDTO.getAjuste(),
                    productoEntity,
                    detalleDTO.getCantidadAjustada()
        );
        
    }
    
}