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
        
        //updatedDireccion.setCiudad(direccionEntity.getCiudad());
        //updatedDireccion.setDepartamento(direccionEntity.getDepartamento());
        updatedDireccion.setCalle1(direccionEntity.getCalle1());
        updatedDireccion.setCalle2(direccionEntity.getCalle2());
        updatedDireccion.setNroCasa(direccionEntity.getNroCasa());
        updatedDireccion.setDireccion(direccionEntity.getDireccion());
        updatedDireccion.setCiudad(direccionEntity.getCiudad());
        updatedDireccion.setPersona(direccionEntity.getPersona());
        //updatedDireccion.setLat(direccionEntity.getLat());
        //updatedDireccion.setLng(direccionEntity.getLng());
        return direccionrepository.save(updatedDireccion);
    }

    // Verifica si algún campo tiene valor
    @Override
    public boolean algunCampoTieneValor(DireccionEntity direccion) {
        return (direccion.getCalle1() != null && !direccion.getCalle1().isEmpty()) ||
               (direccion.getCalle2() != null && !direccion.getCalle2().isEmpty()) ||
                (direccion.getCalle3() != null && !direccion.getCalle3().isEmpty()) ||
               (direccion.getNroCasa() != null && !direccion.getNroCasa().isEmpty()) ||
                (direccion.getDireccion() != null && !direccion.getDireccion().isEmpty()) ||
                (direccion.getCiudad() != null);
    }

    @Override
    public void deleteDireccion(Long id) {
        direccionrepository.deleteById(id);
    }

    @Override
    public List<DireccionEntity> findDireccionesById(Long id) {
        return direccionrepository.findByIdCliente(id);
      }

    
}
