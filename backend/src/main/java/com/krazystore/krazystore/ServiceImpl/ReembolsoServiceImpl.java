/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.ReembolsoAnticipo;
import com.krazystore.krazystore.Repository.ReembolsoRepository;

import com.krazystore.krazystore.Service.ReembolsoService;
import java.util.List;
import java.util.Optional;
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
    public List<ReembolsoAnticipo> findAll() {
        return reembolsoRepository.findAllByOrderByIdDesc();
    }

    @Override
    public Optional<ReembolsoAnticipo> findById(Long id) {
        return reembolsoRepository.findById(id);
    }

    @Override
    public ReembolsoAnticipo saveReembolso(ReembolsoAnticipo reembolsoEntity) {
        

        ReembolsoAnticipo reembolso = reembolsoRepository.save(reembolsoEntity);
        
        
        
        return reembolso;
        
    }
    
  

    @Override
    public ReembolsoAnticipo updateReembolso(ReembolsoAnticipo reembolsoEntity, Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteReembolsoAnticipo(Long id) {

        reembolsoRepository.deleteById(id);
        
    }
    
    @Override
    public void deleteReembolsosAnticipo(List<Long> ids) {

        reembolsoRepository.deleteByIds(ids);
        
    }

    @Override
    public List<ReembolsoAnticipo> findByIdAnticipo(Long id) {
        return reembolsoRepository.findByIdAnticipo(id);
    }

    @Override
    public List<Long> getIdReembolsosByIdAnticipo(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
