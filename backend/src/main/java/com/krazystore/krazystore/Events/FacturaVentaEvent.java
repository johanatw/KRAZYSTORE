/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Events;

import Utils.TipoEvento;
import com.krazystore.krazystore.Entity.VentaEntity;

/**
 *
 * @author HP
 */
public class FacturaVentaEvent extends  FacturaEvent{

    public FacturaVentaEvent(TipoEvento tipoEvento, VentaEntity nuevaVenta) {
        super(tipoEvento, nuevaVenta);
    }

    public FacturaVentaEvent( TipoEvento tipoEvento, VentaEntity nuevaVenta, Long idPedido) {
        super(tipoEvento, nuevaVenta, idPedido);
    }

}
