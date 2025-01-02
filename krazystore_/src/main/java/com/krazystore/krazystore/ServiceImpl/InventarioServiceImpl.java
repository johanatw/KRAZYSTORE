/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.Estado;
import Utils.AjustarInventario;
import Utils.TipoEventoExistencias;
import com.krazystore.krazystore.DTO.DetalleInventarioDTO;
import com.krazystore.krazystore.DTO.InventarioCreationDTO;
import com.krazystore.krazystore.DTO.InventarioDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.CategoriaEntity;
import com.krazystore.krazystore.Entity.DetalleInventario;
import com.krazystore.krazystore.Entity.InventarioEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;
import com.krazystore.krazystore.Repository.InventarioRepository;
import com.krazystore.krazystore.Service.DetalleInventarioService;
import com.krazystore.krazystore.Service.InventarioService;
import java.util.List;
import java.util.Optional;
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
    private DetalleInventarioService detalleService;
    
    @Override
    public List<InventarioDTO> findAll() {
        return inventarioRepository.findAllInventarios();
    }

    @Override
    public Optional<InventarioDTO> findById(Long id) {
        Optional<InventarioDTO> inventario = inventarioRepository.findInventario(id);
        if (inventario.isPresent()) {
            List<DetalleInventarioDTO> detalles = inventarioRepository.findDetallesByIdInventario(id);
            inventario.get().setDetalle(detalles);
        }
        return inventario;
    }

    @Transactional
    @Override
    public InventarioEntity saveInventario(InventarioCreationDTO inventarioDTO) throws Exception {
        List<DetalleInventario> detalle = inventarioDTO.getDetalle();
        InventarioEntity inventario = inventarioDTO.getInventario() ;
   
        inventario.setEstado(Estado.ENCURSO.getCodigo());
        InventarioEntity nuevoInventario = inventarioRepository.save(inventario);
        if(!detalle.isEmpty()){
            detalleService.saveDetInventario(detalle, nuevoInventario.getId());
        }
        
        return nuevoInventario;
    }

    @Transactional
    @Override
    public InventarioEntity updateInventario(InventarioCreationDTO inventarioDTO, Long id) throws Exception {
        List<DetalleInventario> detalle = inventarioDTO.getDetalle();
        InventarioEntity inventario = inventarioDTO.getInventario();
        InventarioEntity updatedInventario = inventarioRepository.findById(id).get();
        //No puede modificar si ya esta finalizado o ajustado
        /*if(PAGADO == updatedCompra.getEstado()){
            throw new BadRequestException("No se puede modificar " );
        }*/
        updatedInventario.setEstado(inventario.getEstado());
        updatedInventario.setFecha(inventario.getFecha());
        inventarioRepository.save(updatedInventario);
        
        detalleService.updateDetInventario(detalle, id);
        
        return updatedInventario;
    }

    @Transactional
    @Override
    public void deleteInventario(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public List<DetalleInventarioDTO> getDetallesInventarioIniciales() {

        return detalleService.getDetallesInventarioIniciales();
    }
    
    @Override
    public InventarioCreationDTO obtenerDetallesCompletos(Long id) {
        InventarioCreationDTO inventarioDTO = new InventarioCreationDTO();
        Optional<InventarioEntity> inventario = inventarioRepository.findById(id);
        if (inventario.isPresent()) {
            List<DetalleInventario> detalles = detalleService.obtenerDetallesCompletos(id);
            List<CategoriaEntity> filtros = inventarioRepository.obtenerFiltros(id);
            inventarioDTO.setInventario(inventario.get());
            inventarioDTO.setDetalle(detalles);
            inventarioDTO.setFiltrosInventario(filtros);
        }
        return inventarioDTO;
        
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
        AjustarInventario evento = new AjustarInventario(productosActualizarExistencias, TipoEventoExistencias.AJUSTAR_INVENTARIO);
        eventPublisher.publishEvent(evento);
    }
    
}
