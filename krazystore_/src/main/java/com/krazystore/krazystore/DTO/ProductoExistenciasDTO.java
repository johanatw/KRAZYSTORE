/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import Utils.TipoAjusteExistencia;

/**
 *
 * @author HP
 */
public class ProductoExistenciasDTO {
    private Long idProducto;
    private Integer cantidad;
    private TipoAjusteExistencia accion;

    public ProductoExistenciasDTO() {
    }

    public ProductoExistenciasDTO(Long idProducto, Integer cantidad, TipoAjusteExistencia accion) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.accion = accion;
    }
    
    

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public TipoAjusteExistencia getAccion() {
        return accion;
    }

    public void setAccion(TipoAjusteExistencia accion) {
        this.accion = accion;
    }
    
    
}
