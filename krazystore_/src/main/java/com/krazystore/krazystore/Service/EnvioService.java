/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.EnvioEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface EnvioService {
    List<EnvioEntity> findAll();
    Optional<EnvioEntity> findById(Long id);
    EnvioEntity saveEnvio(EnvioEntity envioEntity);
    EnvioEntity updateEnvio(EnvioEntity envioEntity, Long id);
    void deleteEnvio(Long id);
}
