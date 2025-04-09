/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Entity;

import com.krazystore.krazystore.DTO.ProductoDTO;
import jakarta.persistence.Column;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author HP
 */


@Entity
@Table(name = "productos")
public class ProductoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull
    private String nombre;
    @Column
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "id_sub_categoria")
    private SubCategoriaEntity subCategoria;
    @ManyToOne
    @JoinColumn(name = "id_tipo_iva")
    private IvaEntity tipoIva;
    @Column
    private Boolean estado;
    @Column(name = "bajo_demanda")
    private Boolean bajoDemanda = false;
    @Column(name = "limite_bajo_demanda")
    private Integer cantLimBajoDemanda;
    @Column
    private int cantStock = 0;
    @Column
    private int cantDisponible = 0;
    @Column
    private int cantReservada = 0;

    
    public ProductoEntity() {
        
    }

    public ProductoEntity(Long id, String nombre, SubCategoriaEntity subCategoria, Boolean estado, Boolean preVenta, Integer cantPreVenta, int cantStock, int cantDisponible, int cantReservada) {
        this.id = id;
        this.nombre = nombre;
        this.subCategoria = subCategoria;
        this.estado = estado;
        this.bajoDemanda = preVenta;
        this.cantLimBajoDemanda = cantPreVenta;
        this.cantStock = cantStock;
        this.cantDisponible = cantDisponible;
        this.cantReservada = cantReservada;
    }

   public ProductoEntity(Long id, String nombre, SubCategoriaEntity subCategoria, IvaEntity tipoIva,Boolean bajoDemanda, Integer cantLimBajoDemanda) {
        this.id = id;
        this.nombre = nombre;
        this.subCategoria = subCategoria;
        this.tipoIva = tipoIva;
        this.bajoDemanda = bajoDemanda;
        this.cantLimBajoDemanda = cantLimBajoDemanda;
    }

    public ProductoEntity(Long id, String nombre, String descripcion, SubCategoriaEntity subCategoria, IvaEntity tipoIva, Boolean bajoDemanda, Integer cantLimBajoDemanda) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.subCategoria = subCategoria;
        this.tipoIva = tipoIva;
        this.bajoDemanda = bajoDemanda;
        this.cantLimBajoDemanda = cantLimBajoDemanda;
    }

    public ProductoEntity(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

   
    
    public int getCantStock() {
        return cantStock;
    }

    public void setCantStock(int cantStock) {
        this.cantStock = cantStock;
    }

    public int getCantDisponible() {
        return cantDisponible;
    }

    public void setCantDisponible(int cantDisponible) {
        this.cantDisponible = cantDisponible;
    }

    public int getCantReservada() {
        return cantReservada;
    }

    public void setCantReservada(int cantReservada) {
        this.cantReservada = cantReservada;
    }

    
    
    

    public Boolean getBajoDemanda() {
        return bajoDemanda;
    }

    public void setBajoDemanda(Boolean bajoDemanda) {
        this.bajoDemanda = bajoDemanda;
    }

    public Integer getCantLimBajoDemanda() {
        return cantLimBajoDemanda;
    }

    public void setCantLimBajoDemanda(Integer cantLimBajoDemanda) {
        this.cantLimBajoDemanda = cantLimBajoDemanda;
    }
        

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public SubCategoriaEntity getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(SubCategoriaEntity subCategoria) {
        this.subCategoria = subCategoria;
    }



    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public IvaEntity getTipoIva() {
        return tipoIva;
    }

    public void setTipoIva(IvaEntity tipoIva) {
        this.tipoIva = tipoIva;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
}
