/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.ClienteDTO;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface ClienteService {

    public List<ClienteDTO> findAll();

    public Optional<ClienteDTO> findById(Long id);

    public ClienteDTO saveCliente(ClienteDTO clienteDTO);

    public ClienteDTO updateCliente(ClienteDTO clienteDTO, long id);

    public void deleteCliente(Long id);
    
}
