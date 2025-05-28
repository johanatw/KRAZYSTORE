/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.DTO.DetalleEntregaDTO;
import com.krazystore.krazystore.Entity.DetalleEntrega;
import com.krazystore.krazystore.Repository.DetalleEntregaRepository;
import com.krazystore.krazystore.Service.DetalleEntregaService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class DetalleEntregaServiceImpl implements DetalleEntregaService {

    public DetalleEntregaServiceImpl(DetalleEntregaRepository detalleEntregaRepository) {
        this.detalleEntregaRepository = detalleEntregaRepository;
    }
    private final DetalleEntregaRepository detalleEntregaRepository;

    @Override
    public List<DetalleEntregaDTO> findByIdEntrega(Long id) {
        return detalleEntregaRepository.findDetallesByIdEntrega(id);
    }
    
    @Transactional
    @Override
    public List<DetalleEntrega> saveDetalle(List<DetalleEntrega> detalle) {       
        return detalleEntregaRepository.saveAll(detalle);
       
    }

    @Transactional
    @Override
    public void deleteByIdEntrega(Long id) {
        detalleEntregaRepository.deleteByIdEntrega(id);
    }
}
