/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.EstadoEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface EstadoService {
    List<EstadoEntity> findAll();
    Optional<EstadoEntity> findById(Long id);
    List<EstadoEntity> findByTipo(String tipo);
    EstadoEntity saveEstado(EstadoEntity estadoEntity);
    EstadoEntity updateEstado(EstadoEntity estadoEntity, Long id);
    void deleteEstado(Long id);
}
