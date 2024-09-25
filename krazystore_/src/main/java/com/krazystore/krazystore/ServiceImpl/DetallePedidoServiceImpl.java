/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.DTO.DetallePedidoCreationRequest;
import com.krazystore.krazystore.DTO.DetallePedidoDTO;
import com.krazystore.krazystore.Entity.CategoriaEntity;

import com.krazystore.krazystore.Entity.DetallePedidoEntity;
import com.krazystore.krazystore.Entity.DetalleVentaEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;
import com.krazystore.krazystore.Repository.DetallePedidoRepository;
import com.krazystore.krazystore.Service.AnticipoService;

import com.krazystore.krazystore.Service.DetallePedidoService;
import com.krazystore.krazystore.Service.ProductoService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class DetallePedidoServiceImpl implements DetallePedidoService{
    private final DetallePedidoRepository detallepedidorepository;
   
    
    @Autowired
    private ProductoService productoService;
    
    
    public DetallePedidoServiceImpl(DetallePedidoRepository detallepedidorepository) {
        this.detallepedidorepository = detallepedidorepository;
    }

    @Override
    public List<DetallePedidoEntity> findAll() {
        return detallepedidorepository.findAll();
    }

    @Override
    public Optional<DetallePedidoEntity> findById(Long id) {
        return detallepedidorepository.findById(id);
    }
    
     @Override
    public List<DetallePedidoDTO> findByNroPedido(Long idPedido) {
        //return detallepedidorepository.findByNroPedido(idPedido);
        return detallepedidorepository.findPedido(idPedido);
    }
    
    @Override
    public List<DetallePedidoEntity> findByPedido(Long idPedido) {
        return detallepedidorepository.findByNroPedido(idPedido);
    }
    
    @Transactional
    @Override
    public Iterable<DetallePedidoEntity> saveDetallePedido(Long idPedido, List<DetallePedidoEntity> detalles) {
        System.out.println("entraaca");
        List<ProductoEntity> productosActualizar = new ArrayList<>();
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(idPedido);
        detalles.forEach(d -> {
            d.setPedido(pedido);
            d.setSaldoPendiente(d.getSubtotal());
            productosActualizar.add(d.getProducto());
        });
        
        productoService.updateExistencias(productosActualizar);
        return detallepedidorepository.saveAll(detalles);
    }



    @Transactional
    @Override
    public Iterable<DetallePedidoEntity> updateDetallesPedido(List<DetallePedidoEntity> nuevosDetalles, Long id) throws Exception {
        List<DetallePedidoEntity> detallesAnteriores = detallepedidorepository.findByNroPedido(id); 

        // Procesar eliminaciones
        List<ProductoEntity> productosActualizarExistencias = new ArrayList<>();
        productosActualizarExistencias.addAll(procesarEliminaciones(detallesAnteriores, nuevosDetalles));

        // Procesar modificaciones
        productosActualizarExistencias.addAll(procesarModificaciones(detallesAnteriores, nuevosDetalles));

        // Procesar registros
        productosActualizarExistencias.addAll(procesarRegistros(detallesAnteriores, nuevosDetalles));

        // Actualizar existencias de productos
        if (!productosActualizarExistencias.isEmpty()) {
            productoService.updateExistencias(productosActualizarExistencias);
        }

        // Retornar los detalles del pedido actualizados
        return detallepedidorepository.findByNroPedido(id);
        
    }
    
    
    private List<ProductoEntity> procesarEliminaciones(List<DetallePedidoEntity> detallesAnteriores, List<DetallePedidoEntity> nuevosDetalles) throws Exception {
        List<DetallePedidoEntity> itemsEliminar = getElementosEliminar(detallesAnteriores, nuevosDetalles);
        List<ProductoEntity> productosActualizarExistencias = new ArrayList<>();

        if (!itemsEliminar.isEmpty()) {
            productosActualizarExistencias.addAll(getProductosExistenciasActualizadas(itemsEliminar, null, "ELIMINAR"));
            detallepedidorepository.deleteAll(itemsEliminar);
        }

        return productosActualizarExistencias;
    }
    
    
    private List<ProductoEntity> procesarModificaciones(List<DetallePedidoEntity> detallesAnteriores, List<DetallePedidoEntity> nuevosDetalles)throws Exception {
        List<DetallePedidoEntity> itemsModificar = getElementosModificar(detallesAnteriores, nuevosDetalles);
        List<ProductoEntity> productosActualizarExistencias = new ArrayList<>();

        if (!itemsModificar.isEmpty()) {
            productosActualizarExistencias.addAll(getProductosExistenciasActualizadas(itemsModificar, detallesAnteriores, "MODIFICAR"));
            detallepedidorepository.saveAll(itemsModificar);
        }

        return productosActualizarExistencias;
    }
    
    private List<ProductoEntity> procesarRegistros(List<DetallePedidoEntity> detallesAnteriores, List<DetallePedidoEntity> nuevosDetalles) {
        List<DetallePedidoEntity> itemsRegistrar = getElementosRegistrar(detallesAnteriores, nuevosDetalles);
        List<ProductoEntity> productosActualizarExistencias = new ArrayList<>();

        if (!itemsRegistrar.isEmpty()) {
            productosActualizarExistencias.addAll(getProductosExistenciasActualizadas(itemsRegistrar, null, "REGISTRAR"));
            detallepedidorepository.saveAll(itemsRegistrar);
        }

        return productosActualizarExistencias;
    }
    
    public List<DetallePedidoEntity> getElementosEliminar (List<DetallePedidoEntity> anteriorDetalle, List<DetallePedidoEntity> actualDetalle)throws Exception {
        List<DetallePedidoEntity> elementos = new ArrayList<>();
                           
        // Recorre detalle anterior
        anteriorDetalle.forEach(anterior -> {
            // Busca si el producto del detalle anterior existe en el detalle actual
            boolean existe = actualDetalle.stream()
                    .anyMatch(actual -> Objects.equals(actual.getProducto().getId(), anterior.getProducto().getId()));

            // Si no existe en el detalle actual, se intenta eliminar
            if (!existe) {
                if (anterior.getCantidadFacturada() > 0) {
                    // Lanza excepción si el producto ya fue facturado
                    throw new RuntimeException("No es posible eliminar el Producto: " + anterior.getProducto().getNombre());
                }
                // Si no está facturado, se agrega a la lista de elementos a eliminar
                elementos.add(anterior);
            }
        });
        
        return elementos;
    }
    
    public List<DetallePedidoEntity> getElementosModificar (List<DetallePedidoEntity> anteriorDetalle, List<DetallePedidoEntity> actualDetalle)throws Exception {
        List<DetallePedidoEntity> elementos = new ArrayList<>();
                           
        // Recorre detalle anterior
        anteriorDetalle.forEach(anterior -> {
            // Busca si el producto del detalle anterior existe en el detalle actual
            Optional<DetallePedidoEntity> actual = actualDetalle.stream().filter(act -> Objects.equals(act.getProducto().getId(), anterior.getProducto().getId())).findAny();
            // Si encuentra, y hay diferencia se intenta modificar
            if(actual.isPresent() ){
                if(actual.get().getCantidad() != anterior.getCantidad()){
                    // Lanza excepción si el producto ya fue facturado
                    if(anterior.getCantidadFacturada() > actual.get().getCantidad()){
                        throw new RuntimeException("No es posible modificar la cantidad del Producto: "+ anterior.getProducto().getNombre());
                    }
                    
                    actual.get().setId(anterior.getId());
                    actual.get().setPedido(anterior.getPedido());
                    
                    elementos.add(actual.get());
                }
            }

        });
        
        return elementos;
    }
    
    public List<DetallePedidoEntity> getElementosRegistrar (List<DetallePedidoEntity> anteriorDetalle, List<DetallePedidoEntity> actualDetalle) {
        PedidoEntity pedido = anteriorDetalle.get(0).getPedido();
        List<DetallePedidoEntity> elementos = new ArrayList<>();
                           
        // Recorre detalle anterior
        actualDetalle.forEach(actual -> {
            // Busca si el producto del detalle actual no existe en el detalle anterior
            boolean existe = anteriorDetalle.stream()
                    .anyMatch(anterior -> Objects.equals(actual.getProducto().getId(), anterior.getProducto().getId()));

            // Si no existe en el detalle anterior, se intenta registrar
            if (!existe) {
                actual.setPedido(pedido);
              
                elementos.add(actual);
            }
        });
        
        return elementos;
    }
    
    
    public List<ProductoEntity> getProductosExistenciasActualizadas(List<DetallePedidoEntity> detalleProcesar, List<DetallePedidoEntity> detalleAnterior, String tipoItems) {
        List<ProductoEntity> productosActualizarExistencias = new ArrayList<>();

        for (DetallePedidoEntity detalle : detalleProcesar) {
            ProductoEntity producto = detalle.getProducto();

            switch (tipoItems) {
                case "REGISTRAR":
                    procesarRegistro(detalle, producto);
                    break;

                case "MODIFICAR":
                    procesarModificacion(detalle, detalleAnterior, producto);
                    break;

                case "ELIMINAR":
                    procesarEliminacion(detalle, producto);
                    break;

                default:
                    throw new IllegalArgumentException("Tipo de operación no soportada: " + tipoItems);
            }

            productosActualizarExistencias.add(producto);
        }

        return productosActualizarExistencias;
    }

    private void procesarRegistro(DetallePedidoEntity detalle, ProductoEntity productoActual) {
        ProductoEntity producto = productoService.findById(productoActual.getId()).get();
        
        productoActual.setCantDisponible(producto.getCantDisponible() - detalle.getCantidad());
        productoActual.setCantReservada(producto.getCantReservada() + detalle.getCantidad());
    }

    private void procesarModificacion(DetallePedidoEntity detalle, List<DetallePedidoEntity> detallesAnteriores, ProductoEntity producto) {
        DetallePedidoEntity detalleAnterior = detallesAnteriores.stream()
                .filter(d -> Objects.equals(d.getProducto().getId(), detalle.getProducto().getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el detalle anterior para el producto: " + detalle.getProducto().getId()));

        int cantDisponibleAnt = detalleAnterior.getProducto().getCantDisponible();
        int cantReservadaAnt = detalleAnterior.getProducto().getCantReservada();

        int cantidadAnterior = detalleAnterior.getCantidad();
        int cantidadNueva = detalle.getCantidad();

        producto.setCantDisponible(cantDisponibleAnt + cantidadAnterior - cantidadNueva);
        producto.setCantReservada(cantReservadaAnt - cantidadAnterior + cantidadNueva);
    }

    private void procesarEliminacion(DetallePedidoEntity detalle, ProductoEntity producto) {
        producto.setCantDisponible(producto.getCantDisponible() + detalle.getCantidad());
        producto.setCantReservada(producto.getCantReservada() - detalle.getCantidad());
    }

    
 
  
    @Override
    public void updateDetallesFacturadas(List<DetalleVentaEntity> detalles, PedidoEntity pedido, String accion){
        List<DetallePedidoEntity> detallePedido = detallepedidorepository.findByNroPedido(pedido.getId());
        List<DetallePedidoEntity> detallePedidoUpdated = new ArrayList<>();
       detalles.forEach((detalle)-> {
           DetallePedidoEntity detalleAnterior = detallePedido.stream().filter(obj -> Objects.equals(obj.getProducto().getId(), detalle.getProducto().getId())).findFirst().get();

           if(detalleAnterior != null){
               switch (accion) {
                case "REGISTRAR" -> detalleAnterior.setCantidadFacturada(detalleAnterior.getCantidadFacturada() + detalle.getCantidad());

                case "ELIMINAR" -> detalleAnterior.setCantidadFacturada(detalleAnterior.getCantidadFacturada() - detalle.getCantidad());

                }
               
               detallePedidoUpdated.add(detalleAnterior);
           }

        });
       detallepedidorepository.saveAll(detallePedidoUpdated);
    }

    
    @Override
    public void deleteDetallePedido(Long id) {
        detallepedidorepository.deleteById(id);
    }
    
    @Override
    public void deleteDetallesPedido(List<Long> ids) {
        detallepedidorepository.deleteAllById(ids);
    }
    
    @Override
    public void deleteByPedido(Long id) {
        detallepedidorepository.deleteByPedido(id);
    }
    

}
