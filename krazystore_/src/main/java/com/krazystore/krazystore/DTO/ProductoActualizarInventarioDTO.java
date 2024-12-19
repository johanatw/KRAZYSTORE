/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

/**
 *
 * @author HP
 */
public class ProductoActualizarInventarioDTO {
    private Long idProductos;
    private int cant;
    private char accion;

    public ProductoActualizarInventarioDTO() {
    }

    public Long getIdProductos() {
        return idProductos;
    }

    public void setIdProductos(Long idProductos) {
        this.idProductos = idProductos;
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }

    public char getAccion() {
        return accion;
    }

    public void setAccion(char accion) {
        this.accion = accion;
    }
    
    
}
