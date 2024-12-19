/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author HP
 */
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import java.util.List;

public class ProductosReservadosEvent extends CambioExistenciasEvent{

    public ProductosReservadosEvent(List<ProductoExistenciasDTO> productosActualizar, TipoEventoExistencias tipoEvento) {
        super(productosActualizar, tipoEvento);
    }
    
    
}
