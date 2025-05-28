/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.TipoAjusteExistencia;
import Utils.TipoOperacionDetalle;
import com.krazystore.krazystore.DTO.DetalleVentaCreationRequest;
import com.krazystore.krazystore.DTO.DetalleVentaPrepararDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.DetalleVentaEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import com.krazystore.krazystore.Repository.DetalleVentaRepository;
import com.krazystore.krazystore.Repository.VentaRepository;
import com.krazystore.krazystore.Service.DetalleVentaService;
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author HP
 */
@Service
public class DetalleVentaServiceImpl implements DetalleVentaService{
    private final DetalleVentaRepository detalleventarepository;
 

    public DetalleVentaServiceImpl(DetalleVentaRepository detalleventarepository) {
        this.detalleventarepository = detalleventarepository;
    }

    
    @Override
    public List<DetalleVentaEntity> findAll() {
        return detalleventarepository.findAll();
    }

    @Override
    public Optional<DetalleVentaEntity> findById(Long id) {
        return detalleventarepository.findById(id);
    }
    
    @Override
    public List<DetalleVentaEntity> findByIdVenta(Long id) {
        return detalleventarepository.findByIdVenta(id);
    }

    @Override
    public List<ProductoExistenciasDTO> saveDetalleVenta(VentaEntity venta, List<DetalleVentaEntity> detalle) {
        List<ProductoExistenciasDTO> productosActualizarExistencias = new ArrayList<>();
        
        detalle.forEach(d -> {
            ProductoExistenciasDTO productoActualizar = new ProductoExistenciasDTO(
                    d.getProducto().getId(),
                    d.getCantidad(),
                    TipoAjusteExistencia.INCREMENTAR
            );
            d.setVenta(venta);
            productosActualizarExistencias.add(productoActualizar);
        });
        
        detalleventarepository.saveAll(detalle);
        return productosActualizarExistencias;
        
    }

    @Override
    public DetalleVentaEntity updateDetalleVenta(DetalleVentaEntity detalleVentaEntity, Long id) {
        DetalleVentaEntity updatedDetalleVenta = detalleventarepository.findById(id).get();
        
        updatedDetalleVenta.setVenta(detalleVentaEntity.getVenta());
        updatedDetalleVenta.setProducto(detalleVentaEntity.getProducto());
        updatedDetalleVenta.setCantidad(detalleVentaEntity.getCantidad());
        updatedDetalleVenta.setPrecio(detalleVentaEntity.getPrecio());
       // updatedDetalleVenta.setMontoIva(detalleVentaEntity.getMontoIva());
        updatedDetalleVenta.setSubTotal(detalleVentaEntity.getSubTotal());
        
        return detalleventarepository.save(updatedDetalleVenta);
    }

    @Override
    public void deleteDetalleVenta(Long id) {
        detalleventarepository.deleteById(id);
    }
    
    @Override
    public List<ProductoExistenciasDTO> anularDetalleVenta(Long idVenta) {
        List<ProductoExistenciasDTO> productosActualizarExistencias = new ArrayList<>();
        List<DetalleVentaEntity> detalle = detalleventarepository.findByIdVenta(idVenta);
        detalle.forEach(d -> {
            ProductoExistenciasDTO productoActualizar = new ProductoExistenciasDTO(
                    d.getProducto().getId(),
                    d.getCantidad(),
                    TipoAjusteExistencia.DISMINUIR
            );
 
            productosActualizarExistencias.add(productoActualizar);
        });
        
        return productosActualizarExistencias;
        
    }
    
    @Transactional
    @Override
    public List<ProductoExistenciasDTO> updateDetVenta(List<DetalleVentaEntity> detalle, Long idVenta) throws Exception {
        if(detalle.isEmpty()){
            throw new BadRequestException("Falta Detalle " );
        }
        List<DetalleVentaEntity> detallesAnteriores = detalleventarepository.findByIdVenta(idVenta); 
        
        // Crear copias de los objetos para evitar que se modifiquen las referencias originales
        List<DetalleVentaEntity> anterioresCopias = detallesAnteriores.stream()
            .map(item -> new DetalleVentaEntity(item)) // Constructor de copia
            .collect(Collectors.toList());
        
        // Procesar cambios en los detalles
        procesarCambiosEnDetalles(detallesAnteriores, detalle);
        
        //List<DetallePedidoEntity> detallesAnteriores = detallepedidorepository.findByNroPedido(id);
        List<ProductoExistenciasDTO> productosActualizarExistencias = calcularProductosExistencias(anterioresCopias, detalle);   

        return productosActualizarExistencias;
        
    }



    private void procesarCambiosEnDetalles(List<DetalleVentaEntity> detallesAnteriores, List<DetalleVentaEntity> nuevosDetalles) throws Exception {
        procesarEliminaciones(detallesAnteriores, nuevosDetalles);
        procesarModificaciones(detallesAnteriores, nuevosDetalles);
        procesarRegistros(detallesAnteriores, nuevosDetalles);
    }
    
