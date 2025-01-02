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
public class AjustarInventario extends CambioExistenciasEvent {
    
    public AjustarInventario(List<ProductoExistenciasDTO> productosActualizar, TipoEventoExistencias tipoEvento) {
        super(productosActualizar, tipoEvento);
    }
    
}
