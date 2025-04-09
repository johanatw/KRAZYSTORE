/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.PuntoEntregaEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface PuntoEntregaService {
    List<PuntoEntregaEntity> findAll();
    Optional<PuntoEntregaEntity> findById(Long id);
    PuntoEntregaEntity savePuntoEntrega(PuntoEntregaEntity puntoEntrega);
    PuntoEntregaEntity updatePuntoEntrega(PuntoEntregaEntity puntoEntrega, Long id);
    void deletePuntoEntrega(Long id);
}
