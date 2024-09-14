/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

/**
 *
 * @author HP
 */
public class DetallePagoPedidoDTO {
    private Long idPL;
    private int total;
    private Long totalPagos;

    public DetallePagoPedidoDTO() {
    }

    public DetallePagoPedidoDTO(Long id, int total, Long totalPagos) {
        this.idPL = id;
        this.total = total;
        this.totalPagos = totalPagos;
    }
     public DetallePagoPedidoDTO(Long id, int total) {
        this.idPL = id;
        this.total = total;
        
    }
     public DetallePagoPedidoDTO(Long id) {
        this.idPL = id;
   
    }

    public Long getIdPedido() {
        return idPL;
    }

    public void setIdPedido(Long id) {
        this.idPL = id;
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
