/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Mapper;

import com.krazystore.krazystore.DTO.RecepcionDTO;
import com.krazystore.krazystore.Entity.PedidoCompraEntity;
import com.krazystore.krazystore.Entity.RecepcionEntity;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class RecepcionMapper implements Function<RecepcionDTO, RecepcionEntity> {
    @Override
    public RecepcionEntity apply(RecepcionDTO recepcionDTO) {
        PedidoCompraEntity pedido = new PedidoCompraEntity();
        pedido.setId(recepcionDTO.getIdPedido());
        return new RecepcionEntity(
                recepcionDTO.getId(),
                recepcionDTO.getFecha(),
                recepcionDTO.getEstado(),
                pedido
        );
    }
    
}
