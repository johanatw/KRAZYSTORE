/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.DireccionEntity;
import com.krazystore.krazystore.Repository.DireccionRepository;
import com.krazystore.krazystore.Service.DireccionService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class DireccionServiceImpl implements DireccionService {
    private final DireccionRepository direccionrepository;

    public DireccionServiceImpl(DireccionRepository direccionrepository) {
        this.direccionrepository = direccionrepository;
    }


    @Override
    public List<DireccionEntity> findAll() {
        return direccionrepository.findAll();
    }

    @Override
    public Optional<DireccionEntity> findById(Long id) {
        return direccionrepository.findById(id);
    }

    @Override
    public DireccionEntity saveDireccion(DireccionEntity direccionEntity) {
        return direccionrepository.save(direccionEntity);
    }

    @Override
    public DireccionEntity updateDireccion(DireccionEntity direccionEntity, Long id) {
        DireccionEntity updatedDireccion = direccionrepository.findById(id).get();
        
        updatedDireccion.setCiudad(direccionEntity.getCiudad());
        updatedDireccion.setDepartamento(direccionEntity.getDepartamento());
        updatedDireccion.setDireccion(direccionEntity.getDireccion());
        updatedDireccion.setPersona(direccionEntity.getPersona());
        return direccionrepository.save(updatedDireccion);
    }


    @Override
    public void deleteDireccion(Long id) {
        direccionrepository.deleteById(id);
    }

    
}
