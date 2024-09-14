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
@NamedNativeQuery(name= "ProductoEntity.findProductosDTO",
        query="SELECT p.id as id, c.id as idCategoria, p.nombre as nombre,  c.descripcion as categoria, "
                + "p.precio as precio, p.costo as costo, p.estado as estado, p.pre_venta as bajoDemanda, "
                + "COALESCE(p.cant_pre_venta, 0) as cantLimBajoDemanda, e.id as idExistencia, COALESCE(e.stock, 0)as cantStock, "
                + "COALESCE(e.disponible, 0) as cantDisponible, COALESCE(e.reservado, 0) as cantReservada "
                + "FROM productos p LEFT JOIN existencias e "
                + "ON e.id_producto = p.id "
                + "JOIN categorias c "
                + "ON c.id = p.id_categoria",
        resultSetMapping = "Mapping.ProductoDTO")
@SqlResultSetMapping(name="Mapping.ProductoDTO",
        classes = @ConstructorResult(targetClass = ProductoDTO.class,
                columns = {@ColumnResult(name = "id"),
                @ColumnResult(name = "idCategoria"),
                @ColumnResult(name = "nombre"),
                @ColumnResult(name = "categoria"),
                @ColumnResult(name = "precio"),
                @ColumnResult(name = "costo"),
                @ColumnResult(name = "estado"),
                @ColumnResult(name = "bajoDemanda"),
                @ColumnResult(name = "cantLimBajoDemanda"),
                @ColumnResult(name = "idExistencia"),
                @ColumnResult(name = "cantStock"),
                @ColumnResult(name = "cantDisponible"),
                @ColumnResult(name = "cantReservada")}))


@Entity
@Table(name = "productos")
public class ProductoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull
    private String nombre;
    
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private CategoriaEntity categoria;
    @Column
    private Integer precio;
    @Column
    private Integer costo;
    @Column
    private Boolean estado;
    @Column
    private Boolean preVenta = false;
    @Column
    private Integer cantPreVenta;
    @Column
    private int cantStock = 0;
    @Column
    private int cantDisponible = 0;
    @Column
    private int cantReservada = 0;
    @Column
    private Integer seña;
    
    public ProductoEntity() {
        
    }

    public ProductoEntity(Long id, String nombre, CategoriaEntity categoria, Integer precio, Integer costo, Boolean estado, Boolean preVenta, Integer cantPreVenta, int cantStock, int cantDisponible, int cantReservada, Integer seña) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.costo = costo;
        this.estado = estado;
        this.preVenta = preVenta;
        this.cantPreVenta = cantPreVenta;
        this.cantStock = cantStock;
        this.cantDisponible = cantDisponible;
        this.cantReservada = cantReservada;
        this.seña = seña;
    }

    public Integer getSeña() {
        return seña;
    }

    public void setSeña(Integer seña) {
        this.seña = seña;
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

    
    
    

    public Boolean getPreVenta() {
        return preVenta;
    }

    public void setPreVenta(Boolean preVenta) {
        this.preVenta = preVenta;
    }

    public Integer getCantPreVenta() {
        return cantPreVenta;
    }

    public void setCantPreVenta(Integer cantPreVenta) {
        this.cantPreVenta = cantPreVenta;
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

    public CategoriaEntity getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEntity categoria) {
        this.categoria = categoria;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Integer getCosto() {
        return costo;
    }

    public void setCosto(Integer costo) {
        this.costo = costo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
    
    
}
