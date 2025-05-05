/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.MotivoAjusteEntity;
import com.krazystore.krazystore.Repository.MotivoAjusteRepository;
import com.krazystore.krazystore.Service.MotivoAjusteService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class MotivoAjusteServiceImpl implements MotivoAjusteService {
    private final MotivoAjusteRepository motivoRepository;

    public MotivoAjusteServiceImpl(MotivoAjusteRepository motivoRepository) {
        this.motivoRepository = motivoRepository;
    }

    @Override
    public List<MotivoAjusteEntity> findAll() {
        return motivoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Optional<MotivoAjusteEntity> findById(Long id) {
        return motivoRepository.findById(id);
    }

    @Override
    public MotivoAjusteEntity saveMotivoAjuste(MotivoAjusteEntity motivoEntity) {
        return motivoRepository.save(motivoEntity);
    }

    @Override
    public MotivoAjusteEntity updateMotivoAjuste(MotivoAjusteEntity motivoEntity, Long id) {
        MotivoAjusteEntity updatedMotivo = motivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Motivo no encontrado"));
        
        updatedMotivo.setDescripcion(motivoEntity.getDescripcion());
        
        return motivoRepository.save(updatedMotivo);
    }

    @Override
    public void deleteMotivoAjuste(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
