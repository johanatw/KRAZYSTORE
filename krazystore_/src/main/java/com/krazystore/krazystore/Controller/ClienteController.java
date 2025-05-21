/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.ClienteCreationDTO;
import com.krazystore.krazystore.DTO.ClienteDTO;
import com.krazystore.krazystore.Entity.ClienteEntity;
import com.krazystore.krazystore.Service.ClienteService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/clientes")
public class ClienteController {

    public ClienteController(com.krazystore.krazystore.Service.ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    private final ClienteService clienteService;
    
    @GetMapping
    public List<ClienteDTO> findAll() {
        return clienteService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ClienteDTO> findById(@PathVariable("id") Long id) {
        return clienteService.findById(id);
    }
    
    @PostMapping
    public ClienteDTO saveCliente(@RequestBody ClienteDTO clienteDTO) {
            return clienteService.saveCliente(clienteDTO);

    }

    @PutMapping("/{id}")
    public ClienteDTO updateCliente(@PathVariable long id, @RequestBody ClienteDTO clienteDTO) {
        
            return clienteService.updateCliente(clienteDTO, id);
        
    }

    @DeleteMapping("/{id}")
    public void deleteCliente(@PathVariable("id") Long id) {
        clienteService.deleteCliente(id);
    }
}
