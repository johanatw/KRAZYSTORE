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

/**
 *
 * @author HP
 */
@Entity
@Table(name = "compras_recepciones")
public class CompraRecepcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name="id_recepcion")
    private RecepcionEntity recepcion;
    @ManyToOne
    @JoinColumn(name="id_compra")
    private CompraEntity compra;

    public CompraRecepcion() {
    }

    public CompraRecepcion(Long id, RecepcionEntity recepcion, CompraEntity compra) {
        this.id = id;
        this.recepcion = recepcion;
        this.compra = compra;
    }

    public CompraRecepcion(RecepcionEntity recepcion, CompraEntity compra) {
        this.recepcion = recepcion;
        this.compra = compra;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RecepcionEntity getRecepcion() {
        return recepcion;
    }

    public void setRecepcion(RecepcionEntity recepcion) {
        this.recepcion = recepcion;
    }

    public CompraEntity getCompra() {
        return compra;
    }

    public void setCompra(CompraEntity compra) {
        this.compra = compra;
    }
    
    
}
