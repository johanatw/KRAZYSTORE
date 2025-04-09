/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

/**
 *
 * @author HP
 */
public class DetalleRecepcionarFacturaDTO {
     private Long id;
    private Long idRecepcion;
    private DetallePedidoCompraDTO detallePedido;
    private int cantAceptada;
    private int cantRechazada;
    private int cantRecepcionado;
    private int cantFacturado;
    private int recepcionadoFactura;
    
    public DetalleRecepcionarFacturaDTO(Long idDetallePedido, Long idProducto, 
            String producto, Long recepcionadoFactura ) {
        this.detallePedido = new DetallePedidoCompraDTO(idDetallePedido, idProducto, producto);
        this.cantAceptada = 0;
        this.cantRechazada = 0;
        this.cantRecepcionado = 0;
        this.recepcionadoFactura = (int)(long)recepcionadoFactura;
    } 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdRecepcion() {
        return idRecepcion;
    }

    public void setIdRecepcion(Long idRecepcion) {
        this.idRecepcion = idRecepcion;
    }

    public DetallePedidoCompraDTO getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(DetallePedidoCompraDTO detallePedido) {
        this.detallePedido = detallePedido;
    }

    public int getCantAceptada() {
        return cantAceptada;
    }

    public void setCantAceptada(int cantAceptada) {
        this.cantAceptada = cantAceptada;
    }

    public int getCantRechazada() {
        return cantRechazada;
    }

    public void setCantRechazada(int cantRechazada) {
        this.cantRechazada = cantRechazada;
    }

    public int getCantRecepcionado() {
        return cantRecepcionado;
    }

    public void setCantRecepcionado(int cantRecepcionado) {
        this.cantRecepcionado = cantRecepcionado;
    }

    public int getCantFacturado() {
        return cantFacturado;
    }

    public void setCantFacturado(int cantFacturado) {
        this.cantFacturado = cantFacturado;
    }

    public int getRecepcionadoFactura() {
        return recepcionadoFactura;
    }

    public void setRecepcionadoFactura(int recepcionadoFactura) {
        this.recepcionadoFactura = recepcionadoFactura;
    }
    
    
}
