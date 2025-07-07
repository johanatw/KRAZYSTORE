/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.MedioCobroEntity;
import com.krazystore.krazystore.Repository.MedioCobroRepository;
import com.krazystore.krazystore.Service.MedioCobroService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class MedioCobroServiceImpl implements MedioCobroService {

    public MedioCobroServiceImpl(MedioCobroRepository medioCobroRepository) {
        this.medioCobroRepository = medioCobroRepository;
    }

    private final MedioCobroRepository medioCobroRepository;
    
    @Override
    public List<MedioCobroEntity> findAll() {
        return medioCobroRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Optional<MedioCobroEntity> findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public MedioCobroEntity saveMedioCobro(MedioCobroEntity medioCobroEntity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public MedioCobroEntity updateMedioCobro(MedioCobroEntity medioCobroEntity, Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteMedioCobro(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
