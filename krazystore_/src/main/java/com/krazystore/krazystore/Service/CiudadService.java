/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.CiudadEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface CiudadService {
    List<CiudadEntity> findAll();
    List<CiudadEntity> findByIdDepartamento(Long id);
    Optional<CiudadEntity> findById(Long id);
    CiudadEntity saveCiudad(CiudadEntity ciudadEntity);
    CiudadEntity updateCiudad(CiudadEntity ciudadEntity, Long id);
    void deleteCiudad(Long id);
}
