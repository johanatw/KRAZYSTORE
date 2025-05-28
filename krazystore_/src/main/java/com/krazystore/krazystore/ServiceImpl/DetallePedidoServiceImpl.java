/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.TipoAjusteExistencia;
import Utils.TipoOperacionDetalle;
import com.krazystore.krazystore.DTO.DetallePedidoCompraDTO;
import com.krazystore.krazystore.DTO.DetallePedidoCreationRequest;
import com.krazystore.krazystore.DTO.DetallePedidoDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.CategoriaEntity;

import com.krazystore.krazystore.Entity.DetallePedidoEntity;
import com.krazystore.krazystore.Entity.DetalleVentaEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;
import com.krazystore.krazystore.Repository.DetallePedidoRepository;
import com.krazystore.krazystore.Service.AnticipoService;

import com.krazystore.krazystore.Service.DetallePedidoService;
import com.krazystore.krazystore.Service.ProductoService;
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
    public List<DetallePedidoDTO> findDTOByIdPedido(Long id) {
        return detallepedidorepository.findDetallesByIdPedido(id);
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
    public List<ProductoExistenciasDTO> saveDetallePedido(Long idPedido, List<DetallePedidoEntity> detalles) {
  
        List<ProductoExistenciasDTO> productosActualizarExistencias = new ArrayList<>();
        List<ProductoEntity> productosActualizar = new ArrayList<>();
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(idPedido);
        detalles.forEach(d -> {
            ProductoExistenciasDTO productoActualizar = new ProductoExistenciasDTO(
                    d.getProducto().getId(),
                    d.getCantidad(),
                    TipoAjusteExistencia.INCREMENTAR
            );
            d.setPedido(pedido);
            productosActualizarExistencias.add(productoActualizar);
        });
        
        //productoService.updateExistencias(productosActualizar);
        detallepedidorepository.saveAll(detalles);
        return productosActualizarExistencias;
        
    }



    @Transactional
    @Override
    public List<ProductoExistenciasDTO> updateDetallesPedido(List<DetallePedidoEntity> nuevosDetalles, Long id) throws Exception {
        List<DetallePedidoEntity> detallesAnteriores = detallepedidorepository.findByNroPedido(id); 
        List<ProductoExistenciasDTO> productosActualizarExistencias = new ArrayList<>();
        
        // Crear copias de los objetos para evitar que se modifiquen las referencias originales
            List<DetallePedidoEntity> anterioresCopias = detallesAnteriores.stream()
                .map(item -> new DetallePedidoEntity(item)) // Constructor de copia
                .collect(Collectors.toList());

        // Procesar cambios en los detalles
        procesarCambiosEnDetalles(detallesAnteriores, nuevosDetalles);
        
        //List<DetallePedidoEntity> detallesAnteriores = detallepedidorepository.findByNroPedido(id);
        productosActualizarExistencias = calcularProductosExistencias(anterioresCopias, nuevosDetalles);

        // Calcular productos a actualizar existencias
        return productosActualizarExistencias;
        
    }
    
    private void procesarCambiosEnDetalles(List<DetallePedidoEntity> detallesAnteriores, List<DetallePedidoEntity> nuevosDetalles) throws Exception {
        procesarEliminaciones(detallesAnteriores, nuevosDetalles);
        procesarModificaciones(detallesAnteriores, nuevosDetalles);
        procesarRegistros(detallesAnteriores, nuevosDetalles);
    }
    
    private void procesarEliminaciones(List<DetallePedidoEntity> detallesAnteriores, List<DetallePedidoEntity> nuevosDetalles) throws Exception {
        List<DetallePedidoEntity> itemsEliminar = getElementosEliminar(detallesAnteriores, nuevosDetalles);
        if (!itemsEliminar.isEmpty()) {
            detallepedidorepository.deleteAll(itemsEliminar);
        }
    }
    
    private void procesarModificaciones(List<DetallePedidoEntity> detallesAnteriores, List<DetallePedidoEntity> nuevosDetalles) throws Exception {
        List<DetallePedidoEntity> itemsModificar = getElementosModificar(detallesAnteriores, nuevosDetalles);
        if (!itemsModificar.isEmpty()) {
            detallepedidorepository.saveAll(itemsModificar);
        }

    }
    
    private void procesarRegistros(List<DetallePedidoEntity> detallesAnteriores, List<DetallePedidoEntity> nuevosDetalles) {
        List<DetallePedidoEntity> itemsRegistrar = getElementosRegistrar(detallesAnteriores, nuevosDetalles);
        if (!itemsRegistrar.isEmpty()) {
            detallepedidorepository.saveAll(itemsRegistrar);
        }
    }
    
    private List<ProductoExistenciasDTO> calcularProductosExistencias(List<DetallePedidoEntity> detallesAnteriores, List<DetallePedidoEntity> nuevosDetalles) throws Exception {
        List<ProductoExistenciasDTO> productosExistencias = new ArrayList<>();

        // Agregar productos de eliminaciones
        productosExistencias.addAll(getProductosExistencias(detallesAnteriores, nuevosDetalles, TipoOperacionDetalle.ELIMINAR));

        // Agregar productos de modificaciones
        productosExistencias.addAll(getProductosExistencias(detallesAnteriores, nuevosDetalles, TipoOperacionDetalle.MODIFICAR));

        // Agregar productos de registros
        productosExistencias.addAll(getProductosExistencias(detallesAnteriores, nuevosDetalles, TipoOperacionDetalle.REGISTRAR));

        return productosExistencias;
    }
    
    private List<ProductoExistenciasDTO> getProductosExistencias(List<DetallePedidoEntity> detallesAnteriores, List<DetallePedidoEntity> nuevosDetalles, TipoOperacionDetalle tipoOperacion) throws Exception{
        List<ProductoExistenciasDTO> productosExistencias = new ArrayList<>();
        Map<Long, Integer> mapaCantidadAnterior = construirMapaCantidadAnterior(detallesAnteriores);

        List<DetallePedidoEntity> detallesProcesar = switch (tipoOperacion) {
            case REGISTRAR -> getElementosRegistrar(detallesAnteriores, nuevosDetalles);
            case MODIFICAR -> getElementosModificar(detallesAnteriores, nuevosDetalles);
            case ELIMINAR -> getElementosEliminar(detallesAnteriores, nuevosDetalles);
        };
                
        for (DetallePedidoEntity detalle : detallesProcesar) {
            Long idProducto = detalle.getProducto().getId();
            
            int cantidadAnterior = (tipoOperacion == TipoOperacionDetalle.MODIFICAR ) ? mapaCantidadAnterior.getOrDefault(idProducto, 0): 0;
            int cantidadNueva = detalle.getCantidad();
            
            ProductoExistenciasDTO productoActualizar = new ProductoExistenciasDTO();
            productoActualizar.setIdProducto(idProducto);
            productoActualizar.setCantidad(cantidadNueva - cantidadAnterior);
            productoActualizar.setAccion(determinarAccion(tipoOperacion));
            productosExistencias.add(productoActualizar);
        }

        return productosExistencias;
    }
    

    private TipoAjusteExistencia determinarAccion(TipoOperacionDetalle tipoOperacion) {
        return (tipoOperacion == TipoOperacionDetalle.ELIMINAR) ? TipoAjusteExistencia.DISMINUIR : TipoAjusteExistencia.INCREMENTAR;
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
                /*if (anterior.getCantidadFacturada() > 0) {
                    // Lanza excepción si el producto ya fue facturado
                    throw new BadRequestException("No es posible eliminar el Producto: " + anterior.getProducto().getNombre());
                }*/
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
                    /*if(anterior.getCantidadFacturada() > actual.get().getCantidad()){
                        throw new BadRequestException("No es posible modificar la cantidad del Producto: "+ anterior.getProducto().getNombre());
                    }*/
                    
                    actual.get().setId(anterior.getId());
                    //actual.get().setPedido(anterior.getPedido());
                    
                    elementos.add(actual.get());
                }
            }

        });
        
        return elementos;
    }
    
    public List<DetallePedidoEntity> getElementosRegistrar (List<DetallePedidoEntity> anteriorDetalle, List<DetallePedidoEntity> actualDetalle) {
        PedidoEntity pedido = anteriorDetalle.get(0).getPedido();
        List<DetallePedidoEntity> elementos = new ArrayList<>();
        System.out.println("getElementosRegistrar");
        // Recorre detalle anterior
        actualDetalle.forEach(actual -> {
            System.out.println(actual.getProducto().getId());
            // Busca si el producto del detalle actual no existe en el detalle anterior
            boolean existe = anteriorDetalle.stream()
                    .anyMatch(anterior -> Objects.equals(actual.getProducto().getId(), anterior.getProducto().getId()));

            // Si no existe en el detalle anterior, se intenta registrar
            if (!existe) {
                System.out.println("ENTRA IF");
                actual.setPedido(pedido);
              
                elementos.add(actual);
            }
        });
        
        return elementos;
    }
    
    
    public List<ProductoExistenciasDTO> getProductosExistenciasActualizadas(List<DetallePedidoEntity> detalleProcesar, List<DetallePedidoEntity> detalleAnterior, String tipoItems) {
        List<ProductoEntity> productosActualizarExistencias = new ArrayList<>();
        List<ProductoExistenciasDTO> productosExistencias = new ArrayList<>();

        for (DetallePedidoEntity detalle : detalleProcesar) {
            ProductoExistenciasDTO productoActualizar = new ProductoExistenciasDTO();
            ProductoEntity producto = detalle.getProducto();

            switch (tipoItems) {
                case "REGISTRAR":
                    procesarRegistro(detalle, productoActualizar);
                    break;

                case "MODIFICAR":
                    procesarModificacion(detalle, detalleAnterior, productoActualizar);
                    break;

                case "ELIMINAR":
                    procesarEliminacion(detalle, productoActualizar);
                    break;

                default:
                    throw new IllegalArgumentException("Tipo de operación no soportada: " + tipoItems);
            }

            //productosActualizarExistencias.add(producto);
            productosExistencias.add(productoActualizar);
        }

        return productosExistencias;
    }

    private void procesarRegistro(DetallePedidoEntity detalle, ProductoExistenciasDTO productoActualizar) {
        productoActualizar.setIdProducto(detalle.getProducto().getId());
        productoActualizar.setCantidad(detalle.getCantidad());
        productoActualizar.setAccion(TipoAjusteExistencia.INCREMENTAR);
 
        /*ProductoEntity producto = productoService.findById(productoActual.getId()).get();
        
        productoActual.setCantDisponible(producto.getCantDisponible() - detalle.getCantidad());
        productoActual.setCantReservada(producto.getCantReservada() + detalle.getCantidad());*/
    }

    private void procesarModificacion(DetallePedidoEntity detalle, List<DetallePedidoEntity> detallesAnteriores, ProductoExistenciasDTO productoActualizar) {
        productoActualizar.setIdProducto(detalle.getProducto().getId());
        DetallePedidoEntity detalleAnterior = detallesAnteriores.stream()
                .filter(d -> Objects.equals(d.getProducto().getId(), productoActualizar.getIdProducto()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el detalle anterior para el producto: " + productoActualizar.getIdProducto()));

        //int cantDisponibleAnt = detalleAnterior.getProducto().getCantDisponible();
        //int cantReservadaAnt = detalleAnterior.getProducto().getCantReservada();

        int cantidadAnterior = detalleAnterior.getCantidad();
        int cantidadNueva = detalle.getCantidad();
        
        productoActualizar.setCantidad(cantidadNueva - cantidadAnterior);
        
        TipoAjusteExistencia accion = (productoActualizar.getCantidad() < 0) ? TipoAjusteExistencia.INCREMENTAR : TipoAjusteExistencia.DISMINUIR;
        productoActualizar.setAccion(accion);
        
        //producto.setCantDisponible(cantDisponibleAnt + cantidadAnterior - cantidadNueva);
        //producto.setCantReservada(cantReservadaAnt - cantidadAnterior + cantidadNueva);
    }

    private void procesarEliminacion(DetallePedidoEntity detalle, ProductoExistenciasDTO productoActualizar) {
        productoActualizar.setIdProducto(detalle.getProducto().getId());
        productoActualizar.setCantidad(detalle.getCantidad());
        productoActualizar.setAccion(TipoAjusteExistencia.DISMINUIR);
        //producto.setCantDisponible(producto.getCantDisponible() + detalle.getCantidad());
        //producto.setCantReservada(producto.getCantReservada() - detalle.getCantidad());
    }

    private Map<Long, Integer> construirMapaCantidadAnterior(List<DetallePedidoEntity> detallesAnteriores) {
        return detallesAnteriores.stream()
                .collect(Collectors.toMap(
                    detalle -> detalle.getProducto().getId(),
                    DetallePedidoEntity::getCantidad
                ));
    }
    
    
 
  /*
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
    }*/

    
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

    @Override
    public List<ProductoExistenciasDTO> getProductosRevertirReservas(Long id) {
        List<DetallePedidoEntity> detalles = detallepedidorepository.findByNroPedido(id); 
        List<ProductoExistenciasDTO> productosActualizarExistencias = new ArrayList<>();
        
        List<ProductoEntity> productosActualizar = new ArrayList<>();

        detalles.forEach(d -> {
            ProductoExistenciasDTO productoActualizar = new ProductoExistenciasDTO(
                    d.getProducto().getId(),
                    d.getCantidad(),
                    TipoAjusteExistencia.DISMINUIR
            );
            productosActualizarExistencias.add(productoActualizar);
        });
        
        return productosActualizarExistencias;
    }

   
    

}
