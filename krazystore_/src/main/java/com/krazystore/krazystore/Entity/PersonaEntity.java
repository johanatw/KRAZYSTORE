/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Entity;

import jakarta.persistence.*;
import jakarta.annotation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



/**
 *
 * @author HP
 */

@Data
@Entity
@Table(name = "personas", uniqueConstraints = { @UniqueConstraint(columnNames = { "email"}),
                   @UniqueConstraint(columnNames = { "telefono"}),
                    @UniqueConstraint(columnNames = { "nroDoc"})} )
public class PersonaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @NotNull(message = "Nombre requerido!")
    private String nombre;
    @Column
    private String apellido;
    @Column
    private String direccion;
    @Column
    @Email
    private String email;
    @ManyToOne
    @JoinColumn(name = "id_tipo_doc")
    private TipoDocEntity tipoDoc;
    @Column
    private String nroDoc;
    @Column
    private String telefono;

    public PersonaEntity() {
    }

    public PersonaEntity(long id, String nombre, String apellido, String direccion, String email, TipoDocEntity tipo_doc, String nro_doc, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.email = email;
        this.tipoDoc = tipo_doc;
        this.nroDoc = nro_doc;
        this.telefono = telefono;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   
    
    
    
}
