/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.AplicacionAnticipo;
import com.krazystore.krazystore.Entity.AplicacionPagoPedidoCompra;
import com.krazystore.krazystore.Entity.FormaCobroEntity;
import com.krazystore.krazystore.Entity.FormaPagoEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public class MovimientoCreationDTO {
    private MovimientoEntity movimiento;
    private List<FormaPagoEntity> pagos;
    private List<FormaCobroEntity> cobros;
    private List<AplicacionAnticipo> anticiposAplicados;
    private List<AplicacionPagoPedidoCompra> pagosPedidoCompraAplicados;

    public MovimientoCreationDTO() {
    }

   
   

    public MovimientoEntity getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(MovimientoEntity movimiento) {
        this.movimiento = movimiento;
    }

    public List<FormaPagoEntity> getPagos() {
        return pagos;
    }

    public void setPagos(List<FormaPagoEntity> pagos) {
        this.pagos = pagos;
    }

    public List<FormaCobroEntity> getCobros() {
        return cobros;
    }

    public void setCobros(List<FormaCobroEntity> cobros) {
        this.cobros = cobros;
    }

    public List<AplicacionAnticipo> getAnticiposAplicados() {
        return anticiposAplicados;
    }

    public void setAnticiposAplicados(List<AplicacionAnticipo> anticiposAplicados) {
        this.anticiposAplicados = anticiposAplicados;
    }

    public List<AplicacionPagoPedidoCompra> getPagosPedidoCompraAplicados() {
        return pagosPedidoCompraAplicados;
    }

    public void setPagosPedidoCompraAplicados(List<AplicacionPagoPedidoCompra> pagosPedidoCompraAplicados) {
        this.pagosPedidoCompraAplicados = pagosPedidoCompraAplicados;
    }

    
   


    
    
}
