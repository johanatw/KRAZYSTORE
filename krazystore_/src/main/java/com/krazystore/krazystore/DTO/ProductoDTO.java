/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.CategoriaEntity;
import com.krazystore.krazystore.Entity.IvaEntity;
import com.krazystore.krazystore.Entity.SubCategoriaEntity;

/**
 *
 * @author HP
 */
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private SubCategoriaEntity subCategoria;
    private int precio;
    private int costo;
    private Boolean estado;
    private Boolean bajoDemanda;
    private int cantLimBajoDemanda;
    private int cantStock;
    private int cantDisponible;
    private int cantReservada;
    private IvaEntity tipoIva;
    private Boolean esServicio;

    public ProductoDTO() {
    }

    public ProductoDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public ProductoDTO(Long id, String nombre, int precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public ProductoDTO(Long id, String nombre, SubCategoriaEntity categoria) {
        this.id = id;
        this.nombre = nombre;
        this.subCategoria = categoria;
    }

    
    public ProductoDTO(Long id, String nombre, int precio, int costo) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.costo = costo;
    }

    public ProductoDTO(Long id, String nombre, Long costo, IvaEntity tipoIva) {
        this.id = id;
        this.nombre = nombre;
        this.costo = (int)(long)costo;
        this.tipoIva = tipoIva;
    }
    
    public ProductoDTO(Long id, String nombre, int cantStock, IvaEntity tipoIva) {
        this.id = id;
        this.nombre = nombre;
        this.cantStock = cantStock;
        this.tipoIva = tipoIva;
    }
    
    

    public ProductoDTO(Long id, String nombre, SubCategoriaEntity categoria, Long precio, 
            Long costo, Boolean bajoDemanda, int cantLimBajoDemanda, int cantStock, int cantDisponible, int cantReservada) {
        this.id = id;
        this.nombre = nombre;
        this.subCategoria = categoria;
        this.precio = (int)(long)precio;
        this.costo = (int)(long)costo;
        this.bajoDemanda = bajoDemanda;
        this.cantLimBajoDemanda = cantLimBajoDemanda;
        this.cantStock = cantStock;
        this.cantDisponible = cantDisponible;
        this.cantReservada = cantReservada;
    }
    
    
    
    public ProductoDTO(Long id, String nombre, SubCategoriaEntity categoria, Long precio, 
            Long costo, Boolean bajoDemanda, int cantLimBajoDemanda, int cantStock, int cantDisponible, int cantReservada, IvaEntity tipoIva) {
        this.id = id;
        this.nombre = nombre;
        this.subCategoria = categoria;
        this.precio = (int)(long)precio;
        this.costo = (int)(long)costo;
        this.bajoDemanda = bajoDemanda;
        this.cantLimBajoDemanda = cantLimBajoDemanda;
        this.cantStock = cantStock;
        this.cantDisponible = cantDisponible;
        this.cantReservada = cantReservada;
        this.tipoIva = tipoIva;
    }
    
    public ProductoDTO(Long id, String nombre, SubCategoriaEntity categoria, Long precio, 
            Long costo, Boolean bajoDemanda, int cantLimBajoDemanda, int cantStock, int cantDisponible, int cantReservada, IvaEntity tipoIva, Boolean esServicio) {
        this.id = id;
        this.nombre = nombre;
        this.subCategoria = categoria;
        this.precio = (int)(long)precio;
        this.costo = (int)(long)costo;
        this.bajoDemanda = bajoDemanda;
        this.cantLimBajoDemanda = cantLimBajoDemanda;
        this.cantStock = cantStock;
        this.cantDisponible = cantDisponible;
        this.cantReservada = cantReservada;
        this.tipoIva = tipoIva;
        this.esServicio = esServicio;
    }

    public ProductoDTO(Long id, String nombre, IvaEntity tipoIva) {
        this.id = id;
        this.nombre = nombre;
        this.tipoIva = tipoIva;
    }

    
    public SubCategoriaEntity getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(SubCategoriaEntity categoria) {
        this.subCategoria = categoria;
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


    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getBajoDemanda() {
        return bajoDemanda;
    }

    public void setBajoDemanda(Boolean bajoDemanda) {
        this.bajoDemanda = bajoDemanda;
    }

    public int getCantLimBajoDemanda() {
        return cantLimBajoDemanda;
    }

    public void setCantLimBajoDemanda(int cantLimBajoDemanda) {
        this.cantLimBajoDemanda = cantLimBajoDemanda;
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

    public Boolean getEsServicio() {
        return esServicio;
    }

    public void setEsServicio(Boolean esServicio) {
        this.esServicio = esServicio;
    }
    
    
}
