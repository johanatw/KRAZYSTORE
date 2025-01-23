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

public class ProductosFacturadosEvent extends CambioExistenciasEvent{

    public ProductosFacturadosEvent(List<ProductoExistenciasDTO> productosActualizar, TipoEvento tipoEvento) {
        super(productosActualizar, tipoEvento);
    }

    
    
}
