/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.DTO.PersonaDTO;
import com.krazystore.krazystore.Repository.PersonaRepository;
import com.krazystore.krazystore.Service.PersonaService;
import com.krazystore.krazystore.Entity.PersonaEntity;
import com.krazystore.krazystore.Mapper.PersonaDTOMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class PersonaServiceImpl implements PersonaService{
    private final PersonaRepository personarepository;
    private final PersonaDTOMapper personaDTOMapper;
    

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
    public Optional<PersonaEntity> findById(Long id) {
        return personarepository.findById(id);
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
    public PersonaEntity savePersona(PersonaEntity personaEntity) { 
        
        return personarepository.save(personaEntity);
    }

    @Override
    public PersonaEntity updatePersona(PersonaEntity personaEntity, Long id) {
        PersonaEntity updatedPersona = personarepository.findById(id).get();
        System.out.println(updatedPersona.getId());
        updatedPersona.setNombre(personaEntity.getNombre());
        updatedPersona.setApellido(personaEntity.getApellido());
        updatedPersona.setDireccion(personaEntity.getDireccion());
        updatedPersona.setTipoDoc(personaEntity.getTipoDoc());
        updatedPersona.setEmail(personaEntity.getEmail());
        updatedPersona.setNroDoc(personaEntity.getNroDoc());
        updatedPersona.setTelefono(personaEntity.getTelefono());
        return personarepository.save(updatedPersona);
        
       
        
        
    }

    @Override
    public void deletePersona(Long id) {
        personarepository.deleteById(id);
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
