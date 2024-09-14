/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Entity;

import com.krazystore.krazystore.DTO.DetallePedidoDTO;
import jakarta.persistence.*;

/**
 *
 * @author HP
 */

@NamedNativeQuery(name= "DetallePedidoEntity.findPedido",
        query="SELECT p.id as id, p.nombre as nombre, p.precio as precio, "
                + "d.cantidad as cantidad, d.subtotal as subtotal, p.pre_venta as preVenta, "
                + "COALESCE(p.cant_pre_venta, 0) as cantLimBajoDemanda, COALESCE(e.stock, 0)as cantStock, "
                + "COALESCE(e.disponible, 0) as cantDisponible, COALESCE(e.reservado, 0) as cantReservada " 
                + "FROM detalle_pedidos d LEFT JOIN productos p "
                + "ON p.id = d.id_producto LEFT JOIN existencias e "
                + "ON e.id_producto = d.id_producto "
                + "WHERE d.id_pedido = ?1",
        resultSetMapping = "Mapping.DetallePedidoDTO")
@SqlResultSetMapping(name="Mapping.DetallePedidoDTO",
        classes = @ConstructorResult(targetClass = DetallePedidoDTO.class,
                columns = {@ColumnResult(name = "id"),
                @ColumnResult(name = "nombre"),
                @ColumnResult(name = "precio"),
                @ColumnResult(name = "cantidad"),
                @ColumnResult(name = "subtotal"),
                @ColumnResult(name = "preVenta"),
                @ColumnResult(name = "cantLimBajoDemanda"),
                @ColumnResult(name = "cantStock"),
                @ColumnResult(name = "cantDisponible"),
                @ColumnResult(name = "cantReservada")}))
@Entity
@Table(name = "detalle_pedidos" )
public class DetallePedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private PedidoEntity pedido;
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private ProductoEntity producto;
    @Column
    private int cantidad;
    @Column
    private int subtotal;
    @Column
    private int montoIva;
    @Column
    private int pagado=0;
    @Column
    private int saldoPendiente;
    @Column
    private int cantidadPagado;
    @Column
    private int cantidadFacturada=0;

    public DetallePedidoEntity() {
    }

    public DetallePedidoEntity(Long id, PedidoEntity pedido, ProductoEntity producto, int cantidad, int subtotal, int montoIva, int pagado, int saldoPendiente, int cantidadPagado) {
        this.id = id;
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.montoIva = montoIva;
        this.pagado = pagado;
        this.saldoPendiente = saldoPendiente;
        this.cantidadPagado = cantidadPagado;
    }

    public int getCantidadFacturada() {
        return cantidadFacturada;
    }

    public void setCantidadFacturada(int cantidadFacturada) {
        this.cantidadFacturada = cantidadFacturada;
    }
    
    

    public int getCantidadPagado() {
        return cantidadPagado;
    }

    public void setCantidadPagado(int cantidadPagado) {
        this.cantidadPagado = cantidadPagado;
    }

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PedidoEntity getPedido() {
        return pedido;
    }

    public void setPedido(PedidoEntity pedido) {
        this.pedido = pedido;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getMontoIva() {
        return montoIva;
    }

    public void setMontoIva(int montoIva) {
        this.montoIva = montoIva;
    }

    public int getPagado() {
        return pagado;
    }

    public void setPagado(int pagado) {
        this.pagado = pagado;
    }

    public int getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setSaldoPendiente(int saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }
    
    
}
