/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Events;

/**
 *
 * @author HP
 */
import Utils.TipoEvento;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import java.util.List;

public class ProductosReservadosEvent extends CambioExistenciasEvent{

    public ProductosReservadosEvent(List<ProductoExistenciasDTO> productosActualizar, TipoEvento tipoEvento) {
        super(productosActualizar, tipoEvento);
    }
    
    
}
