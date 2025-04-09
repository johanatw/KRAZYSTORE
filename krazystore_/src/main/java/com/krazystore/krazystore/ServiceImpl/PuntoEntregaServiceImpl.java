/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Entity.PuntoEntregaEntity;
import com.krazystore.krazystore.Repository.PuntoEntregaRepository;
import com.krazystore.krazystore.Service.PuntoEntregaService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class PuntoEntregaServiceImpl implements PuntoEntregaService{

    public PuntoEntregaServiceImpl(PuntoEntregaRepository puntoEntregaRepository) {
        this.puntoEntregaRepository = puntoEntregaRepository;
    }
    private final PuntoEntregaRepository puntoEntregaRepository;

    @Override
    public List<PuntoEntregaEntity> findAll() {
        return puntoEntregaRepository.findAll();
    }

    @Override
    public Optional<PuntoEntregaEntity> findById(Long id) {
        return puntoEntregaRepository.findById(id);
    }

    @Override
    public PuntoEntregaEntity savePuntoEntrega(PuntoEntregaEntity puntoEntrega) {
        return puntoEntregaRepository.save(puntoEntrega);
    }

    @Override
    public PuntoEntregaEntity updatePuntoEntrega(PuntoEntregaEntity puntoEntrega, Long id) {
         PuntoEntregaEntity updatedPuntoEntrega = puntoEntregaRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Punto de entrega no existe"));
         
         updatedPuntoEntrega.setDescripcion(puntoEntrega.getDescripcion());
         
         return puntoEntregaRepository.save(updatedPuntoEntrega);
    }

    @Override
    public void deletePuntoEntrega(Long id) {
        puntoEntregaRepository.deleteById(id);
    }
}
