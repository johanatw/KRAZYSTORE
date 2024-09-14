/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.CostoEnvioEntity;
import com.krazystore.krazystore.Repository.CostoEnvioRepository;
import com.krazystore.krazystore.Service.CostoEnvioService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class CostoEnvioServiceImpl implements CostoEnvioService{
    private final CostoEnvioRepository costoenviorepository;

    public CostoEnvioServiceImpl(CostoEnvioRepository costoenviorepository) {
        this.costoenviorepository = costoenviorepository;
    }
    
    

    @Override
    public List<CostoEnvioEntity> findAll() {
        return costoenviorepository.findAll();
    }

    @Override
    public Optional<CostoEnvioEntity> findById(Long id) {
        return costoenviorepository.findById(id);
    }
    
    @Override
    public List<CostoEnvioEntity> findByCiudad(Long id){
        return costoenviorepository.findByCiudad(id);
    }

    @Override
    public CostoEnvioEntity saveCostoEnvio(CostoEnvioEntity costoEnvioEntity) {
        return costoenviorepository.save(costoEnvioEntity);
    }

    @Override
    public CostoEnvioEntity updateCostoEnvio(CostoEnvioEntity costoEnvioEntity, Long id) {
        var updatedCostoEnvio = costoenviorepository.findById(id).get();
        
        updatedCostoEnvio.setEnvio(costoEnvioEntity.getEnvio());
        updatedCostoEnvio.setCiudad(costoEnvioEntity.getCiudad());
        updatedCostoEnvio.setCosto(costoEnvioEntity.getCosto());
     
      
        return costoenviorepository.save(updatedCostoEnvio);

    }

    @Override
    public void deleteCostoEnvio(Long id) {
        costoenviorepository.deleteById(id);
    }
    
}
