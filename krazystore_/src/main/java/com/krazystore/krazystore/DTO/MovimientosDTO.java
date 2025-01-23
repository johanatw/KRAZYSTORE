/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import java.util.Date;
import java.util.List;

/**
 *
 * @author HP
 */
public class MovimientosDTO {
    private  Long id;
    private Date fecha;
    private String concepto;
    private String formaPago;
    private Long total;
    private String factura;
    private char estado;
    private char tipo;

    public MovimientosDTO() {
    }

    public MovimientosDTO(Long id, Date fecha, String concepto, String formaPago, Long total, String factura, char estado, char tipo) {
        this.id = id;
        this.fecha = fecha;
        this.concepto = concepto;
        this.formaPago = formaPago;
        this.total = total;
        this.factura = factura;
        this.estado = estado;
        this.tipo = tipo;
    }
    
    

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    

    

    public MovimientosDTO(Long id) {
        this.id = id;

       
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

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }


    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    
    
    
    
    
}
