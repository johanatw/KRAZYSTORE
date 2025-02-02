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

    public DetalleRecepcionDTO() {
    }

    public DetalleRecepcionDTO(Long id, Long idRecepcion, Long idDetallePedido, Long idProducto, 
            String producto, int costo, int cantSolicitado, int cantAceptada, int cantRechazada, int cantRecepcionado, Long cantTotalRecepcionado) {
        this.id = id;
        this.idRecepcion = idRecepcion;
        this.detallePedido = new DetallePedidoCompraDTO(idDetallePedido, idProducto, producto,
        cantSolicitado, costo, cantTotalRecepcionado);
        this.cantAceptada = cantAceptada;
        this.cantRechazada = cantRechazada;
        this.cantRecepcionado = cantRecepcionado;
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

}
