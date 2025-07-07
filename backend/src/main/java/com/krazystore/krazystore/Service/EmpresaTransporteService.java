/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.EmpresaTransporte;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface EmpresaTransporteService {
    List<EmpresaTransporte> findAll();
    Optional<EmpresaTransporte> findById(Long id);
    EmpresaTransporte saveEnvio(EmpresaTransporte envioEntity);
    EmpresaTransporte updateEnvio(EmpresaTransporte envioEntity, Long id);
    void deleteEnvio(Long id);
}
