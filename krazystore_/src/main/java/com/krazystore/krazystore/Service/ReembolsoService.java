/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.PagoEntity;
import com.krazystore.krazystore.Entity.ReembolsoEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface ReembolsoService {
    List<ReembolsoEntity> findAll();
    Optional<ReembolsoEntity> findById(Long id);
    ReembolsoEntity saveReembolso(ReembolsoEntity reembolsoEntity);
    ReembolsoEntity updateReembolso(ReembolsoEntity reembolsoEntity, Long id);
    void deleteReembolso(Long id);

    List<ReembolsoEntity> findByIdAnticipo(Long id);
}
