/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.DetalleInventarioDTO;
import com.krazystore.krazystore.DTO.InventarioCreationDTO;
import com.krazystore.krazystore.DTO.InventarioDTO;
import com.krazystore.krazystore.Entity.InventarioEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface InventarioService {
    List<InventarioDTO> findAll();
    InventarioCreationDTO findById(Long id);
    InventarioEntity saveInventario (InventarioCreationDTO inventarioDTO) throws Exception;
    InventarioEntity updateInventario(InventarioCreationDTO inventarioDTO, Long id)throws Exception;
    void deleteInventario(Long id);
    List<DetalleInventarioDTO> getDetallesInventarioIniciales(List<Long> ids);
    InventarioEntity ajustarInventario(Long id);
    InventarioEntity finalizarInventario(InventarioCreationDTO inventarioDTO, Long id) throws Exception;
}
