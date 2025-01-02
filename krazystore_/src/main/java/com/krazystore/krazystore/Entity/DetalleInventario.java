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
@Table(name = "detalle_inventario")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleInventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_inventario")
    private InventarioEntity inventario;
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private ProductoEntity producto;
    @Column
    private int cantStock;
    @Column
    private int cantContada;
    @Column
    private int diferencia;
    
    public DetalleInventario(Long id, Long idProducto, String producto, Long idCategoria, String categoria, int cantStock, int cantContada) {
        this.id = id;
        this.producto = new ProductoEntity();
        this.producto.setId(idProducto);
        this.producto.setNombre(producto);
        this.producto.setCategoria(new CategoriaEntity(idCategoria, categoria));
        this.cantStock = cantStock;
        this.cantContada = cantContada;
        this.diferencia = this.cantContada - this.cantStock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InventarioEntity getInventario() {
        return inventario;
    }

    public void setInventario(InventarioEntity inventario) {
        this.inventario = inventario;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }

    public int getCantStock() {
        return cantStock;
    }

    public void setCantStock(int cantStock) {
        this.cantStock = cantStock;
    }

    public int getCantContada() {
        return cantContada;
    }

    public void setCantContada(int cantContada) {
        this.cantContada = cantContada;
    }

    public int getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(int diferencia) {
        this.diferencia = diferencia;
    }
    
    
    
}
