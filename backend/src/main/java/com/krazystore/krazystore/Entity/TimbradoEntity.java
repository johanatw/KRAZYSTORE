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
import jakarta.persistence.Table;
import java.util.Date;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "timbrados")
public class TimbradoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column
    private Integer numeroTimbrado;
    @Column
    private Date vigenciaInicio;
    @Column
    private Date vigenciaFin;
    @Column
    private Integer codEstablecimiento;
    @Column
    private Integer puntoExpedicion;
    @Column
    private Integer numeroInicio;
    @Column
    private Integer numeroFin;
    @Column
    private Integer ultimoEmitido = 0;
    @Column
    private Date fecha_creacion;
    @Column
    private Boolean activo = true;

    public TimbradoEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroTimbrado() {
        return numeroTimbrado;
    }

    public void setNumeroTimbrado(Integer numeroTimbrado) {
        this.numeroTimbrado = numeroTimbrado;
    }

    public Date getVigenciaInicio() {
        return vigenciaInicio;
    }

    public void setVigenciaInicio(Date vigenciaInicio) {
        this.vigenciaInicio = vigenciaInicio;
    }

    public Date getVigenciaFin() {
        return vigenciaFin;
    }

    public void setVigenciaFin(Date vigenciaFin) {
        this.vigenciaFin = vigenciaFin;
    }

    public Integer getCodEstablecimiento() {
        return codEstablecimiento;
    }

    public void setCodEstablecimiento(Integer codEstablecimiento) {
        this.codEstablecimiento = codEstablecimiento;
    }

    public Integer getPuntoExpedicion() {
        return puntoExpedicion;
    }

    public void setPuntoExpedicion(Integer puntoExpedicion) {
        this.puntoExpedicion = puntoExpedicion;
    }

    public Integer getNumeroInicio() {
        return numeroInicio;
    }

    public void setNumeroInicio(Integer numeroInicio) {
        this.numeroInicio = numeroInicio;
    }

    public Integer getNumeroFin() {
        return numeroFin;
    }

    public void setNumeroFin(Integer numeroFin) {
        this.numeroFin = numeroFin;
    }

    public Integer getUltimoEmitido() {
        return ultimoEmitido;
    }

    public void setUltimoEmitido(Integer ultimoEmitido) {
        this.ultimoEmitido = ultimoEmitido;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    

    
    
}
