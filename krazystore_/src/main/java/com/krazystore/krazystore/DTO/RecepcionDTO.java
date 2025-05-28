/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.CompraEntity;
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
    private Character estado;
    private ProveedorEntity proveedor;
    private CompraEntity compra;
    private String idsPedido;
    

    public RecepcionDTO() {
    }

    public RecepcionDTO(Long id, Date fecha, Character estado) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
    }
    
    public RecepcionDTO(Long id, Date fecha, Long idPedido, Character estado) {
        this.id = id;
        this.fecha = fecha;
        this.idPedido = idPedido;
        this.estado = estado;
        
    }

    public RecepcionDTO(Long id, Date fecha, String idsPedido, Character estado) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
        this.idsPedido = idsPedido;
    }

    
    public RecepcionDTO(Long id, Date fecha, Long idPedido, Character estado, CompraEntity compra) {
        this.id = id;
        this.fecha = fecha;
        this.idPedido = idPedido;
        this.estado = estado;
        this.compra = compra;
    }

    
    

    
    public RecepcionDTO(Long id, Date fecha, Long idPedido, ProveedorEntity proveedor) {
        this.id = id;
        this.fecha = fecha;
        this.idPedido = idPedido;
        this.proveedor = proveedor;
    }

    public RecepcionDTO(Long id, Date fecha, Long idPedido, Character estado, ProveedorEntity proveedor) {
        this.id = id;
        this.fecha = fecha;
        this.idPedido = idPedido;
        this.estado = estado;
        this.proveedor = proveedor;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
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

    public CompraEntity getCompra() {
        return compra;
    }

    public void setCompra(CompraEntity compra) {
        this.compra = compra;
    }

    public String getIdsPedido() {
        return idsPedido;
    }

    public void setIdsPedido(String idsPedido) {
        this.idsPedido = idsPedido;
    }
   
    
    
}
