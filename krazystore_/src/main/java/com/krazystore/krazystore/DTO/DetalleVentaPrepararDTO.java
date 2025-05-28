/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

/**
 *
 * @author HP
 */
public class DetalleVentaPrepararDTO {
    private Long idDetVent;
    private ProductoDTO producto;
    private Integer cantidadFacturada;
    private Long cantidadPreparada;

    public DetalleVentaPrepararDTO() {
    }

    public DetalleVentaPrepararDTO(Long idDetVent, Long idProducto, String nombre, Integer cantidadFacturada, Long cantidadRecepcionada) {
        this.idDetVent = idDetVent;
        this.producto = new ProductoDTO(idProducto,nombre);
        this.cantidadFacturada = cantidadFacturada;
        this.cantidadPreparada = cantidadRecepcionada;
    }

    public Long getIdDetVent() {
        return idDetVent;
    }

    public void setIdDetVent(Long idDetVent) {
        this.idDetVent = idDetVent;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }

    public Integer getCantidadFacturada() {
        return cantidadFacturada;
    }

    public void setCantidadFacturada(Integer cantidadFacturada) {
        this.cantidadFacturada = cantidadFacturada;
    }

    public Long getCantidadPreparada() {
        return cantidadPreparada;
    }

    public void setCantidadPreparada(Long cantidadPreparada) {
        this.cantidadPreparada = cantidadPreparada;
    }

    
    
    
    
}
