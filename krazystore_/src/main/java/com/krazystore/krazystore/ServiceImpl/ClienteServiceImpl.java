/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.DTO.ClienteCreationDTO;
import com.krazystore.krazystore.DTO.ClienteDTO;
import com.krazystore.krazystore.DTO.PersonaCreationDTO;
import com.krazystore.krazystore.Entity.ClienteEntity;
import com.krazystore.krazystore.Entity.PersonaEntity;
import com.krazystore.krazystore.Repository.ClienteRepository;
import com.krazystore.krazystore.Service.ClienteService;
import com.krazystore.krazystore.Service.PersonaService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    public ClienteServiceImpl(com.krazystore.krazystore.Repository.ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    private final ClienteRepository clienteRepository;
    
    @Autowired
    private PersonaService personaService;

    @Override
    public List<ClienteDTO> findAll() {
        return clienteRepository.findAllDTO();
    }

    @Override
    public Optional<ClienteDTO> findById(Long id) {
        return clienteRepository.findClienteDTOById(id);
    }

    @Transactional
    @Override
    public ClienteDTO saveCliente(ClienteDTO clienteDTO) {
        PersonaEntity persona = new PersonaEntity(clienteDTO.getNombre(),
                clienteDTO.getApellido(),
                clienteDTO.getCorreo(),
                clienteDTO.getTipoDoc(),
                clienteDTO.getNroDoc(),
                clienteDTO.getTelefono());
        
        PersonaCreationDTO personaCreationDTO = new PersonaCreationDTO(
                persona,
                clienteDTO.getDireccion());
        
        PersonaEntity personaCliente = personaService.savePersona(personaCreationDTO);
        ClienteEntity nuevoCliente = new ClienteEntity(personaCliente);
        clienteRepository.save(nuevoCliente);
        return new ClienteDTO(nuevoCliente.getId(),personaCliente);
    }

    @Transactional
    @Override
    public ClienteDTO updateCliente(ClienteDTO clienteDTO, long id) {
        ClienteEntity updatedCliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no existe"));
        
        PersonaEntity persona = new PersonaEntity(clienteDTO.getNombre(),
                clienteDTO.getApellido(),
                clienteDTO.getCorreo(),
                clienteDTO.getTipoDoc(),
                clienteDTO.getNroDoc(),
                clienteDTO.getTelefono());
        
        PersonaCreationDTO personaCreationDTO = new PersonaCreationDTO(
                persona,
                clienteDTO.getDireccion());
        
        personaService.updatePersona(personaCreationDTO, updatedCliente.getPersona().getId());
        
        return new ClienteDTO(id, updatedCliente.getPersona());
    }

    @Override
    public void deleteCliente(Long id) {
        ClienteEntity deletedCliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no existe"));
        
        deletedCliente.setActivo(Boolean.FALSE);
        clienteRepository.save(deletedCliente);
    }
    
}
