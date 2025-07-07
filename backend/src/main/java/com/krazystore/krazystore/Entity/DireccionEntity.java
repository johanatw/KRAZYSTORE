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
@Table(name = "direcciones")
public class DireccionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column
    private String direccion;
    @Column
    private String calle1;
    @Column
    private String calle2;
    @Column
    private String calle3;
    @Column
    private String nroCasa;
    @Column
    private char tipo;
    @Column
    private Float lat;
    @Column
    private Float lng;
    @ManyToOne
    @JoinColumn(name = "id_ciudad")
    private CiudadEntity ciudad;
    @ManyToOne
    @JoinColumn(name = "id_persona")
    private PersonaEntity persona;

    public DireccionEntity() {
    }
    
    

    public CiudadEntity getCiudad() {
        return ciudad;
    }

    public String getCalle3() {
        return calle3;
    }

    public void setCalle3(String calle3) {
        this.calle3 = calle3;
    }

    
    public void setCiudad(CiudadEntity ciudad) {
        this.ciudad = ciudad;
    }
    

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

   

    
    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }
    

    public String getCalle1() {
        return calle1;
    }

    public void setCalle1(String calle1) {
        this.calle1 = calle1;
    }

    public String getCalle2() {
        return calle2;
    }

    public void setCalle2(String calle2) {
        this.calle2 = calle2;
    }
    
    


    

    public String getNroCasa() {
        return nroCasa;
    }

    public void setNroCasa(String nroCasa) {
        this.nroCasa = nroCasa;
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

    public PersonaEntity getPersona() {
        return persona;
    }

    public void setPersona(PersonaEntity persona) {
        this.persona = persona;
    }
    
    
}
