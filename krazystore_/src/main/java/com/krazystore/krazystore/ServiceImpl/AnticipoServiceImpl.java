/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.AplicacionAnticiposEvent;
import Utils.TipoEvento;
import Utils.TipoPedido;
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.AplicacionAnticipo;

import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import com.krazystore.krazystore.Repository.AnticipoRepository;
import com.krazystore.krazystore.Service.AnticipoService;


import com.krazystore.krazystore.Service.PedidoService;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import static com.lowagie.text.pdf.BaseFont.COURIER;
import com.lowagie.text.pdf.PdfDocument;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    /*@Override
    public String saveAnticipo(AnticipoEntity anticipoEntity) {
        
        AnticipoEntity anticipo;
        try {
            anticipo = generarPdf(anticipoEntity);
             //pdf = Files.readAllBytes(new File(anticipo.getNombre()).toPath());
             File pdf = new File(anticipo.getNombre());
             String parentDir = pdf.getPath();
             System.out.println(parentDir);
            return parentDir;
        } catch (IOException ex) {
            Logger.getLogger(AnticipoServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }*/
    
    @Override
    public AnticipoEntity saveAnticipo(AnticipoEntity anticipoEntity) {
       
        return anticipoRepository.save(anticipoEntity);
        
    }

    @Override
    public AnticipoEntity updateAnticipo(AnticipoEntity anticipoEntity, Long id) {

        AnticipoEntity updatedAnticipo = anticipoRepository.findById(id).get();
        updatedAnticipo.setReembolsado(anticipoEntity.getReembolsado());
        updatedAnticipo.setSaldo(anticipoEntity.getSaldo());
        AnticipoEntity newAnticipo = anticipoRepository.save(updatedAnticipo);  

        return newAnticipo;
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
    public List<AnticipoEntity> updateAnticipos(List<AnticipoEntity> anticipos) {
        
        return anticipoRepository.saveAll(anticipos);
    }

    @Override
    public int deleteAnticipo(Long id) {
       AnticipoEntity anticipo = anticipoRepository.findById(id).get();

       // movimientoService.deleteMovimientosByAnticipo(anticipo);
        anticipoRepository.deleteById(id);
     
        return 0;
    }   
    
    @Override
    public List<AplicacionAnticipo> findAnticiposAplicarByIdPedidoVenta(Long id) {
        System.out.println("Anticipo Service");
        return anticipoRepository.findAnticiposAplicarByIdPedidoVenta(id);
    }
    
    @Override
    public boolean existsByPedido(PedidoEntity p) {
        
        //return anticipoRepository.existsByPedido(p);
        return false;
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
    public void deleteReembolsosByIdAnticipo(Long id) {
       anticipoRepository.deleteReembolsosByIdAnticipo(id);

    }
    
    @Override
    public List<Long> getIdReembolsosAnticipo(Long id) {
        
        return anticipoRepository.getReembolsosByIdAnticipo(id);
   
    }
    
 

    @Override
    public AnticipoEntity updateAnticipo(AnticipoEntity asnticipoEntity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
    
}
