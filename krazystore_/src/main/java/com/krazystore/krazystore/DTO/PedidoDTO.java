/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import java.util.Date;

/**
 *
 * @author HP
 */
public class PedidoDTO {
     private long id;
      private Date fecha;
        private int total;
    private int costoEnvio =0;
    private String cliente;
    private String telefono;
    private String estadoPedido;
    private String estadoPago;
    private long cantPreVenta;
    private long totalItems;         

    public PedidoDTO() {
    }

    public PedidoDTO(long id, Date fecha, int total, String cliente, String telefono, String estadoPedido, String estadoPago, long cantPreVenta, long totalItems) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.cliente = cliente;
        this.telefono = telefono;
        this.estadoPedido = estadoPedido;
        this.estadoPago = estadoPago;
        this.cantPreVenta = cantPreVenta;
        this.totalItems = totalItems;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public long getCantPreVenta() {
        return cantPreVenta;
    }

    public void setCantPreVenta(long cantPreVenta) {
        this.cantPreVenta = cantPreVenta;
    }

    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(int costoEnvio) {
        this.costoEnvio = costoEnvio;
    }


    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    
}
