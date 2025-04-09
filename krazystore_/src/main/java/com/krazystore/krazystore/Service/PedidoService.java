/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.PedidoCreationDTO;
import com.krazystore.krazystore.DTO.PedidoDTO;
import com.krazystore.krazystore.Entity.DetallePedidoEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface PedidoService {
    List<PedidoDTO> findAll();
    PedidoCreationDTO findById(Long id);
    PedidoEntity savePedido(PedidoCreationDTO pedidoCreationDTO);
    PedidoEntity updatePedido(PedidoCreationDTO pedidoCreationDTO, Long id) throws Exception;
    void deletePedido(Long id);
}
