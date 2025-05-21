/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.Estado;
import Utils.AjustarInventario;
import Utils.TipoEvento;
import com.krazystore.krazystore.DTO.DetalleInventarioDTO;
import com.krazystore.krazystore.DTO.InventarioCreationDTO;
import com.krazystore.krazystore.DTO.InventarioDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.CategoriaEntity;
import com.krazystore.krazystore.Entity.DetalleInventario;
import com.krazystore.krazystore.Entity.InventarioEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;
import com.krazystore.krazystore.Mapper.DetalleInventarioDTOMapper;
import com.krazystore.krazystore.Mapper.DetalleInventarioMapper;
import com.krazystore.krazystore.Repository.InventarioRepository;
import com.krazystore.krazystore.Service.DetalleInventarioService;
import com.krazystore.krazystore.Service.InventarioService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class InventarioServiceImpl implements InventarioService {
    private final InventarioRepository inventarioRepository;
    private final ApplicationEventPublisher eventPublisher;

    public InventarioServiceImpl(InventarioRepository inventarioRepository, ApplicationEventPublisher eventPublisher) {
        this.inventarioRepository = inventarioRepository;
        this.eventPublisher = eventPublisher;
    }
    
    @Autowired
    private DetalleInventarioDTOMapper detalleDTOMapper;
    
    @Autowired
    private DetalleInventarioMapper detalleMapper;
    
    @Autowired
    private DetalleInventarioService detalleService;
    
    @Override
    public List<InventarioDTO> findAll() {
        return inventarioRepository.findAllInventarios();
    }

    @Override
    public InventarioCreationDTO findById(Long id) {
        Optional<InventarioEntity> inventario = inventarioRepository.findById(id);
        InventarioCreationDTO inventarioDTO = new InventarioCreationDTO();
        if (inventario.isPresent()) {
            List<DetalleInventarioDTO> detalles = detalleService.findByIdInventario(id)
                .stream()
                .map(detalleDTOMapper)
                .collect(Collectors.toList());
            
            inventarioDTO.setDetalle(detalles);
            inventarioDTO.setInventario(inventario.get());
        }
        return inventarioDTO;
    }

    @Transactional
    @Override
    public InventarioEntity saveInventario(InventarioCreationDTO inventarioDTO) throws Exception {
        List<DetalleInventarioDTO> detalleDTO = inventarioDTO.getDetalle();
        InventarioEntity inventario = inventarioDTO.getInventario() ;
   
        inventario.setEstado(Estado.ENCURSO.getCodigo());
        InventarioEntity nuevoInventario = inventarioRepository.save(inventario);
        if(!detalleDTO.isEmpty()){
            detalleDTO.forEach(det -> det.setStockInicialInventario(det.getStockActual()));
            List<DetalleInventario> detalle = detalleDTO
                .stream()
                .map(detalleMapper)
                .collect(Collectors.toList());
            
            // Establecer la relación bidireccional si es necesario
            detalle.forEach(det -> det.setInventario(nuevoInventario));
            
            detalleService.saveDetInventario(detalle, nuevoInventario.getId());
        }
        
        return nuevoInventario;
    }

    @Transactional
    @Override
    public InventarioEntity updateInventario(InventarioCreationDTO inventarioDTO, Long id) throws Exception {
        // Buscar el inventario existente o lanzar excepción
        InventarioEntity inventario = inventarioRepository.findById(id)
            .orElseThrow(() -> new Exception("El inventario con ID " + id + " no existe"));
        
        // Actualizar campos del inventario
        inventario.setFecha(inventarioDTO.getInventario().getFecha());
        inventarioRepository.save(inventario);
        
        // Actualizar detalles si existen
        List<DetalleInventarioDTO> detalleDTO = inventarioDTO.getDetalle();
        if(detalleDTO != null && !detalleDTO.isEmpty()){
            List<DetalleInventario> detalle = detalleDTO
                .stream()
                .map(detalleMapper)
                .collect(Collectors.toList());
            
            // Establecer la relación bidireccional si es necesario
            detalle.forEach(det -> det.setInventario(inventario));
            
            detalleService.updateDetInventario(detalle, id);
        }
        
        return inventario;
    }

    @Transactional
    @Override
    public void deleteInventario(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    @Transactional
    @Override
    public InventarioEntity ajustarInventario(Long id) {
  
        Optional<InventarioEntity> inventario = inventarioRepository.findById(id) ;
        if (inventario.isPresent()) {
            List<ProductoExistenciasDTO> productosActualizarExistencias = detalleService.getProductosActualizarExistencias(id);
            actualizarExistencias(productosActualizarExistencias);
            inventario.get().setEstado('A');
            return inventarioRepository.save(inventario.get());
        }
        return null;
    }
    
    public void actualizarExistencias(List<ProductoExistenciasDTO> productosActualizarExistencias) {
        // Publicar el evento
        AjustarInventario evento = new AjustarInventario(productosActualizarExistencias, TipoEvento.AJUSTAR_INVENTARIO);
        eventPublisher.publishEvent(evento);
    }

    @Override
    public List<DetalleInventarioDTO> getDetallesInventarioIniciales(List<Long> ids) {
        return detalleService.getDetallesInventarioIniciales(ids)
                .stream()
                .map(detalleDTOMapper)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public InventarioEntity finalizarInventario(InventarioCreationDTO inventarioDTO, Long id) throws Exception {
        InventarioEntity inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inventario no encontrado"));
        
        inventario.setEstado(Estado.PENDIENTEAJUSTE.getCodigo());
        inventarioRepository.save(inventario);
         // Actualizar detalles si existen
        List<DetalleInventarioDTO> detalleDTO = inventarioDTO.getDetalle();
        if(detalleDTO != null && !detalleDTO.isEmpty()){
            detalleDTO.forEach(det -> det.setStockInicialInventario(det.getStockActual()));
            List<DetalleInventario> detalle = detalleDTO
                .stream()
                .map(detalleMapper)
                .collect(Collectors.toList());
            
        // Establecer la relación bidireccional si es necesario
        detalle.forEach(det -> System.out.println(det.getDiferencia()));
        detalle.forEach(det -> det.setInventario(inventario));

        detalleService.updateDetInventario(detalle, id);
        
        }
        return inventario;
    }
    
}
