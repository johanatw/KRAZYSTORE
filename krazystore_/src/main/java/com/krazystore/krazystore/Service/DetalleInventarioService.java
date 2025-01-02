/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.DetalleInventarioDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.DetalleInventario;
import java.util.List;

/**
 *
 * @author HP
 */
public interface DetalleInventarioService {
    List<DetalleInventario> findByIdInventario(Long id);
    List<DetalleInventario> saveDetInventario(List<DetalleInventario> detalle, Long idInventario) throws Exception;
    List<DetalleInventario> updateDetInventario(List<DetalleInventario> detalle, Long idInventario)throws Exception;
    void deleteDetInventario(Long idInventario);
    List<DetalleInventarioDTO> getDetallesInventarioIniciales();
    List<DetalleInventario> obtenerDetallesCompletos(Long id);
    List<ProductoExistenciasDTO> getProductosActualizarExistencias(Long id);
}
