/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.MedioPagoEntity;
import com.krazystore.krazystore.Repository.MedioPagoRepository;
import com.krazystore.krazystore.Service.MedioPagoService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class MedioPagoServiceImpl implements MedioPagoService {

    public MedioPagoServiceImpl(MedioPagoRepository medioPagoRepository) {
        this.medioPagoRepository = medioPagoRepository;
    }
    private final MedioPagoRepository medioPagoRepository;

    @Override
    public List<MedioPagoEntity> findAll() {
        return medioPagoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Optional<MedioPagoEntity> findById(Long id) {
        return medioPagoRepository.findById(id);
    }

    @Override
    public MedioPagoEntity saveMedioPago(MedioPagoEntity medioPagoEntity) {
        return medioPagoRepository.save(medioPagoEntity);
    }

    @Override
    public MedioPagoEntity updateMedioPago(MedioPagoEntity medioPagoEntity, Long id) {
        MedioPagoEntity updatedMedio = medioPagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medio de pago no existe"));
        
        updatedMedio.setDescripcion(medioPagoEntity.getDescripcion());
        return medioPagoRepository.save(updatedMedio);
    }

    @Override
    public void deleteMedioPago(Long id) {
        MedioPagoEntity deletedMedio = medioPagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medio de pago no existe"));
        
        deletedMedio.setActivo(Boolean.FALSE);
        medioPagoRepository.save(deletedMedio);
    }
    
}
