/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.PersonaDTO2;
import com.krazystore.krazystore.Entity.PersonaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface PersonaRepository extends JpaRepository<PersonaEntity, Long> {

    public List<PersonaEntity> findByOrderByIdAsc();
    
    @Query(
  value = "SELECT * FROM personas u order by u.id ASC", 
  nativeQuery = true)
    public List<PersonaEntity> find();

    @Query(
  value = "SELECT * FROM personas u WHERE u.nro_doc = ?1 ", 
  nativeQuery = true)
    public Optional<PersonaEntity> findByDoc(String doc);
    
    @Query(
   "SELECT p FROM PersonaEntity p LEFT JOIN FETCH p.tipoDoc WHERE p.telefono = ?1 ")
    public Optional<PersonaEntity> findByTelefono(String value);
    
    @Query("SELECT p FROM PersonaEntity p LEFT JOIN FETCH p.tipoDoc WHERE p.activo = TRUE ")
    List<PersonaEntity> findPersonas();

    public int countByTelefono(String telefono);

    public int countByNroDoc(String nroDoc);

    public int countByEmail(String email);
    
    @Query(
    "SELECT new com.krazystore.krazystore.DTO.PersonaDTO2(p, d)FROM PersonaEntity p "
           + "LEFT JOIN DireccionEntity d "
            + "on d.persona = p and d.tipo = 'P' "
           )
    public List<PersonaDTO2> findPersonasDTO();
    
    @Query(
    "SELECT new com.krazystore.krazystore.DTO.PersonaDTO2(p, d)FROM PersonaEntity p "
           + "LEFT JOIN DireccionEntity d "
            + "on d.persona = p and d.tipo = 'P' "
            + "where p.id = ?1"
           )
    public Optional<PersonaDTO2> findPersona(long id);
   
}
