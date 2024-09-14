/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.CostoEnvioEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface CostoEnvioService {
    List<CostoEnvioEntity> findAll();
    Optional<CostoEnvioEntity> findById(Long id);
    List<CostoEnvioEntity> findByCiudad(Long id);
    CostoEnvioEntity saveCostoEnvio(CostoEnvioEntity costoEnvioEntity);
    CostoEnvioEntity updateCostoEnvio(CostoEnvioEntity costoEnvioEntity, Long id);
    void deleteCostoEnvio(Long id);
}
