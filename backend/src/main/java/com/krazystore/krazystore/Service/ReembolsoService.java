/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.ReembolsoAnticipo;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface ReembolsoService {
    List<ReembolsoAnticipo> findAll();
    Optional<ReembolsoAnticipo> findById(Long id);
    ReembolsoAnticipo saveReembolso(ReembolsoAnticipo reembolsoEntity);
    ReembolsoAnticipo updateReembolso(ReembolsoAnticipo reembolsoEntity, Long id);
    void deleteReembolsoAnticipo(Long id);
    void deleteReembolsosAnticipo(List<Long> ids);
    List<ReembolsoAnticipo> findByIdAnticipo(Long id);
    List<Long> getIdReembolsosByIdAnticipo(Long id);
}
