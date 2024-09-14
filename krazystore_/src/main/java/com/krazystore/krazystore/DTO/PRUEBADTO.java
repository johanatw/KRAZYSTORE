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
   
    private int monto;
    //private Long pedido;

    public PRUEBADTO(Long id, String concepto, Date fecha, int monto) {
        this.id = id;
        this.concepto = concepto;
        this.fecha = fecha;
      
        this.monto = monto;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
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