    private void procesarEliminaciones(List<DetalleVentaEntity> detallesAnteriores, List<DetalleVentaEntity> nuevosDetalles)  {
        List<DetalleVentaEntity> itemsEliminar = getElementosEliminar(detallesAnteriores, nuevosDetalles);

        if (!itemsEliminar.isEmpty()) {
            detalleventarepository.deleteAll(itemsEliminar);
        }

    }
    
    
    private void procesarModificaciones(List<DetalleVentaEntity> detallesAnteriores, List<DetalleVentaEntity> nuevosDetalles) {
        List<DetalleVentaEntity> itemsModificar = getElementosModificar(detallesAnteriores, nuevosDetalles);

        if (!itemsModificar.isEmpty()) {
            detalleventarepository.saveAll(itemsModificar);
        }

    }
    
    private void procesarRegistros(List<DetalleVentaEntity> detallesAnteriores, List<DetalleVentaEntity> nuevosDetalles) {
        List<DetalleVentaEntity> itemsRegistrar = getElementosRegistrar(detallesAnteriores, nuevosDetalles);

        if (!itemsRegistrar.isEmpty()) {
            detalleventarepository.saveAll(itemsRegistrar);
        }

}

    public List<DetalleVentaEntity> getElementosEliminar (List<DetalleVentaEntity> anteriorDetalle, List<DetalleVentaEntity> actualDetalle) {
        List<DetalleVentaEntity> elementos = new ArrayList<>();
                           
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
    
    public List<DetalleVentaEntity> getElementosModificar (List<DetalleVentaEntity> anteriorDetalle, List<DetalleVentaEntity> actualDetalle) {
        List<DetalleVentaEntity> elementos = new ArrayList<>();
                           
        // Recorre detalle anterior
        anteriorDetalle.forEach(anterior -> {
            // Busca si el producto del detalle anterior existe en el detalle actual
            Optional<DetalleVentaEntity> actual = actualDetalle.stream().filter(act -> Objects.equals(act.getProducto().getId(), anterior.getProducto().getId())).findAny();
            // Si encuentra, y hay diferencia se intenta modificar
            if(actual.isPresent() ){
                if(actual.get().getCantidad() != anterior.getCantidad() ){
                    // Lanza excepción si el producto ya fue facturado
                    /*if(anterior.getCantRecepcionada() > actual.get().getCantidad()){
                        throw new BadRequestException("No es posible modificar la cantidad del Producto: "+ anterior.getProducto().getNombre());
                    }*/
                    
                    actual.get().setIdDetVent(anterior.getIdDetVent());
      
                    
                    elementos.add(actual.get());
                }
            }

        });
        
        return elementos;
    }
    
    public List<DetalleVentaEntity> getElementosRegistrar (List<DetalleVentaEntity> anteriorDetalle, List<DetalleVentaEntity> actualDetalle) {

        List<DetalleVentaEntity> elementos = new ArrayList<>();
                           
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

    private List<ProductoExistenciasDTO> calcularProductosExistencias(List<DetalleVentaEntity> detallesAnteriores, List<DetalleVentaEntity> nuevosDetalles) throws Exception {
        List<ProductoExistenciasDTO> productosExistencias = new ArrayList<>();

        // Agregar productos de eliminaciones
        productosExistencias.addAll(getProductosExistencias(detallesAnteriores, nuevosDetalles, TipoOperacionDetalle.ELIMINAR));

        // Agregar productos de modificaciones
        productosExistencias.addAll(getProductosExistencias(detallesAnteriores, nuevosDetalles, TipoOperacionDetalle.MODIFICAR));

        // Agregar productos de registros
        productosExistencias.addAll(getProductosExistencias(detallesAnteriores, nuevosDetalles, TipoOperacionDetalle.REGISTRAR));

        return productosExistencias;
    }
    
    private List<ProductoExistenciasDTO> getProductosExistencias(List<DetalleVentaEntity> detallesAnteriores, List<DetalleVentaEntity> nuevosDetalles, TipoOperacionDetalle tipoOperacion) throws Exception{
        List<ProductoExistenciasDTO> productosExistencias = new ArrayList<>();
        Map<Long, Integer> mapaCantidadAnterior = construirMapaCantidadAnterior(detallesAnteriores);

        List<DetalleVentaEntity> detallesProcesar = switch (tipoOperacion) {
            case REGISTRAR -> getElementosRegistrar(detallesAnteriores, nuevosDetalles);
            case MODIFICAR -> getElementosModificar(detallesAnteriores, nuevosDetalles);
            case ELIMINAR -> getElementosEliminar(detallesAnteriores, nuevosDetalles);
        };
                
        for (DetalleVentaEntity detalle : detallesProcesar) {
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
    
    private Map<Long, Integer> construirMapaCantidadAnterior(List<DetalleVentaEntity> detallesAnteriores) {
        return detallesAnteriores.stream()
                .collect(Collectors.toMap(
                    detalle -> detalle.getProducto().getId(),
                    DetalleVentaEntity::getCantidad
                ));
    }

    @Override
    public List<DetalleVentaPrepararDTO> findDetallesFacturaRecepcionarByIdVenta(Long id) {
        return detalleventarepository.findDetallesFacturaPrepararByIdVenta(id);
    }
    
}
