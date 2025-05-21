/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.PedidoEntity;

/**
 *
 * @author HP
 */
public class EstadoPagoPedidoDTO {
    private PedidoEntity pedido;
    private int total;
    private int totalAnticipos;
    private int totalPagos;

    public EstadoPagoPedidoDTO() {
    }

    public EstadoPagoPedidoDTO(PedidoEntity pedido, long total, long totalPagos) {
        this.pedido = pedido;
        this.total = (int)(long)total;
        this.totalPagos = (int)(long)totalPagos;
    }

    
    public int getTotalAnticipos() {
        return totalAnticipos;
    }

    public void setTotalAnticipos(int totalAnticipos) {
        this.totalAnticipos = totalAnticipos;
    }

    public PedidoEntity getPedido() {
        return pedido;
    }

    public void setPedido(PedidoEntity pedido) {
        this.pedido = pedido;
    }
    
    


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPagos() {
        return totalPagos;
    }

    public void setTotalPagos(int totalPagos) {
        this.totalPagos = totalPagos;
    }
    
    
}
