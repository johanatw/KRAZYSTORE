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
    private int numeroTimbrado;
    @Column
    private Date vigenciaInicio;
    @Column
    private Date vigenciaFin;
    @Column
    private int codEstablecimiento;
    @Column
    private int puntoExpedicion;
    @Column
    private int numeroInicio;
    @Column
    private int numeroFin;
    @Column
    private int cantHabilitada;
    @Column
    private int cantUtilizada;
    @Column
    private int ultimoRemitido;
    @Column
    private Date fecha_creacion;
    @Column
    private char estado;

    public TimbradoEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public int getNumeroTimbrado() {
        return numeroTimbrado;
    }

    public void setNumeroTimbrado(int numeroTimbrado) {
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

    public int getCodEstablecimiento() {
        return codEstablecimiento;
    }

    public void setCodEstablecimiento(int codEstablecimiento) {
        this.codEstablecimiento = codEstablecimiento;
    }

    public int getPuntoExpedicion() {
        return puntoExpedicion;
    }

    public void setPuntoExpedicion(int puntoExpedicion) {
        this.puntoExpedicion = puntoExpedicion;
    }

    public int getNumeroInicio() {
        return numeroInicio;
    }

    public void setNumeroInicio(int numeroInicio) {
        this.numeroInicio = numeroInicio;
    }

    public int getNumeroFin() {
        return numeroFin;
    }

    public void setNumeroFin(int numeroFin) {
        this.numeroFin = numeroFin;
    }

    public int getCantHabilitada() {
        return cantHabilitada;
    }

    public void setCantHabilitada(int cantHabilitada) {
        this.cantHabilitada = cantHabilitada;
    }

    public int getCantUtilizada() {
        return cantUtilizada;
    }

    public void setCantUtilizada(int cantUtilizada) {
        this.cantUtilizada = cantUtilizada;
    }

    public int getUltimoRemitido() {
        return ultimoRemitido;
    }

    public void setUltimoRemitido(int ultimoRemitido) {
        this.ultimoRemitido = ultimoRemitido;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }
    
    
}
