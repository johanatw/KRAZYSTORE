/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.DetalleAjusteDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.DetalleAjuste;
import java.util.List;

/**
 *
 * @author HP
 */
public interface DetalleAjusteService {
    List<DetalleAjuste> findByIdAjuste(Long id);
    List<DetalleAjuste> saveDetAjuste(List<DetalleAjuste> detalle, Long idAjuste) throws Exception;
    List<DetalleAjuste> updateDetAjuste(List<DetalleAjuste> detalle, Long idAjuste)throws Exception;
    void deleteDetAjuste(Long idInventario);
    List<DetalleAjusteDTO> obtenerProductosParaAjuste();
    List<ProductoExistenciasDTO> getProductosActualizarExistencias(Long id);
}
