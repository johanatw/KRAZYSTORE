/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Mapper;

import com.krazystore.krazystore.DTO.DetalleAjusteDTO;
import com.krazystore.krazystore.Entity.DetalleAjuste;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class DetalleAjusteDTOMapper implements Function<DetalleAjuste, DetalleAjusteDTO>{
    @Override
    public DetalleAjusteDTO apply(DetalleAjuste detalleEntity){
        return new DetalleAjusteDTO(
                    detalleEntity.getId(),
                    detalleEntity.getAjuste(),
                    detalleEntity.getProducto().getId(),
                    detalleEntity.getProducto().getNombre(),
                    detalleEntity.getProducto().getSubCategoria().getId(),
                    detalleEntity.getProducto().getSubCategoria().getDescripcion(),
                    detalleEntity.getProducto().getCantStock(),
                    detalleEntity.getCantidadAjustada()
        );
        
    }
    
}
