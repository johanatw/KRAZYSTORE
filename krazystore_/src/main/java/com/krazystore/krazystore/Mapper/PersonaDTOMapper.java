/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Mapper;

import com.krazystore.krazystore.DTO.PersonaDTO;
import com.krazystore.krazystore.Entity.PersonaEntity;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */
@Service
public class PersonaDTOMapper implements Function<PersonaEntity, PersonaDTO>{
    @Override
    public PersonaDTO apply(PersonaEntity personaEntity){
        return new PersonaDTO(
                    personaEntity.getId(),
                    personaEntity.getNombre(),
                    personaEntity.getApellido(),
                    personaEntity.getNombre()+' '+personaEntity.getApellido(),
                    personaEntity.getDireccion(),
                    personaEntity.getTipoDoc(),
                    personaEntity.getNroDoc(),
                    personaEntity.getTelefono()
        );
        
    }
    
}
