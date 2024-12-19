/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.DetallePedidoCompra;
import com.krazystore.krazystore.Entity.PedidoCompraEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 *
 * @author HP
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoCompraCreationDTO {
    private PedidoCompraEntity pedido;
    private List<DetallePedidoCompra> detalle;

    public PedidoCompraEntity getPedido() {
        return pedido;
    }

    public void setPedido(PedidoCompraEntity pedido) {
        this.pedido = pedido;
    }

    public List<DetallePedidoCompra> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetallePedidoCompra> detalle) {
        this.detalle = detalle;
    }
    
    
}
