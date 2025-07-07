/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.MedioCobroEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface MedioCobroService {
    List<MedioCobroEntity> findAll();
    Optional<MedioCobroEntity> findById(Long id);
    MedioCobroEntity saveMedioCobro(MedioCobroEntity medioCobroEntity);
    MedioCobroEntity updateMedioCobro(MedioCobroEntity medioCobroEntity, Long id);
    void deleteMedioCobro(Long id);
}
