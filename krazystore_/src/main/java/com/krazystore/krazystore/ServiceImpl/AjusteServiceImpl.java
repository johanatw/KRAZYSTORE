/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.AjustarInventario;
import Utils.Estado;
import Utils.TipoEvento;
import com.krazystore.krazystore.DTO.AjusteCreationDTO;
import com.krazystore.krazystore.DTO.DetalleAjusteDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.AjusteStock;
import com.krazystore.krazystore.Entity.DetalleAjuste;
import com.krazystore.krazystore.Mapper.DetalleAjusteDTOMapper;
import com.krazystore.krazystore.Mapper.DetalleAjusteMapper;
import com.krazystore.krazystore.Repository.AjusteRepository;
import com.krazystore.krazystore.Service.AjusteService;
import com.krazystore.krazystore.Service.DetalleAjusteService;
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
public class AjusteServiceImpl implements AjusteService {
    private final AjusteRepository ajusteRepository;
    private final ApplicationEventPublisher eventPublisher;

    public AjusteServiceImpl(AjusteRepository ajusteRepository, ApplicationEventPublisher eventPublisher) {
        this.ajusteRepository = ajusteRepository;
        this.eventPublisher = eventPublisher;
    }
    
    @Autowired
    private DetalleAjusteService detalleService;
    
    @Autowired
    private DetalleAjusteDTOMapper detalleDTOMapper;
    
    @Autowired
    private DetalleAjusteMapper detalleMapper;

    @Override
    public List<AjusteStock> findAll() {
        return ajusteRepository.findAll();
    }

    @Override
    public AjusteCreationDTO findById(Long id) {
        AjusteCreationDTO ajusteDTO = new AjusteCreationDTO();
        Optional<AjusteStock> ajuste = ajusteRepository.findById(id);
        if (ajuste.isPresent()) {
            List<DetalleAjusteDTO> detalles = detalleService.findByIdAjuste(id)
                .stream()
                .map(detalleDTOMapper)
                .collect(Collectors.toList());
            
            ajusteDTO.setAjuste(ajuste.get());
            ajusteDTO.setDetalle(detalles);
        }
        return ajusteDTO;
    }

    @Transactional
    @Override
    public AjusteStock saveAjuste(AjusteCreationDTO ajusteDTO) throws Exception {
        List<DetalleAjusteDTO> detalleDTO = ajusteDTO.getDetalle();
        AjusteStock ajuste = ajusteDTO.getAjuste();

        // Guardar el ajuste
        ajuste.setEstado(Estado.PENDIENTEAJUSTE.getCodigo());
        AjusteStock nuevoAjuste = ajusteRepository.save(ajuste);
        
        detalleDTO.forEach(det -> System.out.println(det.getProducto()));
        // Guardar los detalles si existen
        if(!detalleDTO.isEmpty()){
            List<DetalleAjuste> detalle = detalleDTO
                .stream()
                .map(detalleMapper)
                .collect(Collectors.toList());
            
            // Establecer la relación bidireccional si es necesario
            detalle.forEach(det -> det.setAjuste(nuevoAjuste));
            
            // Guardar los detalles
            detalleService.saveDetAjuste(detalle, nuevoAjuste.getId());
        }
        
        return nuevoAjuste;
    }

    @Transactional
    @Override
    public AjusteStock updateAjuste(AjusteCreationDTO ajusteDTO, Long id) throws Exception {
        // Buscar el ajuste existente o lanzar excepción
        AjusteStock ajuste = ajusteRepository.findById(id)
            .orElseThrow(() -> new Exception("El ajuste con ID " + id + " no existe"));

        // Actualizar campos del ajuste
        ajuste.setFecha(ajusteDTO.getAjuste().getFecha());
        ajuste.setObservaciones(ajusteDTO.getAjuste().getObservaciones());
        ajusteRepository.save(ajuste);

        // Actualizar detalles si existen
        List<DetalleAjusteDTO> detalleDTO = ajusteDTO.getDetalle();
        if (detalleDTO != null && !detalleDTO.isEmpty()) {
            List<DetalleAjuste> detalle = detalleDTO
                .stream()
                .map(detalleMapper) // Convertir DTO a entidad
                .collect(Collectors.toList());

            // Establecer la relación bidireccional si es necesario
            detalle.forEach(det -> det.setAjuste(ajuste));

            // Actualizar los detalles en el servicio
            detalleService.updateDetAjuste(detalle, id);
        }

        return ajuste;
    
    }

    @Override
    public void deleteAjuste(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DetalleAjusteDTO> obtenerProductosParaAjuste() {
        return detalleService.obtenerProductosParaAjuste();
    }

    @Transactional
    @Override
    public AjusteStock ajustar(Long id) {
        Optional<AjusteStock> ajuste = ajusteRepository.findById(id);
        if (ajuste.isPresent()) {
            List<ProductoExistenciasDTO> productosActualizarExistencias = detalleService.getProductosActualizarExistencias(id);
            actualizarExistencias(productosActualizarExistencias);
            ajuste.get().setEstado(Estado.AJUSTADO.getCodigo());
            return ajusteRepository.save(ajuste.get());
        }
        return null;
    }
    
    public void actualizarExistencias(List<ProductoExistenciasDTO> productosActualizarExistencias) {
        // Publicar el evento
        AjustarInventario evento = new AjustarInventario(productosActualizarExistencias, TipoEvento.AJUSTAR_INVENTARIO);
        eventPublisher.publishEvent(evento);
    }
}
