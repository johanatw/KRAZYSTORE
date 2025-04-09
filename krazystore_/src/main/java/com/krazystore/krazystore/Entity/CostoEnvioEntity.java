/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Entity;

import jakarta.persistence.*;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "costos_envio")
public class CostoEnvioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "id_envio")
    private EmpresaTransporte envio;
    @ManyToOne
    @JoinColumn(name = "id_ciudad")
    private CiudadEntity ciudad;
    @Column
    private int costo;

    public CostoEnvioEntity() {
    }

    public CostoEnvioEntity(Long id, EmpresaTransporte envio, CiudadEntity ciudad, int costo) {
        this.id = id;
        this.envio = envio;
        this.ciudad = ciudad;
        this.costo = costo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmpresaTransporte getEnvio() {
        return envio;
    }

    public void setEnvio(EmpresaTransporte envio) {
        this.envio = envio;
    }

    public CiudadEntity getCiudad() {
        return ciudad;
    }

    public void setCiudad(CiudadEntity ciudad) {
        this.ciudad = ciudad;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }
    
    
    
}
