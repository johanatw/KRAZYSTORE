/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "ciudades")
public class CiudadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "id_departamento")
    private DepartamentoEntity departamento;

    public CiudadEntity() {
    }

    public CiudadEntity(Long id, String descripcion, DepartamentoEntity departamento) {
        this.id = id;
        this.descripcion = descripcion;
        this.departamento = departamento;
    }

    public DepartamentoEntity getDepartamento() {
        return departamento;
    }

    public void setDepartamento(DepartamentoEntity departamento) {
        this.departamento = departamento;
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
    
    
}
