/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.DetalleVentaEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;

/**
 *
 * @author HP
 */
public class DetalleEntregaDTO {
    private Long id;
    private DetallePedidoDTO detallePedido;
    private DetalleVentaEntity detalleVenta;
    private Integer cantidad;

    public DetalleEntregaDTO() {
    }

    public DetalleEntregaDTO(Long id, Long idDetVenta, ProductoEntity producto, 
            Integer cantFacturada, Integer cantidad) {
        this.id = id;
        this.detalleVenta = new DetalleVentaEntity(idDetVenta, producto, cantFacturada);
        this.cantidad = cantidad;
    }

    
    public DetalleEntregaDTO(Long id, 
            Long idDetPedido, Long idProducto, String nombre, 
            Integer cantSolicitada, Long cantFacturada, Long cantEntregada,
            Integer cantidad) {
        this.id = id;
        this.detallePedido = new DetallePedidoDTO(idDetPedido, idProducto, nombre, 
                cantSolicitada, cantFacturada, cantEntregada);
        this.cantidad = cantidad;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DetallePedidoDTO getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(DetallePedidoDTO detallePedido) {
        this.detallePedido = detallePedido;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public DetalleVentaEntity getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(DetalleVentaEntity detalleVenta) {
        this.detalleVenta = detalleVenta;
    }
    
    
    
}
