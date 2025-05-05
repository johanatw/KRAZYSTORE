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
import java.util.Date;
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
@Table(name="pagos_adelantados_pedidos_compra")
public class PagoPedidoCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column
    private int total;
    @Column
    private Date fecha;
    @ManyToOne
    @JoinColumn(name = "id_pedido_compra")
    private PedidoCompraEntity pedidoCompra;
    @Column
    private int saldo;
    @Column
    private int reembolsado=0;
    @Column
    private int utilizado=0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public PedidoCompraEntity getPedidoCompra() {
        return pedidoCompra;
    }

    public void setPedidoCompra(PedidoCompraEntity pedidoCompra) {
        this.pedidoCompra = pedidoCompra;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public int getReembolsado() {
        return reembolsado;
    }

    public void setReembolsado(int reembolsado) {
        this.reembolsado = reembolsado;
    }

    public int getUtilizado() {
        return utilizado;
    }

    public void setUtilizado(int utilizado) {
        this.utilizado = utilizado;
    }
    
    
}

    