/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.DTO.DetallePedidoRecepcionDTO;
import com.krazystore.krazystore.DTO.ProductoActualizarInventarioDTO;
import com.krazystore.krazystore.Entity.DetalleRecepcion;
import com.krazystore.krazystore.Entity.RecepcionEntity;
import com.krazystore.krazystore.Repository.DetalleRecepcionRepository;
import com.krazystore.krazystore.Service.DetallePedidoCompraService;
import com.krazystore.krazystore.Service.DetalleRecepcionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    

    @Override
    public List<DetalleRecepcion> findByIdRecepcion(Long id) {
        return detalleRepository.findByIdRecepcion(id);
    }

    @Transactional
    @Override
    public List<DetalleRecepcion> saveDetRecepcion(List<DetalleRecepcion> detalle, Long idRecepcion) {
        List<DetalleRecepcion> detalleGuardar = new ArrayList<>();
        detalle.forEach(d -> {
            RecepcionEntity recepcion = new RecepcionEntity();
            recepcion.setId(idRecepcion);
            d.setRecepcion(recepcion);
            if(d.getCantRecepcionada() > 0){
                detalleGuardar.add(d);
            }
        });
        
        detalleGuardar.forEach(d -> {
            System.out.println(d.getId());
            System.out.println(d.getCantRecepcionada());
        });
        
        if (!detalleGuardar.isEmpty()) {
            detalleRepository.saveAll(detalleGuardar);
        }
        
        return detalleGuardar;
    }

    @Override
    public List<DetalleRecepcion> updateDetRecepcion(List<DetalleRecepcion> detalle, Long idRecepcion) {
        //traer original
        //traer detallePedido
        //recorrer y poner nueva cantidad si es mayor a 1 y menor a lo solicitado
        //si no error
        //guardar
         List<DetalleRecepcion> detallesAnteriores = detalleRepository.findByIdRecepcion(idRecepcion);
         
         // Procesar modificaciones
        procesarModificaciones(detallesAnteriores, detalle);
        
        // Retornar los detalles del pedido actualizados
        return detalleRepository.findByIdRecepcion(idRecepcion);
    }

    @Override
    public void deleteDetRecepcion(Long idRecepcion) {
        detalleRepository.deleteAllByIdRecepcion(idRecepcion);
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
            Optional<DetalleRecepcion> actual = actualDetalle.stream().filter(act -> Objects.equals(act.getDetallePedido().getProducto().getId(), anterior.getDetallePedido().getProducto().getId())).findAny();
            // Si encuentra, y hay diferencia se intenta modificar
            if(actual.isPresent() ){
                if(actual.get().getCantRecepcionada() > 0){
                    // Lanza excepción si el producto ya fue facturado
                    /*if(anterior.getCantRecepcionada() > actual.get().getCantidad()){
                        throw new BadRequestException("No es posible modificar la cantidad del Producto: "+ anterior.getProducto().getNombre());
                    }*/
                    
                    actual.get().setId(anterior.getId());
                    actual.get().setRecepcion(anterior.getRecepcion());
                    
                    elementos.add(actual.get());
                }
            }

        });
        
        return elementos;
    }
    
    
}
