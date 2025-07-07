/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Events;

import com.krazystore.krazystore.Entity.CostoEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public class PreciosCompraActualizadosEvent {
    List<CostoEntity> preciosActualizados;

    public PreciosCompraActualizadosEvent(List<CostoEntity> preciosActualizados) {
        this.preciosActualizados = preciosActualizados;
    }

    public List<CostoEntity> getPreciosActualizados() {
        return preciosActualizados;
    }

    public void setPreciosActualizados(List<CostoEntity> preciosActualizados) {
        this.preciosActualizados = preciosActualizados;
    }
    
    
}
