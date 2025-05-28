/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.util.List;

/**
 *
 * @author HP
 */
public class PedidoCompraEvent {
    private Long pedidoId;
    private final TipoEvento tipoEvento;
    private List<Long> idsPedidos;

    public PedidoCompraEvent(Long pedidoId, TipoEvento tipoEvento) {
        this.pedidoId = pedidoId;
        this.tipoEvento = tipoEvento;
    }

    public PedidoCompraEvent(TipoEvento tipoEvento, List<Long> idsPedidos) {
        this.tipoEvento = tipoEvento;
        this.idsPedidos = idsPedidos;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public List<Long> getIdsPedidos() {
        return idsPedidos;
    }

    public void setIdsPedidos(List<Long> idsPedidos) {
        this.idsPedidos = idsPedidos;
    }
    
}
