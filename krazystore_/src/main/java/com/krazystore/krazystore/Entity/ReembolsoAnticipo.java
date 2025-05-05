/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "reembolsos_anticipo")
public class ReembolsoAnticipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column
    private int monto;
    @Column
    private Date fecha;
    @Column
    private String motivo;
    @ManyToOne
    @JoinColumn(name = "id_anticipo")
    private AnticipoEntity anticipo;


    public ReembolsoAnticipo() {
    }

    public ReembolsoAnticipo(Long id, int monto, Date fecha, String motivo, AnticipoEntity anticipo) {
        this.id = id;
        this.monto = monto;
        this.fecha = fecha;
        this.motivo = motivo;
  
        this.anticipo = anticipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    

    public AnticipoEntity getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(AnticipoEntity anticipo) {
        this.anticipo = anticipo;
    }
   
    
}
