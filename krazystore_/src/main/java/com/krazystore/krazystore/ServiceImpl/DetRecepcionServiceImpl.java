/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.TipoAjusteExistencia;
import Utils.TipoOperacionDetalle;
import static Utils.TipoOperacionDetalle.MODIFICAR;
import static Utils.TipoOperacionDetalle.REGISTRAR;
import com.krazystore.krazystore.DTO.DetallePedidoRecepcionDTO;
import com.krazystore.krazystore.DTO.DetalleRecepcionDTO;
import com.krazystore.krazystore.DTO.ProductoActualizarInventarioDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.DetalleRecepcion;
import com.krazystore.krazystore.Entity.ProductoEntity;
import com.krazystore.krazystore.Entity.RecepcionEntity;
import com.krazystore.krazystore.Repository.DetalleRecepcionRepository;
import com.krazystore.krazystore.Service.DetallePedidoCompraService;
import com.krazystore.krazystore.Service.DetalleRecepcionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class DetRecepcionServiceImpl implements DetalleRecepcionService {
    private final DetalleRecepcionRepository detalleRepository;
    
    public DetRecepcionServiceImpl(DetalleRecepcionRepository detalleRepository) {
        this.detalleRepository = detalleRepository;
    }
    
/*
    @Override
    public List<DetalleRecepcion> findByIdRecepcion(Long id) {
        return detalleRepository.findByIdRecepcion(id);
    }
    
    @Override
    public List<DetalleRecepcionDTO> obtenerDetalleFacturaRecepcionar(Long idCompra) {
        return detalleRepository.obtenerDetalleFacturaRecepcionar(idCompra);
    }
    

    

    @Transactional
    @Override
    public List<ProductoExistenciasDTO> updateDetRecepcion(List<DetalleRecepcion> detalle, Long idRecepcion) {
        //traer original
        //traer detallePedido
        //recorrer y poner nueva cantidad si es mayor a 1 y menor a lo solicitado
        //si no error
        //guardar
         List<DetalleRecepcion> detallesAnteriores = detalleRepository.findByIdRecepcion(idRecepcion);
         List<ProductoExistenciasDTO> productosActualizarExistencias = new ArrayList<>();
         // Crear copias de los objetos para evitar que se modifiquen las referencias originales
            List<DetalleRecepcion> anterioresCopias = detallesAnteriores.stream()
                .map(item -> new DetalleRecepcion(item)) // Constructor de copia
                .collect(Collectors.toList());

        // Procesar cambios en los detalles
        procesarCambiosEnDetalles(detallesAnteriores, detalle);
        
        productosActualizarExistencias = calcularProductosExistencias(anterioresCopias, detalle);

        // Calcular productos a actualizar existencias
        return productosActualizarExistencias;
         
         // Procesar modificaciones
        //procesarModificaciones(detallesAnteriores, detalle);
        
        // Retornar los detalles del pedido actualizados
        //return detalleRepository.findByIdRecepcion(idRecepcion);
    }
    
    private void procesarCambiosEnDetalles(List<DetalleRecepcion> detallesAnteriores, List<DetalleRecepcion> nuevosDetalles){
        procesarModificaciones(detallesAnteriores, nuevosDetalles);
    }
    

    
    
    private void procesarModificaciones(List<DetalleRecepcion> detallesAnteriores, List<DetalleRecepcion> nuevosDetalles) {
        List<DetalleRecepcion> itemsModificar = getElementosModificar(detallesAnteriores, nuevosDetalles);

        if (!itemsModificar.isEmpty()) {
            detalleRepository.saveAll(itemsModificar);
        }

    }
    
    public List<DetalleRecepcion> getElementosModificar (List<DetalleRecepcion> anteriorDetalle, List<DetalleRecepcion> actualDetalle) {
        List<DetalleRecepcion> elementos = new ArrayList<>();
                           
        // Recorre detalle anterior
        anteriorDetalle.forEach(anterior -> {
            // Busca si el producto del detalle anterior existe en el detalle actual
            Optional<DetalleRecepcion> actual = actualDetalle.stream().filter(act -> Objects.equals(act.getDetalleCompra().getProducto().getId(), anterior.getDetalleCompra().getProducto().getId())).findAny();
            // Si encuentra, y hay diferencia se intenta modificar
            if(actual.isPresent() ){
                if(actual.get().getCantRecepcionada() > 0 ){
                    
                    actual.get().setId(anterior.getId());
                    actual.get().setCantAceptada(actual.get().getCantRecepcionada()-actual.get().getCantRechazada());
                    elementos.add(actual.get());
                }
            }

        });
        
        return elementos;
    }
    
    private List<ProductoExistenciasDTO> calcularProductosExistencias(List<DetalleRecepcion> detallesAnteriores, List<DetalleRecepcion> nuevosDetalles) {
        List<ProductoExistenciasDTO> productosExistencias = new ArrayList<>();

        // Agregar productos de modificaciones
        productosExistencias.addAll(getProductosExistencias(detallesAnteriores, nuevosDetalles, TipoOperacionDetalle.MODIFICAR));

        return productosExistencias;
    }
    
    private List<ProductoExistenciasDTO> getProductosExistencias(List<DetalleRecepcion> detallesAnteriores, List<DetalleRecepcion> nuevosDetalles, TipoOperacionDetalle tipoOperacion){
        List<ProductoExistenciasDTO> productosExistencias = new ArrayList<>();
        Map<Long, Integer> mapaCantidadAnterior = construirMapaCantidadAnterior(detallesAnteriores);

        List<DetalleRecepcion> detallesProcesar = switch (tipoOperacion) {
            case REGISTRAR -> null;
            case MODIFICAR -> getElementosModificar(detallesAnteriores, nuevosDetalles);
            case ELIMINAR -> null;
        };
                
        for (DetalleRecepcion detalle : detallesProcesar) {
            Long idProducto = detalle.getDetalleCompra().getProducto().getId();
            
            int cantidadAnterior = (tipoOperacion == TipoOperacionDetalle.MODIFICAR ) ? mapaCantidadAnterior.getOrDefault(idProducto, 0): 0;
            int cantidadNueva = detalle.getCantAceptada();
            
            ProductoExistenciasDTO productoActualizar = new ProductoExistenciasDTO();
            productoActualizar.setIdProducto(idProducto);
            productoActualizar.setCantidad(cantidadNueva - cantidadAnterior);
            productoActualizar.setAccion(TipoAjusteExistencia.INCREMENTAR);
            productosExistencias.add(productoActualizar);
        }

        return productosExistencias;
    }
    
    private Map<Long, Integer> construirMapaCantidadAnterior(List<DetalleRecepcion> detallesAnteriores) {
        return detallesAnteriores.stream()
                .collect(Collectors.toMap(
                    detalle -> detalle.getDetalleCompra().getProducto().getId(),
                    detalle -> detalle.getCantRecepcionada() - detalle.getCantRechazada()
                ));
    }

    

    @Override
    public List<DetalleRecepcionDTO> obtenerDetallesFacturasRecepcionar(List<Long> ids) {
        List<DetalleRecepcionDTO> detalleRecepcionar = detalleRepository.obtenerDetallesFacturasRecepcionar(ids).stream()
                    .filter(d -> d.getDetalleCompra().getCantidad()>d.getDetalleCompra().getCantRecepcionada()) // condición
                    .collect(Collectors.toList());
        return detalleRecepcionar;
    }

  */

    @Override
    public List<DetalleRecepcionDTO> findDetalleByIdRecepcion(Long idRecepcion) {
        return detalleRepository.findDetalleByIdRecepcion(idRecepcion);
    }
    
    public List<DetalleRecepcionDTO> obtenerDetallesFacturasRecepcionar(List<Long> ids) {
        List<DetalleRecepcionDTO> detalleRecepcionar = detalleRepository.obtenerDetallesFacturasRecepcionar(ids).stream()
                    .filter(d -> d.getDetalleCompra().getCantPendiente() >0) // condición
                    .collect(Collectors.toList());
        return detalleRecepcionar;
    }
    
    @Transactional
    @Override
    public List<ProductoExistenciasDTO> saveDetRecepcion(List<DetalleRecepcion> detalle, Long idRecepcion) {
        List<DetalleRecepcion> detalleGuardar = new ArrayList<>();
        List<ProductoExistenciasDTO> productosActualizarExistencias = new ArrayList<>();
        detalle.forEach(d -> {
            
            if(d.getCantRecepcionada() > 0){
                System.out.println(d.getCantRecepcionada());
                System.out.println(d.getCantAceptada());
                System.out.println(d.getCantRechazada());
                d.setCantAceptada(d.getCantRecepcionada() - d.getCantRechazada());
                detalleGuardar.add(d);
                
                ProductoExistenciasDTO productoActualizar = new ProductoExistenciasDTO(
                   d.getDetalleCompra().getProducto().getId(),
                    d.getCantAceptada(),
                    TipoAjusteExistencia.INCREMENTAR
                );
            
                productosActualizarExistencias.add(productoActualizar);
            }
        });
        
        if (!detalleGuardar.isEmpty()) {
            detalleRepository.saveAll(detalleGuardar);
        }
        
        return productosActualizarExistencias;
       
    }
    
    @Override
    public List<ProductoExistenciasDTO> deleteDetRecepcion(Long idRecepcion) {
        List<DetalleRecepcion> detalle = detalleRepository.findByIdRecepcion(idRecepcion);
        List<ProductoExistenciasDTO> productosActualizarExistencias = new ArrayList<>();
         
        detalle.forEach(d -> {
                ProductoExistenciasDTO productoActualizar = new ProductoExistenciasDTO(
                   d.getDetalleCompra().getProducto().getId(),
                    d.getCantAceptada(),
                    TipoAjusteExistencia.DISMINUIR
                );
            
                productosActualizarExistencias.add(productoActualizar);
            
        });
        
        detalleRepository.deleteAllByIdRecepcion(idRecepcion);
        return productosActualizarExistencias;
    }
    
}
