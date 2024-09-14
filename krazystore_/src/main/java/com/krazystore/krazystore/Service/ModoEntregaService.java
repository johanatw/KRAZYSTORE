/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.ModoEntregaEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface ModoEntregaService {
    List<ModoEntregaEntity> findAll();
    Optional<ModoEntregaEntity> findById(Long id);
    ModoEntregaEntity saveModoEntrega(ModoEntregaEntity modoEntregaEntity);
    ModoEntregaEntity updateModoEntrega(ModoEntregaEntity modoEntregaEntity, Long id);
    void deleteModoEntrega(Long id);
}
