/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.ModoEntregaEntity;
import com.krazystore.krazystore.Repository.ModoEntregaRepository;
import com.krazystore.krazystore.Service.ModoEntregaService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class ModoEntregaServiceImpl implements ModoEntregaService {
    private final ModoEntregaRepository modoentregarepository;

    public ModoEntregaServiceImpl(ModoEntregaRepository modoentregarepository) {
        this.modoentregarepository = modoentregarepository;
    }

    @Override
    public List<ModoEntregaEntity> findAll() {
         return modoentregarepository.findAll();
    }

    @Override
    public Optional<ModoEntregaEntity> findById(Long id) {
        return modoentregarepository.findById(id);
    }

    @Override
    public ModoEntregaEntity saveModoEntrega(ModoEntregaEntity modoEntregaEntity) {
        return modoentregarepository.save(modoEntregaEntity);
    }

    @Override
    public ModoEntregaEntity updateModoEntrega(ModoEntregaEntity modoEntregaEntity, Long id) {
        ModoEntregaEntity updatedModoEntrega = modoentregarepository.findById(id).get();
        
        updatedModoEntrega.setDescripcion(modoEntregaEntity.getDescripcion());
        return modoentregarepository.save(updatedModoEntrega);
    }

    @Override
    public void deleteModoEntrega(Long id) {
        modoentregarepository.deleteById(id);
    }
    
}
