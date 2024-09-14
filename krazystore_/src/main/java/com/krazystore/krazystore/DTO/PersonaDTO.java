/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.TipoDocEntity;

/**
 *
 * @author HP
 */
public class PersonaDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String nombreCompleto;
    private String direccion;
    private TipoDocEntity tipoDoc;
    private String nroDoc;
    private String telefono;

    public PersonaDTO() {
    }

    public PersonaDTO(Long id, String nombre, String apellido, String nombreCompleto, String direccion, TipoDocEntity tipoDoc, String nroDoc, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nombreCompleto = nombreCompleto;
        this.direccion = direccion;
        this.tipoDoc = tipoDoc;
        this.nroDoc = nroDoc;
        this.telefono = telefono;
    }

    

    


    public Long getIdPersona() {
        return id;
    }

    public void setIdPersona(Long idPersona) {
        this.id = idPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

   

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public TipoDocEntity getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TipoDocEntity tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

   
    
    
    
}

