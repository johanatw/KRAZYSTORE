/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Mapper;

import com.krazystore.krazystore.DTO.DetalleInventarioDTO;
import com.krazystore.krazystore.Entity.DetalleInventario;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class DetalleInventarioDTOMapper implements Function<DetalleInventario, DetalleInventarioDTO>{
    @Override
    public DetalleInventarioDTO apply(DetalleInventario detalleEntity){
        return new DetalleInventarioDTO(
                    detalleEntity.getId(),
                    detalleEntity.getProducto().getId(),
                    detalleEntity.getProducto().getNombre(),
                    detalleEntity.getProducto().getSubCategoria(),
                    detalleEntity.getProducto().getCantStock(),
                    detalleEntity.getCantStock(),
                    detalleEntity.getCantContada(),
                    detalleEntity.getDiferencia()
        );
    }
    
}
