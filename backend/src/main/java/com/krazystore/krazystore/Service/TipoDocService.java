/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.TipoDocEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface TipoDocService {
    List<TipoDocEntity> findAll();
    Optional<TipoDocEntity> findById(Long id);
    TipoDocEntity saveTipoDoc(TipoDocEntity tipoDocEntity);
    TipoDocEntity updateTipoDoc(TipoDocEntity tipoDocEntity, Long id);
    void deleteTipoDoc(Long id);
}
