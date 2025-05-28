/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import com.krazystore.krazystore.Entity.CompraEntity;
import com.krazystore.krazystore.Entity.VentaEntity;

/**
 *
 * @author HP
 */
public abstract class FacturaEvent {
    private final TipoEvento tipoEvento;
    private VentaEntity nuevaVenta;
    private CompraEntity nuevaCompra;
    private Long idPedido;

    public FacturaEvent(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public FacturaEvent(TipoEvento tipoEvento, VentaEntity nuevaVenta) {
        this.tipoEvento = tipoEvento;
        this.nuevaVenta = nuevaVenta;
    }

    public FacturaEvent(TipoEvento tipoEvento, CompraEntity nuevaCompra) {
        this.tipoEvento = tipoEvento;
        this.nuevaCompra = nuevaCompra;
    }

    public FacturaEvent(TipoEvento tipoEvento, VentaEntity nuevaVenta, Long idPedido) {
        this.tipoEvento = tipoEvento;
        this.nuevaVenta = nuevaVenta;
        this.idPedido = idPedido;
    }

    
    public VentaEntity getNuevaVenta() {
        return nuevaVenta;
    }

    public void setNuevaVenta(VentaEntity nuevaVenta) {
        this.nuevaVenta = nuevaVenta;
    }

    public CompraEntity getNuevaCompra() {
        return nuevaCompra;
    }

    public void setNuevaCompra(CompraEntity nuevaCompra) {
        this.nuevaCompra = nuevaCompra;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    
    
    
}
