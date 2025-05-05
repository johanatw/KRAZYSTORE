/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author HP
 */
public class PagoPedidoCompraReembolsoEvent {
    private Long idPagoPedidoCompra;
    private Integer montoReembolsado;
    private TipoEvento tipoEvento;

    public PagoPedidoCompraReembolsoEvent(Long idPagoPedidoCompra, Integer montoReembolsado, TipoEvento tipoEvento) {
        this.idPagoPedidoCompra = idPagoPedidoCompra;
        this.montoReembolsado = montoReembolsado;
        this.tipoEvento = tipoEvento;
    }

    public PagoPedidoCompraReembolsoEvent(Long idPagoPedidoCompra, TipoEvento tipoEvento) {
        this.idPagoPedidoCompra = idPagoPedidoCompra;
        this.tipoEvento = tipoEvento;
    }

    public Long getIdPagoPedidoCompra() {
        return idPagoPedidoCompra;
    }

    public void setIdPagoPedidoCompra(Long idPagoPedidoCompra) {
        this.idPagoPedidoCompra = idPagoPedidoCompra;
    }

    public Integer getMontoReembolsado() {
        return montoReembolsado;
    }

    public void setMontoReembolsado(Integer montoReembolsado) {
        this.montoReembolsado = montoReembolsado;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }
    
    
}
