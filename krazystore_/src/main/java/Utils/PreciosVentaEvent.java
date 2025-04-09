/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import com.krazystore.krazystore.Entity.PrecioVentaEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public class PreciosVentaEvent {
    List<PrecioVentaEntity> preciosActualizados;

    public PreciosVentaEvent() {
    }

    
    public PreciosVentaEvent(List<PrecioVentaEntity> preciosActualizados) {
        this.preciosActualizados = preciosActualizados;
    }

    public List<PrecioVentaEntity> getPreciosActualizados() {
        return preciosActualizados;
    }

    public void setPreciosActualizados(List<PrecioVentaEntity> preciosActualizados) {
        this.preciosActualizados = preciosActualizados;
    }
    
    
}
