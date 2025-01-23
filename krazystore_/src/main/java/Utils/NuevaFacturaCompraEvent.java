/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import com.krazystore.krazystore.Entity.CompraEntity;

/**
 *
 * @author HP
 */
public class NuevaFacturaCompraEvent extends NuevaFacturaEvent {

    public NuevaFacturaCompraEvent(TipoEvento tipoEvento, CompraEntity nuevaCompra) {
        super(tipoEvento, nuevaCompra);
    }
    

}
