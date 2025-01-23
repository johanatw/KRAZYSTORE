/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.AjusteCreationDTO;
import com.krazystore.krazystore.DTO.AjusteDTO;
import com.krazystore.krazystore.DTO.DetalleAjusteDTO;
import com.krazystore.krazystore.Entity.AjusteStock;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface AjusteService {
    List<AjusteStock> findAll();
    AjusteCreationDTO findById(Long id);
    AjusteStock saveAjuste (AjusteCreationDTO ajusteDTO) throws Exception;
    AjusteStock updateAjuste(AjusteCreationDTO ajusteDTO, Long id)throws Exception;
    void deleteAjuste(Long id);
    List<DetalleAjusteDTO> obtenerProductosParaAjuste();
    AjusteStock ajustar(Long id);
}
