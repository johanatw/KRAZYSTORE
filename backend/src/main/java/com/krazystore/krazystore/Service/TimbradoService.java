/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.TimbradoDTO;
import com.krazystore.krazystore.Entity.TimbradoEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface TimbradoService {
    public Optional<TimbradoEntity> getTimbradoVigente();
    public Optional<TimbradoEntity> getTimbradoByNroTimbrado(Integer timbrado);
    public String getNroFactura(TimbradoEntity timbrado);
    public TimbradoEntity updateTimbrado(Long id, TimbradoEntity timbrado);
    public TimbradoDTO getTimbradoDTOVigente();
    public Optional<TimbradoEntity> findById(Long id);
    public TimbradoEntity saveTimbrado(TimbradoEntity timbrado);
    public void deleteTimbrado(Long id);
    public List<TimbradoEntity> findAll();
}
