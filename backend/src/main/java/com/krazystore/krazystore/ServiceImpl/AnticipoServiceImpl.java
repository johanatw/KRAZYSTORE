/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.krazystore.krazystore.Events.AplicacionAnticiposEvent;
import com.krazystore.krazystore.Events.PedidoCanceladoEvent;
import Utils.TipoEvento;
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.AplicacionAnticipo;

import com.krazystore.krazystore.Repository.AnticipoRepository;
import com.krazystore.krazystore.Service.AnticipoService;


import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class AnticipoServiceImpl implements AnticipoService{
    

    private final AnticipoRepository anticipoRepository;
    
    
    

    public AnticipoServiceImpl(AnticipoRepository anticipoRepository) {
        this.anticipoRepository = anticipoRepository;
    }

    @Override
    public List<AnticipoEntity> findAll() {
        return anticipoRepository.findAllByOrderByIdDesc();
    }

    @Override
    public Optional<AnticipoEntity> findById(Long id) {
        return anticipoRepository.findById(id);
    }
    
    @Override
    public AnticipoEntity saveAnticipo(AnticipoEntity anticipoEntity) {
       
        return anticipoRepository.save(anticipoEntity);
        
    }
    
   
    @Transactional
    @Override
    public AnticipoEntity actualizarSaldoAnticipo(Long idAnticipo, int montoReembolsado, TipoEvento evento){
        AnticipoEntity anticipo= anticipoRepository.findById(idAnticipo)
                .orElseThrow(() -> new RuntimeException("Anticipo no encontrado"));
        
        if(evento.equals(TipoEvento.REEMBOLSO_CREADO)){
            anticipo.setReembolsado(anticipo.getReembolsado() + montoReembolsado);
            anticipo.setSaldo(anticipo.getSaldo() - montoReembolsado );
        }else{
            anticipo.setReembolsado(anticipo.getReembolsado() - montoReembolsado);
            anticipo.setSaldo(anticipo.getSaldo() + montoReembolsado );
        }
        
        return anticipoRepository.save(anticipo);
    }

    @Override
    public int deleteAnticipo(Long id) {
       AnticipoEntity anticipo = anticipoRepository.findById(id).get();

        anticipoRepository.deleteById(id);
     
        return 0;
    }   
    
    @Override
    public List<AplicacionAnticipo> findAnticiposAplicarByIdPedidoVenta(Long id) {
        System.out.println("Anticipo Service");
        return anticipoRepository.findAnticiposAplicarByIdPedidoVenta(id);
    }
    
    @Override
    public void deleteAnticipoReembolsos(Long id) {
        AnticipoEntity anticipo = anticipoRepository.findById(id).get();
        List<Long> reembolsosAnticipo = anticipoRepository.getReembolsosByIdAnticipo(id);
   
       // movimientoService.deleteMovimientosByReembolsos(reembolsosAnticipo);
        anticipoRepository.deleteReembolsosByIdAnticipo(id);

       // movimientoService.deleteMovimientosByAnticipo(anticipo);
       
        anticipoRepository.deleteById(id);
       
        
    }
    
    @Override
    public List<Long> getIdReembolsosAnticipo(Long id) {
        
        return anticipoRepository.getReembolsosByIdAnticipo(id);
   
    }
    
    @EventListener
    public void handleAplicacionAnticiposEvents(AplicacionAnticiposEvent event) {
        List<AnticipoEntity> anticiposActualizar = new ArrayList<>();
        List<AplicacionAnticipo> anticiposAplicados = event.getAnticiposAplicados();
        TipoEvento tipoEvento = event.getTipoEvento();
        
        anticiposAplicados.forEach((aa)->{
            AnticipoEntity anticipo = aa.getAnticipo();
            if(tipoEvento==TipoEvento.ANTICIPO_APLICADO){
                anticipo.setUtilizado((int) (anticipo.getUtilizado()+aa.getMonto()));
                anticipo.setSaldo((int) (anticipo.getSaldo()-aa.getMonto()));
            }else if(tipoEvento==TipoEvento.ANTICIPO_APLICADO_ELIMINADO){
                anticipo.setUtilizado((int) (anticipo.getUtilizado()-aa.getMonto()));
                anticipo.setSaldo((int) (anticipo.getSaldo()+aa.getMonto()));
            }
            anticiposActualizar.add(anticipo);
        });
        
        anticipoRepository.saveAll(anticiposActualizar);
    }
    
    @EventListener
    public void handlePedidoCanceladoEvent(PedidoCanceladoEvent evento) {
        Long idPedido = evento.getPedidoId();
        if(idPedido==null)
            throw new IllegalArgumentException("Id de pedido no puede ser null");
        
        List<Long> idAnticipos = anticipoRepository.findAnticiposByIdPedido(idPedido);
        
        if(!idAnticipos.isEmpty()){
            anticipoRepository.desvincularPedido(idAnticipos);
        }
        
    }
    
}
