/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

/**
 *
 * @author HP
 */
public class EntregasPedidoDTO {
    private Long pedidos;
    private Long pendientes;
    private Long entregados;
    private Long noEntregados;
    private Long preparados;

    public EntregasPedidoDTO(Long preparados, Long entregados, Long noEntregados) {
        this.pendientes = preparados;
        this.entregados = entregados;
        this.noEntregados = noEntregados;
        this.preparados = preparados + entregados + noEntregados;
    }

    public EntregasPedidoDTO(Long pedidos, Long pendientes, Long entregados, Long noEntregados) {
        this.pedidos = pedidos;
        this.pendientes = pendientes;
        this.entregados = entregados;
        this.noEntregados = noEntregados;
    }

    
    public EntregasPedidoDTO() {
    }

    
    public Long getPendientes() {
        return pendientes;
    }

    public void setPendientes(Long pendientes) {
        this.pendientes = pendientes;
    }

    public Long getEntregados() {
        return entregados;
    }

    public void setEntregados(Long entregados) {
        this.entregados = entregados;
    }

    public Long getNoEntregados() {
        return noEntregados;
    }

    public void setNoEntregados(Long noEntregados) {
        this.noEntregados = noEntregados;
    }

    public Long getPedidos() {
        return pedidos;
    }

    public void setPedidos(Long pedidos) {
        this.pedidos = pedidos;
    }

    public Long getPreparados() {
        return preparados;
    }

    public void setPreparados(Long preparados) {
        this.preparados = preparados;
    }
    
    
}
