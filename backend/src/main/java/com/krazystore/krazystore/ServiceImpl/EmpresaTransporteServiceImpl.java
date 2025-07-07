/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.EmpresaTransporte;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.krazystore.krazystore.Repository.EmpresaTransporteRepository;
import com.krazystore.krazystore.Service.EmpresaTransporteService;

/**
 *
 * @author HP
 */
@Service
public class EmpresaTransporteServiceImpl implements EmpresaTransporteService {
    private final EmpresaTransporteRepository enviorepository;

    public EmpresaTransporteServiceImpl(EmpresaTransporteRepository enviorepository) {
        this.enviorepository = enviorepository;
    }
    
  

    @Override
    public List<EmpresaTransporte> findAll() {
        return enviorepository.findAll();
    }

    @Override
    public Optional<EmpresaTransporte> findById(Long id) {
        return enviorepository.findById(id);
    }

    @Override
    public EmpresaTransporte saveEnvio(EmpresaTransporte envioEntity) {
        return enviorepository.save(envioEntity);
    }

    @Override
    public EmpresaTransporte updateEnvio(EmpresaTransporte envioEntity, Long id) {
        var updatedEnvio = enviorepository.findById(id).get();
        
        updatedEnvio.setDescripcion(envioEntity.getDescripcion());
      
        return enviorepository.save(updatedEnvio);

    }

    @Override
    public void deleteEnvio(Long id) {
        EmpresaTransporte deletedEmpresa = enviorepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa no existe"));
        
        deletedEmpresa.setActivo(Boolean.FALSE);
        enviorepository.save(deletedEmpresa);
    }
    
}
