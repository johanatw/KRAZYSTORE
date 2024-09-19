/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.CajaEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface CajaService {
    Optional<CajaEntity> getCajaAbierta();
    CajaEntity abrirCaja();
    Optional<CajaEntity> findById(Long id);
    List<CajaEntity> findAll();
    void cerrarCaja(Long id);
    CajaEntity getCaja();
}
