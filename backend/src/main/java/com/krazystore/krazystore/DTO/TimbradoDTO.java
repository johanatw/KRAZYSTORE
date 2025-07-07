/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.TimbradoEntity;

/**
 *
 * @author HP
 */
public class TimbradoDTO {
    private TimbradoEntity timbrado;
    private String nroFactura;

    public TimbradoDTO() {
    }

    public TimbradoDTO(TimbradoEntity timbrado, String nroFactura) {
        this.timbrado = timbrado;
        this.nroFactura = nroFactura;
    }

    public TimbradoDTO(TimbradoEntity timbrado) {
        this.timbrado = timbrado;
    }

    public TimbradoEntity getTimbrado() {
        return timbrado;
    }

    public void setTimbrado(TimbradoEntity timbrado) {
        this.timbrado = timbrado;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }
    
    
}
