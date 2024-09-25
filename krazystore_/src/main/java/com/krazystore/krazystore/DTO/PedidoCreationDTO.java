/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.DetallePedidoEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public class PedidoCreationDTO {
    private PedidoEntity pedido;
    private List<DetallePedidoEntity> detalle;

    public PedidoCreationDTO() {
    }

    public PedidoEntity getPedido() {
        return pedido;
    }

    public void setPedido(PedidoEntity pedido) {
        this.pedido = pedido;
    }

    public List<DetallePedidoEntity> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetallePedidoEntity> detalle) {
        this.detalle = detalle;
    }
    
    
}
