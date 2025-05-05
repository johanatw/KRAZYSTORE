/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import com.krazystore.krazystore.Entity.AplicacionPagoPedidoCompra;
import java.util.List;

/**
 *
 * @author HP
 */
public class AplicacionPagosPedidoCompraEvent {
    private List<AplicacionPagoPedidoCompra> pagosPedidoCompraAplicados;
    private final TipoEvento tipoEvento;

    public AplicacionPagosPedidoCompraEvent(List<AplicacionPagoPedidoCompra> pagosPedidoCompraAplicados, TipoEvento tipoEvento) {
        this.pagosPedidoCompraAplicados = pagosPedidoCompraAplicados;
        this.tipoEvento = tipoEvento;
    }

    public List<AplicacionPagoPedidoCompra> getPagosPedidoCompraAplicados() {
        return pagosPedidoCompraAplicados;
    }

    public void setPagosPedidoCompraAplicados(List<AplicacionPagoPedidoCompra> pagosPedidoCompraAplicados) {
        this.pagosPedidoCompraAplicados = pagosPedidoCompraAplicados;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }
    
    
}
