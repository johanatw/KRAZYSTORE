/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author HP
 */
public class PedidoEvent {
    private Long pedidoId;
    private final TipoEvento tipoEvento;

    public PedidoEvent(Long pedidoId, TipoEvento tipoEvento) {
        this.pedidoId = pedidoId;
        this.tipoEvento = tipoEvento;
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
    
    
}
