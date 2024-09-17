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
public class PRUEBADTO {
    private Long id;
    private String concepto;
    private Date fecha;
    private String pago;
    private Long monto;
    //private Long pedido;

    public PRUEBADTO(Long id, String concepto, Date fecha, Long monto) {
        this.id = id;
        this.concepto = concepto;
        this.fecha = fecha;
      
        this.monto = monto;
    }

    public PRUEBADTO(Long id, String concepto, Date fecha, String pago, Long monto) {
        this.id = id;
        this.concepto = concepto;
        this.fecha = fecha;
        this.pago = pago;
        this.monto = monto;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }
    
    

    public Long getMonto() {
        return monto;
    }

    public void setMonto(Long monto) {
        this.monto = monto;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

 
    
    
    
    
    
    
}
