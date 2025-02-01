/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.RecepcionCreationDTO;
import com.krazystore.krazystore.DTO.RecepcionDTO;
import com.krazystore.krazystore.Entity.RecepcionEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface RecepcionService {
    List<RecepcionDTO> findAll();
    RecepcionCreationDTO findById(Long id);
    RecepcionEntity saveRecepcion (RecepcionCreationDTO recepcion);
    RecepcionEntity updateRecepcion(RecepcionCreationDTO recepcion, Long id);
    void deleteRecepcion(Long id);
    int getTotalRecepcionadoPorProducto(Long pedidoId, Long productoId);
}
