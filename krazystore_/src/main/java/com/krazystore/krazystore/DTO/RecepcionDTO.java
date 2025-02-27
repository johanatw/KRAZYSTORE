/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.ProveedorEntity;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HP
 */
public class RecepcionDTO {
    private Long id;
    private Date fecha;
    private Long idPedido;
    private char estado;
    private ProveedorEntity proveedor;

    public RecepcionDTO() {
    }

    public RecepcionDTO(Long id, Date fecha, Long idPedido) {
        this.id = id;
        this.fecha = fecha;
        this.idPedido = idPedido;
    }

    public RecepcionDTO(Long id, Date fecha, Long idPedido, ProveedorEntity proveedor) {
        this.id = id;
        this.fecha = fecha;
        this.idPedido = idPedido;
        this.proveedor = proveedor;
    }

    public RecepcionDTO(Long id, Date fecha, Long idPedido, char estado, ProveedorEntity proveedor) {
        this.id = id;
        this.fecha = fecha;
        this.idPedido = idPedido;
        this.estado = estado;
        this.proveedor = proveedor;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    
    public RecepcionDTO(Long id, Date fecha) {
        this.id = id;
        this.fecha = fecha;
    }

    public ProveedorEntity getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorEntity proveedor) {
        this.proveedor = proveedor;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }
   
    
    
}
