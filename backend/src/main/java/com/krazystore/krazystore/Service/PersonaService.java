/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.PersonaCreationDTO;
import com.krazystore.krazystore.DTO.PersonaDTO;
import com.krazystore.krazystore.DTO.PersonaDTO2;
import com.krazystore.krazystore.Entity.PersonaEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface PersonaService {
    List<PersonaDTO> findAll();
    Optional<PersonaDTO2> findById(Long id);
    Optional<PersonaEntity> findByDoc(String doc);
    Optional<PersonaEntity> findByValue(String value, String filtro);
    PersonaEntity savePersona(PersonaCreationDTO personaCreationDTO);
    PersonaEntity updatePersona(PersonaCreationDTO personaCreationDTO, Long id);
    void deletePersona(Long id);
    public List<PersonaDTO2> findPersonasDTO();
}
