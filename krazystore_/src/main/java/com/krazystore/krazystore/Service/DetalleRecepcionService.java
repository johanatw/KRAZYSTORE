/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.DetallePedidoRecepcionDTO;
import com.krazystore.krazystore.Entity.DetalleRecepcion;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface DetalleRecepcionService {
    List<DetalleRecepcion> findByIdRecepcion(Long id);
    List<DetalleRecepcion> saveDetRecepcion (List<DetalleRecepcion> detalle, Long idRecepcion);
    List<DetalleRecepcion> updateDetRecepcion(List<DetalleRecepcion> detalle, Long idRecepcion);
    void deleteDetRecepcion(Long idRecepcion);
}
