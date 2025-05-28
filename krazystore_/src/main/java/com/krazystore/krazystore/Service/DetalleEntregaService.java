/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.DetalleEntregaDTO;
import com.krazystore.krazystore.Entity.DetalleEntrega;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface DetalleEntregaService {
    List<DetalleEntregaDTO> findByIdEntrega(Long id);
    List<DetalleEntrega> saveDetalle(List<DetalleEntrega> detalle);

    public void deleteByIdEntrega(Long id);
}
