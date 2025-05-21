/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Mapper;

import com.krazystore.krazystore.DTO.ProductoDTO;
import com.krazystore.krazystore.Entity.ProductoEntity;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class ProductoEntityMapper implements Function<ProductoDTO, ProductoEntity>{
    @Override
    public ProductoEntity apply(ProductoDTO productoDTO){
        System.out.println("PRODUCTOMAPPER");
        Boolean esServicio;
        if(productoDTO.getEsServicio()!=null){
            esServicio = productoDTO.getEsServicio();
        }else{
            esServicio = false;
        }
        
        return new ProductoEntity(
                    productoDTO.getId(),
                productoDTO.getNombre(),
                productoDTO.getDescripcion(),
                productoDTO.getSubCategoria(),
                productoDTO.getTipoIva(),
                productoDTO.getBajoDemanda(),
                productoDTO.getCantLimBajoDemanda(),
                esServicio
        );
        
    }
}

