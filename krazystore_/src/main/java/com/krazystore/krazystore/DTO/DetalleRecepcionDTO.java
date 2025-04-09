/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.ProductoEntity;

/**
 *
 * @author HP
 */

public class DetalleRecepcionDTO {
    private Long id;
    private Long idRecepcion;
    private DetallePedidoCompraDTO detallePedido;
    private int cantAceptada;
    private int cantRechazada;
    private int cantRecepcionado;
    private int cantFacturado;
    private int recepcionadoFactura;
    

    public DetalleRecepcionDTO() {
    }

    public DetalleRecepcionDTO(Long idDetallePedido, Long idProducto, 
            String producto, int cantAceptada, int cantRechazada, int cantRecepcionado) {
        this.detallePedido = new DetallePedidoCompraDTO(idDetallePedido, idProducto, producto);
        this.cantAceptada = cantAceptada;
        this.cantRechazada = cantRechazada;
        this.cantRecepcionado = cantRecepcionado;
    }

    
    public DetalleRecepcionDTO(Long id, Long idRecepcion, Long idDetallePedido, Long idProducto, 
            String producto, int costo, int cantSolicitado, int cantAceptada, int cantRechazada, 
            int cantRecepcionado, Long cantTotalAceptada, Long cantTotalFacturada, Long totalRecepcionado) {
        this.id = id;
        this.idRecepcion = idRecepcion;
        this.detallePedido = new DetallePedidoCompraDTO(idDetallePedido, idProducto, producto,
        cantSolicitado, costo, cantTotalAceptada, cantTotalFacturada, totalRecepcionado);
        this.cantAceptada = cantAceptada;
        this.cantRechazada = cantRechazada;
        this.cantRecepcionado = cantRecepcionado;
    } 
    
    public DetalleRecepcionDTO(Long idDetallePedido, Long idProducto, 
            String producto, long cantFacturado, Long recepcionadoFactura ) {
        this.detallePedido = new DetallePedidoCompraDTO(idDetallePedido, idProducto, producto);
        this.cantAceptada = 0;
        this.cantRechazada = 0;
        this.cantRecepcionado = 0;
        this.cantFacturado = (int)(long)cantFacturado;
        this.recepcionadoFactura = (int)(long)recepcionadoFactura;
    } 
    
    public DetalleRecepcionDTO(Long idDetallePedido, Long idProducto, 
            String producto, Integer costoCompra, Long cantAceptada, Long cantRechazada, Long cantRecepcionado ) {
        this.detallePedido = new DetallePedidoCompraDTO(idDetallePedido, idProducto, producto, costoCompra);
        this.cantAceptada = (int)(long)cantAceptada;
        this.cantRechazada = (int)(long)cantRechazada;
        this.cantRecepcionado = (int)(long)cantRecepcionado;
        this.cantFacturado = (int)(long)cantFacturado;
    } 

    public DetallePedidoCompraDTO getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(DetallePedidoCompraDTO detallePedido) {
        this.detallePedido = detallePedido;
    }
    

    
    public Long getIdRecepcion() {
        return idRecepcion;
    }

    public void setIdRecepcion(Long idRecepcion) {
        this.idRecepcion = idRecepcion;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCantRecepcionado() {
        return cantRecepcionado;
    }

    public void setCantRecepcionado(int cantRecepcionado) {
        this.cantRecepcionado = cantRecepcionado;
    }

    public int getRecepcionadoFactura() {
        return recepcionadoFactura;
    }

    public void setRecepcionadoFactura(int recepcionadoFactura) {
        this.recepcionadoFactura = recepcionadoFactura;
    }

    public int getCantFacturado() {
        return cantFacturado;
    }

    public void setCantFacturado(int cantFacturado) {
        this.cantFacturado = cantFacturado;
    }

    
}
