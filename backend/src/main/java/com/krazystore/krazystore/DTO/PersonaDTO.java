/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.DireccionEntity;
import com.krazystore.krazystore.Entity.PersonaEntity;
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
    private TipoDocEntity tipoDoc;
    private String nroDoc;
    private String telefono;
    private DireccionEntity direccion;

    public PersonaDTO() {
    }

    public PersonaDTO(Long id, String nombre, String apellido, String nombreCompleto, String direccion, TipoDocEntity tipoDoc, String nroDoc, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nombreCompleto = apellido != null ? (nombre + " " + apellido) : nombre;
        this.tipoDoc = tipoDoc;
        this.nroDoc = nroDoc;
        this.telefono = telefono;
    }

    public PersonaDTO(PersonaEntity persona, DireccionEntity direccion) {
        this.id = persona.getId();
        this.nombre = persona.getNombre();
        this.apellido = persona.getApellido();
        this.nombreCompleto = this.apellido != null ? (this.nombre + " " + this.apellido) : this.nombre;
        this.tipoDoc = persona.getTipoDoc();
        this.nroDoc = persona.getNroDoc();
        this.telefono = persona.getTelefono();
        this.direccion = direccion;
    }

    public PersonaDTO(PersonaEntity persona) {
        this.id = persona.getId();
        this.nombre = persona.getNombre();
        this.apellido = persona.getApellido();
        this.nombreCompleto = this.apellido != null ? (this.nombre + " " + this.apellido) : this.nombre;
        this.tipoDoc = persona.getTipoDoc();
        this.nroDoc = persona.getNroDoc();
        this.telefono = persona.getTelefono();
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

    public DireccionEntity getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionEntity direccion) {
        this.direccion = direccion;
    }

   
    
    
    
}

