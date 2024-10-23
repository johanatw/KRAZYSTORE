/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.DireccionEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface DireccionService {
    List<DireccionEntity> findAll();
    Optional<DireccionEntity> findById(Long id);
    DireccionEntity saveDireccion(DireccionEntity direccionEntity);
    DireccionEntity updateDireccion(DireccionEntity direccionEntity, Long id);
    boolean algunCampoTieneValor(DireccionEntity direccion);
    List<DireccionEntity> findDireccionesById(Long id);
    void deleteDireccion(Long id);
}
