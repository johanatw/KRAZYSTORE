/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

/**
 *
 * @author HP
 */
public class EstadoPagoPedidoDTO {
    private Long id;
    private int total;
    private int totalAnticipos;
    private int totalPagos;

    public EstadoPagoPedidoDTO() {
    }

    public EstadoPagoPedidoDTO(Long id, int total, long totalPagos) {
        this.id = id;
        this.total = total;
        this.totalPagos = (int)(long)totalPagos;
    }

    public EstadoPagoPedidoDTO(Long id, int total, long totalAnticipos, long totalPagos) {
        this.id = id;
        this.total = total;
        this.totalAnticipos = (int)(long)totalAnticipos;
        this.totalPagos = (int)(long)totalPagos;
    }

    
    public int getTotalAnticipos() {
        return totalAnticipos;
    }

    public void setTotalAnticipos(int totalAnticipos) {
        this.totalAnticipos = totalAnticipos;
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

    public int getTotalPagos() {
        return totalPagos;
    }

    public void setTotalPagos(int totalPagos) {
        this.totalPagos = totalPagos;
    }
    
    
}
