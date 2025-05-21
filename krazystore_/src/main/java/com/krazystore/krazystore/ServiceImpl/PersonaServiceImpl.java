/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.DTO.PersonaCreationDTO;
import com.krazystore.krazystore.DTO.PersonaDTO;
import com.krazystore.krazystore.DTO.PersonaDTO2;
import com.krazystore.krazystore.Repository.PersonaRepository;
import com.krazystore.krazystore.Service.PersonaService;
import com.krazystore.krazystore.Entity.PersonaEntity;
import com.krazystore.krazystore.Mapper.PersonaDTOMapper;
import com.krazystore.krazystore.Service.DireccionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class PersonaServiceImpl implements PersonaService{
    private final PersonaRepository personarepository;
    private final PersonaDTOMapper personaDTOMapper;
    
    @Autowired
    private DireccionService direccionService;

    public PersonaServiceImpl(PersonaRepository personarepository, PersonaDTOMapper personaDTOMapper) {
        this.personarepository = personarepository;
        this.personaDTOMapper = personaDTOMapper;
        
    }
    

    @Override
    public List<PersonaDTO> findAll() {
        return personarepository.findPersonas()
                .stream()
                .map(personaDTOMapper)
                .collect(Collectors.toList());
        
        
    }
    
    @Override
    public List<PersonaDTO2> findPersonasDTO() {
        return personarepository.findPersonasDTO();
        
    }

    @Override
    public Optional<PersonaDTO2> findById(Long id) {
        return personarepository.findPersona(id);
    }
    
    @Override
    public Optional<PersonaEntity> findByValue(String value, String filtro) {
        if ("telefono".equals(filtro)){
            return personarepository.findByTelefono(value);
        }else{
            return personarepository.findByDoc(value);
        }
        
    }
    
    @Override
    public Optional<PersonaEntity> findByDoc(String doc) {
        return personarepository.findByDoc(doc);
    }

    @Override
    public PersonaEntity savePersona(PersonaCreationDTO personaCreationDTO) { 
        System.out.println("save persona");
        System.out.println(personaCreationDTO.getPersonaEntity().getNombre());
        PersonaEntity persona = personarepository.save(personaCreationDTO.getPersonaEntity());
        if(direccionService.algunCampoTieneValor(personaCreationDTO.getDireccion())){
            personaCreationDTO.getDireccion().setPersona(persona);
            direccionService.saveDireccion(personaCreationDTO.getDireccion());
        }
        
        return persona;
    }

    @Override
    public PersonaEntity updatePersona(PersonaCreationDTO personaCreationDTO, Long id) {
        PersonaEntity personaEntity = personaCreationDTO.getPersonaEntity();
        PersonaEntity updatedPersona = personarepository.findById(id).get();
        System.out.println(updatedPersona.getId());
        updatedPersona.setNombre(personaEntity.getNombre());
        updatedPersona.setApellido(personaEntity.getApellido());
        updatedPersona.setTipoDoc(personaEntity.getTipoDoc());
        updatedPersona.setEmail(personaEntity.getEmail());
        updatedPersona.setNroDoc(personaEntity.getNroDoc());
        updatedPersona.setTelefono(personaEntity.getTelefono());
        PersonaEntity newPersona = personarepository.save(updatedPersona);
        
        if(direccionService.algunCampoTieneValor(personaCreationDTO.getDireccion())){
            System.out.println("tiene valor?");
            if(personaCreationDTO.getDireccion().getId() != null){
                System.out.println("if");
                direccionService.updateDireccion(personaCreationDTO.getDireccion(), personaCreationDTO.getDireccion().getId());
            }else{
                System.out.println("else");
                personaCreationDTO.getDireccion().setPersona(updatedPersona);
                direccionService.saveDireccion(personaCreationDTO.getDireccion());
            }
        }       
        
        return newPersona;
        
    }

    @Override
    public void deletePersona(Long id) {
        PersonaEntity deletedPersona = personarepository.findById(id).get();
        deletedPersona.setActivo(Boolean.FALSE);
        personarepository.save(deletedPersona);
    }

    @Override
    public int countByTelefono(String telefono) {
        return personarepository.countByTelefono(telefono);
    }

    @Override
    public int countByNroDoc(String nroDoc) {
        return personarepository.countByNroDoc(nroDoc);
    }

    @Override
    public int countByEmail(String email) {
        return personarepository.countByEmail(email);
    }
    
    
    
}
