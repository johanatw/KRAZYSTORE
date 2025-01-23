/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import com.krazystore.krazystore.Entity.VentaEntity;

/**
 *
 * @author HP
 */
public class NuevaFacturaVentaEvent extends  NuevaFacturaEvent{

    public NuevaFacturaVentaEvent(TipoEvento tipoEvento, VentaEntity nuevaVenta) {
        super(tipoEvento, nuevaVenta);
    }
    
    
}
