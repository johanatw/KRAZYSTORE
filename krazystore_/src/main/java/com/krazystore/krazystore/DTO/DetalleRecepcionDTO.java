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
    private Long idDetallePedido;
    private Long idProducto;
    private String producto;
    private int costo;
    private int cantSolicitado;
    private int cantAceptada;
    private int cantRechazada;
    private int cantTotalRecepcionado;
    private int cantRecepcionado;

    public DetalleRecepcionDTO() {
    }

    public DetalleRecepcionDTO(Long id, Long idRecepcion, Long idDetallePedido, Long idProducto, 
            String producto, int costo, int cantSolicitado, int cantAceptada, int cantRechazada, int cantRecepcionado, Long cantTotalRecepcionado) {
        this.id = id;
        this.idRecepcion = idRecepcion;
        this.idDetallePedido = idDetallePedido;
        this.idProducto = idProducto;
        this.producto = producto;
        this.costo = costo;
        this.cantSolicitado = cantSolicitado;
        this.cantAceptada = cantAceptada;
        this.cantRechazada = cantRechazada;
        this.cantTotalRecepcionado = (int)(long)cantTotalRecepcionado;
        this.cantRecepcionado = cantRecepcionado;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
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

    public Long getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(Long idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCantSolicitado() {
        return cantSolicitado;
    }

    public void setCantSolicitado(int cantSolicitado) {
        this.cantSolicitado = cantSolicitado;
    }

    public int getCantRecepcionado() {
        return cantRecepcionado;
    }

    public void setCantRecepcionado(int cantRecepcionado) {
        this.cantRecepcionado = cantRecepcionado;
    }

    public int getCantTotalRecepcionado() {
        return cantTotalRecepcionado;
    }

    public void setCantTotalRecepcionado(int cantTotalRecepcionado) {
        this.cantTotalRecepcionado = cantTotalRecepcionado;
    }

    

    
}
