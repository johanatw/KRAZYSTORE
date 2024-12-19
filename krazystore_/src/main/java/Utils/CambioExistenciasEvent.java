/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import java.util.List;

/**
 *
 * @author HP
 */
public abstract class CambioExistenciasEvent {
    private List<ProductoExistenciasDTO> productosActualizar;
    private final TipoEventoExistencias tipoEvento;

    public CambioExistenciasEvent(List<ProductoExistenciasDTO> productosActualizar, TipoEventoExistencias tipoEvento) {
        this.productosActualizar = productosActualizar;
        this.tipoEvento = tipoEvento;
    }

    public List<ProductoExistenciasDTO> getProductosActualizar() {
        return productosActualizar;
    }

    public void setProductosActualizar(List<ProductoExistenciasDTO> productosActualizar) {
        this.productosActualizar = productosActualizar;
    }

    public TipoEventoExistencias getTipoEvento() {
        return tipoEvento;
    }

    
    
    
}
