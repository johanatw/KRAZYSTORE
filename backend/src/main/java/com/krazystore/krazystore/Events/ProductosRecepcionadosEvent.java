/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Events;

import Utils.TipoEvento;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import java.util.List;

/**
 *
 * @author HP
 */
public class ProductosRecepcionadosEvent extends CambioExistenciasEvent{

    public ProductosRecepcionadosEvent(List<ProductoExistenciasDTO> productosActualizar, TipoEvento tipoEvento) {
        super(productosActualizar, tipoEvento);
    }
    
}
