/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.FormaPagoEntity;
import com.krazystore.krazystore.Entity.PagoPedidoCompra;
import java.util.List;

/**
 *
 * @author HP
 */
public class PagoPedidoCompraCreationDTO {
    private PagoPedidoCompra pagoPedidoCompra;
    private List<FormaPagoEntity> pagos;

    public PagoPedidoCompraCreationDTO() {
    }

    public PagoPedidoCompra getPagoPedidoCompra() {
        return pagoPedidoCompra;
    }

    public void setPagoPedidoCompra(PagoPedidoCompra pagoPedidoCompra) {
        this.pagoPedidoCompra = pagoPedidoCompra;
    }

    public List<FormaPagoEntity> getPagos() {
        return pagos;
    }

    public void setPagos(List<FormaPagoEntity> pagos) {
        this.pagos = pagos;
    }
    
    
}
