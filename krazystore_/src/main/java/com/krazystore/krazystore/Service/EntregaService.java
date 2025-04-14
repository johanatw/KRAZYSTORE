/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.EntregaCreationDTO;
import com.krazystore.krazystore.Entity.EntregaEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface EntregaService {
    List<EntregaEntity> findAll();
    EntregaCreationDTO findById(Long id);
    EntregaEntity saveEntrega(EntregaCreationDTO entrega);
    EntregaEntity updateEntrega(EntregaCreationDTO entrega, Long id);
    EntregaEntity reprogramarEntrega(EntregaCreationDTO entrega, Long id);
    EntregaEntity marcarComoEntregado(Long id);
    EntregaEntity marcarComoNoEntregado(Long id);
    void deleteEntrega(Long id);
}
