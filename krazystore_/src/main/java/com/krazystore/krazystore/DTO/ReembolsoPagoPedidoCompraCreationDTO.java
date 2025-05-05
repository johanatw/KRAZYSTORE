/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.FormaCobroEntity;
import com.krazystore.krazystore.Entity.ReembolsoPagoPedidoCompra;
import java.util.List;

/**
 *
 * @author HP
 */
public class ReembolsoPagoPedidoCompraCreationDTO {
    private ReembolsoPagoPedidoCompra reembolso;
    private List<FormaCobroEntity> cobros;

    public ReembolsoPagoPedidoCompraCreationDTO() {
    }

    public ReembolsoPagoPedidoCompra getReembolso() {
        return reembolso;
    }

    public void setReembolso(ReembolsoPagoPedidoCompra reembolso) {
        this.reembolso = reembolso;
    }

    public List<FormaCobroEntity> getCobros() {
        return cobros;
    }

    public void setCobros(List<FormaCobroEntity> cobros) {
        this.cobros = cobros;
    }
    
    
}
