/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.DepartamentoEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface DepartamentoService {
    List<DepartamentoEntity> findAll();
    Optional<DepartamentoEntity> findById(Long id);
    DepartamentoEntity saveDepartamento(DepartamentoEntity departamentoEntity);
    DepartamentoEntity updateDepartamento(DepartamentoEntity departamentoEntity, Long id);
    void deleteDepartamento(Long id);
}
