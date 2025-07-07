/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.TipoAjusteExistencia;
import com.krazystore.krazystore.DTO.DetalleAjusteDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.DetalleAjuste;
import com.krazystore.krazystore.Repository.DetalleAjusteRepository;
import com.krazystore.krazystore.Service.DetalleAjusteService;
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class DetalleAjusteServiceImpl implements DetalleAjusteService {

    public DetalleAjusteServiceImpl(com.krazystore.krazystore.Repository.DetalleAjusteRepository detalleRepository) {
        this.detalleRepository = detalleRepository;
    }
    private final DetalleAjusteRepository detalleRepository;
    

    @Override
    public List<DetalleAjuste> findByIdAjuste(Long id) {
        return detalleRepository.findAllByIdAjuste(id);
    }

    @Transactional
    @Override
    public List<DetalleAjuste> saveDetAjuste(List<DetalleAjuste> detalle, Long idAjuste) throws Exception {
        if(detalle.isEmpty()){
            throw new BadRequestException("Falta Detalle " );
        }
         
        return detalleRepository.saveAll(detalle);
    }

    @Transactional
    @Override
    public List<DetalleAjuste> updateDetAjuste(List<DetalleAjuste> detalle, Long idAjuste) throws Exception {
        if(detalle.isEmpty()){
            throw new BadRequestException("Falta Detalle " );
        }
        List<DetalleAjuste> detallesAnteriores = detalleRepository.findAllByIdAjuste(idAjuste); 
               
        // Procesar cambios en los detalles
        procesarCambiosEnDetalles(detallesAnteriores, detalle);

        return detalleRepository.findAllByIdAjuste(idAjuste);
    }
    
    private void procesarCambiosEnDetalles(List<DetalleAjuste> detallesAnteriores, List<DetalleAjuste> nuevosDetalles) throws Exception {
        procesarEliminaciones(detallesAnteriores, nuevosDetalles);
        procesarModificaciones(detallesAnteriores, nuevosDetalles);
        procesarRegistros(detallesAnteriores, nuevosDetalles);
    }
    
    @Transactional
    private void procesarEliminaciones(List<DetalleAjuste> detallesAnteriores, List<DetalleAjuste> nuevosDetalles)  {
        List<DetalleAjuste> itemsEliminar = getElementosEliminar(detallesAnteriores, nuevosDetalles);

        if (!itemsEliminar.isEmpty()) {
            detalleRepository.deleteAll(itemsEliminar);
        }

    }
    
    @Transactional
    private void procesarModificaciones(List<DetalleAjuste> detallesAnteriores, List<DetalleAjuste> nuevosDetalles) {
        List<DetalleAjuste> itemsModificar = getElementosModificar(detallesAnteriores, nuevosDetalles);

        if (!itemsModificar.isEmpty()) {
            detalleRepository.saveAll(itemsModificar);
        }

    }
    
    @Transactional
    private void procesarRegistros(List<DetalleAjuste> detallesAnteriores, List<DetalleAjuste> nuevosDetalles) {
        List<DetalleAjuste> itemsRegistrar = getElementosRegistrar(detallesAnteriores, nuevosDetalles);

        if (!itemsRegistrar.isEmpty()) {
            detalleRepository.saveAll(itemsRegistrar);
        }

    }
    
    public List<DetalleAjuste> getElementosEliminar (List<DetalleAjuste> anteriorDetalle, List<DetalleAjuste> actualDetalle) {
        List<DetalleAjuste> elementos = new ArrayList<>();
                           
        // Recorre detalle anterior
        anteriorDetalle.forEach(anterior -> {
            // Busca si el producto del detalle anterior existe en el detalle actual
            boolean existe = actualDetalle.stream()
                    .anyMatch(actual -> Objects.equals(actual.getProducto().getId(), anterior.getProducto().getId()));

            // Si no existe en el detalle actual, se intenta eliminar
            if (!existe) {
                /*if (anterior.getCantRecepcionada() > 0) {
                    // Lanza excepción si el producto ya fue facturado
                    throw new BadRequestException("No es posible eliminar el Producto: " + anterior.getProducto().getNombre());
                }*/
                // Si no está facturado, se agrega a la lista de elementos a eliminar
                elementos.add(anterior);
            }
        });
        
        return elementos;
    }
    
    public List<DetalleAjuste> getElementosModificar (List<DetalleAjuste> anteriorDetalle, List<DetalleAjuste> actualDetalle) {
        List<DetalleAjuste> elementos = new ArrayList<>();
                           
        // Recorre detalle anterior
        anteriorDetalle.forEach(anterior -> {
            // Busca si el producto del detalle anterior existe en el detalle actual
            Optional<DetalleAjuste> actual = actualDetalle.stream().filter(act -> Objects.equals(act.getProducto().getId(), anterior.getProducto().getId())).findAny();
            // Si encuentra, y hay diferencia se intenta modificar
            if(actual.isPresent() ){
            
                    // Lanza excepción si el producto ya fue facturado
                    /*if(anterior.getCantRecepcionada() > actual.get().getCantidad()){
                        throw new BadRequestException("No es posible modificar la cantidad del Producto: "+ anterior.getProducto().getNombre());
                    }*/
                    
                    actual.get().setId(anterior.getId());
                    actual.get().setProducto(anterior.getProducto());
                    
                    elementos.add(actual.get());
                
            }

        });
        
        return elementos;
    }
    
    public List<DetalleAjuste> getElementosRegistrar (List<DetalleAjuste> anteriorDetalle, List<DetalleAjuste> actualDetalle) {
        List<DetalleAjuste> elementos = new ArrayList<>();
                           
        // Recorre detalle anterior
        actualDetalle.forEach(actual -> {
            // Busca si el producto del detalle actual no existe en el detalle anterior
            boolean existe = anteriorDetalle.stream()
                    .anyMatch(anterior -> Objects.equals(actual.getProducto().getId(), anterior.getProducto().getId()));

            // Si no existe en el detalle anterior, se intenta registrar
            if (!existe) {          
                elementos.add(actual);
            }
        });
        
        return elementos;
    }
    

    @Override
    public void deleteDetAjuste(Long idInventario) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<DetalleAjusteDTO> obtenerProductosParaAjuste() {
        return detalleRepository.obtenerProductosParaAjuste();
    }

    @Override
    public List<ProductoExistenciasDTO> getProductosActualizarExistencias(Long id) {
        List<DetalleAjuste> detalle = detalleRepository.findAllByIdAjuste(id);
        
        List<ProductoExistenciasDTO> productosActualizarExistencias = new ArrayList<>();
        detalle.forEach(d -> {
            
            if(d.getCantidadAjustada() != 0){
                ProductoExistenciasDTO productoActualizar = new ProductoExistenciasDTO(
                    d.getProducto().getId(),
                    d.getCantidadAjustada(),
                    TipoAjusteExistencia.DISMINUIR
                );
            
                productosActualizarExistencias.add(productoActualizar);
            }
        });
        
        return productosActualizarExistencias;
    }
    
}
