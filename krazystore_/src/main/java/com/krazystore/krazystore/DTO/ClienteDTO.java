/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.DireccionEntity;
import com.krazystore.krazystore.Entity.PersonaEntity;
import com.krazystore.krazystore.Entity.TipoDocEntity;

/**
 *
 * @author HP
 */
public class ClienteDTO {
    private long id;
    private String nombre;
    private String apellido;
    private String nombreCompleto;
    private TipoDocEntity tipoDoc;
    private String nroDoc;
    private String telefono;
    private DireccionEntity direccion;

    public ClienteDTO() {
    }

    public ClienteDTO(Long id, PersonaEntity persona, DireccionEntity direccion) {
        this.id = id;
        this.nombre = persona.getNombre();
        this.apellido = persona.getApellido();
        this.nombreCompleto = this.apellido != null ? (this.nombre + " " + this.apellido) : this.nombre;
        this.tipoDoc = persona.getTipoDoc();
        this.nroDoc = persona.getNroDoc();
        this.telefono = persona.getTelefono();
        this.direccion = direccion;
    }

    public ClienteDTO(Long id, PersonaEntity persona) {
        this.id = id;
        this.nombre = persona.getNombre();
        this.apellido = persona.getApellido();
        this.nombreCompleto = this.apellido != null ? (this.nombre + " " + this.apellido) : this.nombre;
        this.tipoDoc = persona.getTipoDoc();
        this.nroDoc = persona.getNroDoc();
        this.telefono = persona.getTelefono();
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public DireccionEntity getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionEntity direccion) {
        this.direccion = direccion;
    }



    
    
}
