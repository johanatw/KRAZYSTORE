/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

/**
 *
 * @author HP
 */
public class PedidoMontoPagadoDTO {
    private Long id;
    private int total;
    private Long totalPagos;

    public PedidoMontoPagadoDTO(Long id, int total, Long totalPagos) {
        this.id = id;
        this.total = total;
        this.totalPagos = totalPagos;
    }

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

    public Long getTotalPagos() {
        return totalPagos;
    }

    public void setTotalPagos(Long totalPagos) {
        this.totalPagos = totalPagos;
    }
    
    
}
