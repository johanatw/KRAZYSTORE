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
                    detalleEntity.getProducto().getId(),
                    detalleEntity.getProducto().getNombre(),
                    detalleEntity.getProducto().getSubCategoria(),
                    detalleEntity.getProducto().getCantStock(),
                    detalleEntity.getCantidadAjustada(),
                    detalleEntity.getCantidadAnterior(),
                    detalleEntity.getCantidadFinal(),
                    detalleEntity.getMotivoAjuste()
        );
        
    }
    
}
