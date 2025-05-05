/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.MedioPagoEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface MedioPagoService {
    List<MedioPagoEntity> findAll();
    Optional<MedioPagoEntity> findById(Long id);
    MedioPagoEntity saveMedioPago(MedioPagoEntity medioPagoEntity);
    MedioPagoEntity updateMedioPago(MedioPagoEntity medioPagoEntity, Long id);
    void deleteMedioPago(Long id);
}
