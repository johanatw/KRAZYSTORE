/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.PersonaCreationDTO;
import com.krazystore.krazystore.DTO.PersonaDTO;
import com.krazystore.krazystore.DTO.PersonaDTO2;
import com.krazystore.krazystore.Service.PersonaService;
import com.krazystore.krazystore.Entity.PersonaEntity;
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
    public Optional<PersonaDTO2> findPersonaById(@PathVariable("id") Long id) {
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
    public PersonaEntity savePersona(@RequestBody PersonaCreationDTO personaCreation) {
            System.out.println("controller persona");
            System.out.println(personaCreation.getPersonaEntity());
            return personaService.savePersona(personaCreation);

    }

    @PutMapping("/{id}")
    public PersonaEntity updatePersona(@PathVariable long id, @RequestBody PersonaCreationDTO personaCreation) {
        
            return personaService.updatePersona(personaCreation, id);
        
    }

    @DeleteMapping("/{id}")
    public void deletePersona(@PathVariable("id") Long id) {
        personaService.deletePersona(id);
    }

    @GetMapping("/clientes")
    public List<PersonaDTO2> findPersonasDTO() {
        return personaService.findPersonasDTO();
    }

    
}
