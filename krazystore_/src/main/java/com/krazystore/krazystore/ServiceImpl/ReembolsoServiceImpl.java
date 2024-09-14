/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
import com.krazystore.krazystore.Entity.ReembolsoEntity;
import com.krazystore.krazystore.Repository.ReembolsoRepository;
import com.krazystore.krazystore.Service.AnticipoService;
import com.krazystore.krazystore.Service.MovimientoService;

import com.krazystore.krazystore.Service.ReembolsoService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class ReembolsoServiceImpl implements ReembolsoService {
    private final ReembolsoRepository reembolsoRepository;

    public ReembolsoServiceImpl(ReembolsoRepository reembolsoRepository) {
        this.reembolsoRepository = reembolsoRepository;
    }

  

    @Override
    public List<ReembolsoEntity> findAll() {
        return reembolsoRepository.findAllByOrderByIdDesc();
    }

    @Override
    public Optional<ReembolsoEntity> findById(Long id) {
        return reembolsoRepository.findById(id);
    }

    @Override
    public ReembolsoEntity saveReembolso(ReembolsoEntity reembolsoEntity) {
        

        ReembolsoEntity reembolso = reembolsoRepository.save(reembolsoEntity);
        
        
        
        return reembolso;
        
    }
    
  

    @Override
    public ReembolsoEntity updateReembolso(ReembolsoEntity reembolsoEntity, Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteReembolso(Long id) {

        reembolsoRepository.deleteById(id);
        
    }

    @Override
    public List<ReembolsoEntity> findByIdAnticipo(Long id) {
        return reembolsoRepository.findByIdAnticipo(id);
    }
    
}
