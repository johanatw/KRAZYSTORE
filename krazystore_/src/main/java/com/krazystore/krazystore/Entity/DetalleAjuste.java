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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "detalle_ajuste")
public class DetalleAjuste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_ajuste")
    private AjusteStock ajuste;
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private ProductoEntity producto;
    @Column(name = "cantidad_ajustada")
    private int cantidadAjustada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AjusteStock getAjuste() {
        return ajuste;
    }

    public void setAjuste(AjusteStock ajuste) {
        this.ajuste = ajuste;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }

    public int getCantidadAjustada() {
        return cantidadAjustada;
    }

    public void setCantidadAjustada(int cantidadAjustada) {
        this.cantidadAjustada = cantidadAjustada;
    }
    
    
}
