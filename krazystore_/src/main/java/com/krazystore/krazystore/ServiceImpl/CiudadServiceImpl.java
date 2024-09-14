/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.CiudadEntity;
import com.krazystore.krazystore.Repository.CiudadRepository;
import com.krazystore.krazystore.Service.CiudadService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class CiudadServiceImpl implements CiudadService {
    private final CiudadRepository ciudadrepository;

    public CiudadServiceImpl(CiudadRepository ciudadrepository) {
        this.ciudadrepository = ciudadrepository;
    }

    @Override
    public List<CiudadEntity> findAll() {
        return ciudadrepository.findAll();
    }

    @Override
    public Optional<CiudadEntity> findById(Long id) {
        return ciudadrepository.findById(id);
    }

    @Override
    public CiudadEntity saveCiudad(CiudadEntity ciudadEntity) {
        return ciudadrepository.save(ciudadEntity);
    }

    @Override
    public CiudadEntity updateCiudad(CiudadEntity ciudadEntity, Long id) {
        CiudadEntity updatedCiudad = ciudadrepository.findById(id).get();
        
        updatedCiudad.setDescripcion(ciudadEntity.getDescripcion());
        return ciudadrepository.save(updatedCiudad);
    }

    @Override
    public void deleteCiudad(Long id) {
        ciudadrepository.deleteById(id);
    }
    
}
