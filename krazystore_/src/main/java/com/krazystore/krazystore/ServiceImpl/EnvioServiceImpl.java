/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.EnvioEntity;
import com.krazystore.krazystore.Repository.EnvioRepository;
import com.krazystore.krazystore.Service.EnvioService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class EnvioServiceImpl implements EnvioService {
    private final EnvioRepository enviorepository;

    public EnvioServiceImpl(EnvioRepository enviorepository) {
        this.enviorepository = enviorepository;
    }
    
  

    @Override
    public List<EnvioEntity> findAll() {
        return enviorepository.findAll();
    }

    @Override
    public Optional<EnvioEntity> findById(Long id) {
        return enviorepository.findById(id);
    }

    @Override
    public EnvioEntity saveEnvio(EnvioEntity envioEntity) {
        return enviorepository.save(envioEntity);
    }

    @Override
    public EnvioEntity updateEnvio(EnvioEntity envioEntity, Long id) {
        var updatedEnvio = enviorepository.findById(id).get();
        
        updatedEnvio.setDescripcion(envioEntity.getDescripcion());
      
        return enviorepository.save(updatedEnvio);

    }

    @Override
    public void deleteEnvio(Long id) {
        enviorepository.deleteById(id);
    }
    
}
