/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.PersonaDTO;
import com.krazystore.krazystore.Service.PersonaService;
import com.krazystore.krazystore.Entity.PersonaEntity;
import com.krazystore.krazystore.Mapper.PersonaDTOMapper;
import com.krazystore.krazystore.exception.BadRequestException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/personas")
public class PersonaController {
    private final PersonaService personaService;
   

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    public List<PersonaDTO> findAll() {
        return personaService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<PersonaEntity> findPersonaById(@PathVariable("id") Long id) {
        return personaService.findById(id);
    }
    
    @GetMapping("/ci")
    public Optional<PersonaEntity> findPersonaByDoc(@RequestParam(value="ci") String ci) {
        return personaService.findByDoc(ci);
    }
     
    @GetMapping("/valor")
    public Optional<PersonaEntity> findByValue(@RequestParam(value="value") String value, @RequestParam(value="filtro") String filtro){
        return personaService.findByValue(value, filtro);
    }
    
    @PostMapping
    public PersonaEntity savePersona(@RequestBody @Valid PersonaEntity personaEntity) {
         
        try {
           
            return personaService.savePersona(personaEntity);
            
        } catch (DataAccessException exDt) {
           String message = exDt.getMessage();
           
           if(message.contains("duplicate key") && message.contains("(email)")){
               message = "Ya existe un cliente con el mismo Email";
           }else if (message.contains("duplicate key") && message.contains("(nro_doc)")){
               message = "Ya existe un cliente con el mismo N° de Documento";
           }else if (message.contains("duplicate key") && message.contains("(telefono)")){
               message = "Ya existe un cliente con el mismo N° de Telefono";
           }
            throw  new BadRequestException(message);
        }
        
        
    }

    @PutMapping("/{id}")
    public PersonaEntity updatePersona(@PathVariable long id, @RequestBody @Valid PersonaEntity persona) {
        try {
            return personaService.updatePersona(persona, id);
        } catch (DataAccessException exDt) {
           String message = exDt.getMessage();
           
           if(message.contains("duplicate key") && message.contains("(email)")){
               message = "Ya existe un cliente con el mismo Email";
           }else if (message.contains("duplicate key") && message.contains("(nro_doc)")){
               message = "Ya existe un cliente con el mismo N° de Documento";
           }else if (message.contains("duplicate key") && message.contains("(telefono)")){
               message = "Ya existe un cliente con el mismo N° de Telefono";
           }
            throw  new BadRequestException(message);
        }
    }

    @DeleteMapping("/{id}")
    public void deletePersona(@PathVariable("id") Long id) {
        personaService.deletePersona(id);
    }

 


}
