/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.DireccionEntity;
import com.krazystore.krazystore.Entity.PersonaEntity;

/**
 *
 * @author HP
 */
public class PersonaDTO2 {
    private PersonaEntity persona;
    private DireccionEntity direccion;

    public PersonaDTO2(PersonaEntity persona, DireccionEntity direccion) {
        this.persona = persona;
        this.direccion = direccion;
    }

    public PersonaDTO2() {
    }

    public PersonaEntity getPersona() {
        return persona;
    }

    public void setPersona(PersonaEntity persona) {
        this.persona = persona;
    }

    public DireccionEntity getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionEntity direccion) {
        this.direccion = direccion;
    }
    
    
}
