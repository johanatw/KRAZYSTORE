/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.MotivoAjusteEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface MotivoAjusteService {
    List<MotivoAjusteEntity> findAll();
    Optional<MotivoAjusteEntity> findById(Long id);
    MotivoAjusteEntity saveMotivoAjuste(MotivoAjusteEntity motivoEntity);
    MotivoAjusteEntity updateMotivoAjuste(MotivoAjusteEntity motivoEntity, Long id);
    void deleteMotivoAjuste(Long id);
}
