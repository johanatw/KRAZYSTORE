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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 *
 * @author HP
 */
@Entity
@Table(name="proveedores")
public class ProveedorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String descripcion;
    @Column
    private String ruc;
    @Column
    private String telefono;
    @Column
    private String correo;
    @ManyToOne
    @JoinColumn(name = "id_tipo")
    private TipoProveedor tipo;
    @Column
    private String direccion;
    @ManyToOne
    @JoinColumn(name = "id_ciudad")
    private CiudadEntity ciudad;
    @Column
    private Boolean activo = true;

    public ProveedorEntity() {
    }

    public ProveedorEntity(Long id, String descripcion, String ruc, String telefono, String correo, TipoProveedor tipo, String direccion, CiudadEntity ciudad) {
        this.id = id;
        this.descripcion = descripcion;
        this.ruc = ruc;
        this.telefono = telefono;
        this.correo = correo;
        this.tipo = tipo;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public TipoProveedor getTipo() {
        return tipo;
    }

    public void setTipo(TipoProveedor tipo) {
        this.tipo = tipo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public CiudadEntity getCiudad() {
        return ciudad;
    }

    public void setCiudad(CiudadEntity ciudad) {
        this.ciudad = ciudad;
    }
         
    
}
