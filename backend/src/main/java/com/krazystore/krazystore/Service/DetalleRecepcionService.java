/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.DetalleRecepcionDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.DetalleRecepcion;
import java.util.List;

/**
 *
 * @author HP
 */
public interface DetalleRecepcionService {

    public List<DetalleRecepcionDTO> obtenerDetallesFacturasRecepcionar(List<Long> ids);
   /* List<DetalleRecepcion> findByIdRecepcion(Long id);
    List<DetalleRecepcionDTO> obtenerDetalleFacturaRecepcionar(Long idCompra);
    List<DetalleRecepcionDTO> findDetalleByIdRecepcion(Long idRecepcion);
    List<ProductoExistenciasDTO> saveDetRecepcion (List<DetalleRecepcion> detalle, Long idRecepcion);
    List<ProductoExistenciasDTO> updateDetRecepcion(List<DetalleRecepcion> detalle, Long idRecepcion);
    List<ProductoExistenciasDTO> deleteDetRecepcion(Long idRecepcion);

    public List<DetalleRecepcionDTO> obtenerDetallesFacturasRecepcionar(List<Long> ids);*/

    public List<ProductoExistenciasDTO> saveDetRecepcion(List<DetalleRecepcion> detalle, Long id);

    public List<DetalleRecepcionDTO> findDetalleByIdRecepcion(Long id);

    public List<ProductoExistenciasDTO> deleteDetRecepcion(Long id);
}
