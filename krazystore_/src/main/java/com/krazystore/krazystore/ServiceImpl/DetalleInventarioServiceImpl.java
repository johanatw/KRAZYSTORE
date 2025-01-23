/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.TipoAjusteExistencia;
import com.krazystore.krazystore.DTO.DetalleInventarioDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.DetalleInventario;
import com.krazystore.krazystore.Entity.InventarioEntity;
import com.krazystore.krazystore.Repository.DetalleInventarioRepository;
import com.krazystore.krazystore.Service.DetalleInventarioService;
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
public class DetalleInventarioServiceImpl implements DetalleInventarioService{
    private final DetalleInventarioRepository detalleRepository;

    public DetalleInventarioServiceImpl(DetalleInventarioRepository detalleRepository) {
        this.detalleRepository = detalleRepository;
    }

    @Transactional
    @Override
    public List<DetalleInventario> findByIdInventario(Long id) {
        return detalleRepository.findAllByIdInventario(id);
    }

    @Transactional
    @Override
    public List<DetalleInventario> saveDetInventario(List<DetalleInventario> detalle, Long idInventario) throws Exception {
        if(detalle.isEmpty()){
            throw new BadRequestException("Falta Detalle " );
        }
 
        detalle.forEach(d -> {
            InventarioEntity inventario = new InventarioEntity();
            inventario.setId(idInventario);
            d.setInventario(inventario);
        });
        
        return detalleRepository.saveAll(detalle);
    }

    @Transactional
    @Override
    public List<DetalleInventario> updateDetInventario(List<DetalleInventario> detalle, Long idInventario) throws Exception {
        if(detalle.isEmpty()){
            throw new BadRequestException("Falta Detalle " );
        }
        List<DetalleInventario> detallesAnteriores = detalleRepository.findAllByIdInventario(idInventario); 
               
        // Procesar cambios en los detalles
        procesarCambiosEnDetalles(detallesAnteriores, detalle);

        return detalleRepository.findAllByIdInventario(idInventario);
    }

    @Transactional
    @Override
    public void deleteDetInventario(Long idInventario) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    private void procesarCambiosEnDetalles(List<DetalleInventario> detallesAnteriores, List<DetalleInventario> nuevosDetalles) throws Exception {
        procesarEliminaciones(detallesAnteriores, nuevosDetalles);
        procesarModificaciones(detallesAnteriores, nuevosDetalles);
        procesarRegistros(detallesAnteriores, nuevosDetalles);
    }
    
    @Transactional
    private void procesarEliminaciones(List<DetalleInventario> detallesAnteriores, List<DetalleInventario> nuevosDetalles)  {
        List<DetalleInventario> itemsEliminar = getElementosEliminar(detallesAnteriores, nuevosDetalles);

        if (!itemsEliminar.isEmpty()) {
            detalleRepository.deleteAll(itemsEliminar);
        }

    }
    
    @Transactional
    private void procesarModificaciones(List<DetalleInventario> detallesAnteriores, List<DetalleInventario> nuevosDetalles) {
        List<DetalleInventario> itemsModificar = getElementosModificar(detallesAnteriores, nuevosDetalles);

        if (!itemsModificar.isEmpty()) {
            detalleRepository.saveAll(itemsModificar);
        }

    }
    
    @Transactional
    private void procesarRegistros(List<DetalleInventario> detallesAnteriores, List<DetalleInventario> nuevosDetalles) {
        List<DetalleInventario> itemsRegistrar = getElementosRegistrar(detallesAnteriores, nuevosDetalles);

        if (!itemsRegistrar.isEmpty()) {
            detalleRepository.saveAll(itemsRegistrar);
        }

    }
    
    public List<DetalleInventario> getElementosEliminar (List<DetalleInventario> anteriorDetalle, List<DetalleInventario> actualDetalle) {
        List<DetalleInventario> elementos = new ArrayList<>();
                           
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
    
    public List<DetalleInventario> getElementosModificar (List<DetalleInventario> anteriorDetalle, List<DetalleInventario> actualDetalle) {
        List<DetalleInventario> elementos = new ArrayList<>();
                           
        // Recorre detalle anterior
        anteriorDetalle.forEach(anterior -> {
            // Busca si el producto del detalle anterior existe en el detalle actual
            Optional<DetalleInventario> actual = actualDetalle.stream().filter(act -> Objects.equals(act.getProducto().getId(), anterior.getProducto().getId())).findAny();
            // Si encuentra, y hay diferencia se intenta modificar
            if(actual.isPresent() ){
                if(actual.get().getCantContada() != anterior.getCantContada()){
                    // Lanza excepción si el producto ya fue facturado
                    /*if(anterior.getCantRecepcionada() > actual.get().getCantidad()){
                        throw new BadRequestException("No es posible modificar la cantidad del Producto: "+ anterior.getProducto().getNombre());
                    }*/
                    
                    actual.get().setId(anterior.getId());
                    
                    elementos.add(actual.get());
                }
            }

        });
        
        return elementos;
    }
    
    public List<DetalleInventario> getElementosRegistrar (List<DetalleInventario> anteriorDetalle, List<DetalleInventario> actualDetalle) {
        List<DetalleInventario> elementos = new ArrayList<>();
                           
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
    public List<DetalleInventario> obtenerDetallesCompletos(Long id) {
        System.out.println("obtenercompleto");
        detalleRepository.obtenerDetallesCompletos(id).forEach(d -> {
            System.out.println(d.getProducto().getNombre());
        });
        return detalleRepository.obtenerDetallesCompletos(id);
    }
    
    @Override
    public List<ProductoExistenciasDTO> getProductosActualizarExistencias(Long id) {
        List<DetalleInventario> detalle = detalleRepository.findAllByIdInventario(id);
        
        List<ProductoExistenciasDTO> productosActualizarExistencias = new ArrayList<>();
        detalle.forEach(d -> {
            
            if(d.getDiferencia() != 0){
                ProductoExistenciasDTO productoActualizar = new ProductoExistenciasDTO(
                    d.getProducto().getId(),
                    d.getDiferencia(),
                    TipoAjusteExistencia.INCREMENTAR
                );
            
                productosActualizarExistencias.add(productoActualizar);
            }
        });
        
        return productosActualizarExistencias;

    }

    @Override
    public List<DetalleInventario> getDetallesInventarioIniciales() {
       return detalleRepository.getDetallesInventarioIniciales();
    }
    
}
