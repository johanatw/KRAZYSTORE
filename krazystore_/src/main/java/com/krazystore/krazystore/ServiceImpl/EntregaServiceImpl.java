/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.Estado;
import com.krazystore.krazystore.DTO.DetalleEntregaDTO;
import com.krazystore.krazystore.DTO.EntregaCreationDTO;
import com.krazystore.krazystore.Entity.DetalleEntrega;
import com.krazystore.krazystore.Entity.EntregaEntity;
import com.krazystore.krazystore.Mapper.DetalleEntregaMapper;
import com.krazystore.krazystore.Repository.EntregaRepository;
import com.krazystore.krazystore.Service.DetalleEntregaService;
import com.krazystore.krazystore.Service.EntregaService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class EntregaServiceImpl implements EntregaService {

    public EntregaServiceImpl(EntregaRepository entregaRepository) {
        this.entregaRepository = entregaRepository;
    }
    private final EntregaRepository entregaRepository;
    
    @Autowired
    private DetalleEntregaMapper detalleEntregaMapper;
    
    @Autowired
    private DetalleEntregaService detalleService;

    @Override
    public List<EntregaEntity> findAll() {
        return entregaRepository.findAll();
    }

    @Override
    public EntregaCreationDTO findById(Long id) {
        EntregaEntity entrega = entregaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));
        
        List<DetalleEntregaDTO> detalle = detalleService.findByIdEntrega(id);
        
        EntregaCreationDTO entregaDTO = new EntregaCreationDTO();
        entregaDTO.setEntrega(entrega);
        entregaDTO.setDetalle(detalle);
        return entregaDTO;
    }

    @Override
    public EntregaEntity saveEntrega(EntregaCreationDTO entregaDTO) {
        
        entregaDTO.getEntrega().setEstado(Estado.PENDIENTE.getCodigo());
        
        entregaRepository.save(entregaDTO.getEntrega());
        
        
        List<DetalleEntrega> detalle = entregaDTO.getDetalle()
                .stream()
                .map(detalleEntregaMapper)
                .collect(Collectors.toList());
        
        detalle.forEach(det -> det.setEntrega(entregaDTO.getEntrega()));
        
        detalleService.saveDetalle(detalle);
        
        return entregaDTO.getEntrega();
    }

    @Override
    public EntregaEntity updateEntrega(EntregaCreationDTO entrega, Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteEntrega(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
