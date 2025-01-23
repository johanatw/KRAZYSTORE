/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.TipoPedido;
import com.krazystore.krazystore.Entity.AnticipoEntity;

import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
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
    public AnticipoEntity actualizarSaldoAnticipo(Long idAnticipo, int montoReembolsado){
        AnticipoEntity anticipo= anticipoRepository.findById(idAnticipo)
                .orElseThrow(() -> new RuntimeException("Anticipo no encontrado"));
        
        anticipo.setReembolsado(anticipo.getReembolsado() + montoReembolsado);
        anticipo.setSaldo(anticipo.getSaldo() - montoReembolsado );
        
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
    public int verificarAnticipoEstado(Long id) {
        
        boolean tieneReembolsos = anticipoRepository.existenReembolsosAsociados(id);
        
        if(tieneReembolsos){
            return 1;
        }
        
        return 0;
    }
    /*
    public void generarPdf(AnticipoEntity anticipoEntity, List<DetalleAnticipoEntity> detalles)throws IOException {
        Date fecha = new Date();
        //Long lastId = anticipoRepository.findLastAnticipo().getId();
        //String path = "src/main/resources/recibos/";
        String nombre = "src/main/resources/recibos/Recibo_" + anticipoEntity.getId()+".pdf";
        Document document = new Document(PageSize.A4);
        
        PdfWriter.getInstance(document, new FileOutputStream(nombre));
        
        
        document.open();

        document.setMargins(30f, 30f, 30f, 30f);
        
        
        Font fontParagraph = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontParagraph.setStyle(12);
        fontParagraph.setStyle( Font.NORMAL);
        
        Paragraph paragraph1 = new Paragraph("JOhana Carolia \nSan lorenzo", fontParagraph);
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        paragraph1.setSpacingAfter(10f);
        paragraph1.setSpacingBefore(1f);
  
        Image img = Image.getInstance("src/main/resources/logo.png");
        img.scaleAbsolute(80, 80);
        img.setAlignment(Image.ALIGN_LEFT);
        PdfPTable table1 = new PdfPTable(2);
        float secondcolWidth[]= {40f,60f};
        table1.setWidths(secondcolWidth);
        table1.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        PdfPCell cell2 = new PdfPCell();
        
        cell.addElement(img);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //cell.setBorder(0);
        cell2.addElement(paragraph1);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //cell2.setBorder(0);
        table1.addCell(cell);
        table1.addCell(cell2);
        
        table1.getDefaultCell().setBorder(0);
        //document.add(table1);
        
        float secondcolWidth_[]= {50f,50f};
        
        PdfPTable table2 = new PdfPTable(2);
         table2.setWidths(secondcolWidth_);
        table2.setWidthPercentage(100);
        
        PdfPCell cell3 = new PdfPCell();
        cell3.addElement(img);
        cell3.setBorder(0);
        cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
        table2.addCell(cell3);
        table2.setSpacingAfter(20f);
        Paragraph paragraph2 = new Paragraph();
        paragraph2.clear();
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontTitle.setStyle(Font.NORMAL);
        fontTitle.setSize(20f);
        fontTitle.setColor(Color.pink);
        
        
        paragraph2.setFont(fontTitle);
        paragraph2.add("Recibo N° "+ anticipoEntity.getId());
        paragraph2.setAlignment(Paragraph.ALIGN_RIGHT);
        paragraph2.setExtraParagraphSpace(0f);
        //table2.addCell("Recibo N° 1");
        
        PdfPCell cell4= new PdfPCell();
        
        cell4.addElement(paragraph2);
        cell4.setPaddingTop(0f);
        cell4.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell4.setBorder(0);
        table2.addCell(cell4);
        table2.setHorizontalAlignment(5);
        table2.completeRow();
        
        PdfPTable table3 = new PdfPTable(2);
        
        table3.setWidthPercentage(50);
        table3.setSpacingAfter(20f);
        table3.setHorizontalAlignment(Element.ALIGN_LEFT);
        table3.getDefaultCell().setBorder(0);
        //table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table3.addCell("Cliente: ");
        table3.addCell(anticipoEntity.getCliente().getNombre() + " "+ anticipoEntity.getCliente().getApellido());
        table3.addCell("Fecha: ");
        table3.addCell(anticipoEntity.getFecha().toString());
        table3.addCell("Telefono:");
        table3.addCell(anticipoEntity.getCliente().getTelefono());
        
        PdfPTable table4 = new PdfPTable(7);
        table4.setWidthPercentage(100);
        //table4.getDefaultCell().setBorder(0);
        
        table4.getDefaultCell().setBackgroundColor(Color.PINK);
        
        table4.addCell("Descripcion");
        table4.addCell("Cantidad");
        table4.addCell("Precio");
        table4.addCell("Total");
        table4.addCell("Seña");
        table4.addCell("Total Seña");
        table4.addCell("Importe");
        
        
        PdfPTable table6 = new PdfPTable(7);
        float[] widths2 = new float[] { 2f, 1f, 1f,1f,1f,1f,1f };
        table6.setWidthPercentage(100);
        table6.setWidths(widths2);
        detalles.forEach(d -> {
            
            table6.addCell(d.getProducto().getNombre());
            table6.addCell(String.valueOf(d.getCantidad()));
            table6.addCell(String.valueOf(d.getProducto().getPrecio()));
            table6.addCell(String.valueOf(d.getProducto().getPrecio() * d.getCantidad()));
            table6.addCell(String.valueOf(d.getProducto().getSeña()));
            table6.addCell(String.valueOf(d.getProducto().getSeña() * d.getCantidad()));
            table6.addCell(String.valueOf(d.getSubTotal()));
            
        });
        
        table6.setSpacingAfter(20f);
        
        
        PdfPTable table5 = new PdfPTable(2);
        table5.setWidthPercentage(40);
        table5.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table5.getDefaultCell().setBorder(0);
        table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        table5.addCell("Total: ");
        table5.addCell(String.valueOf(anticipoEntity.getTotal())+" Gs");
        
        document.add(table2);
        document.add(table3);
        document.add(table4);
        document.add(table6);
        document.add(table5);
        document.close();
        
        anticipoEntity.setNombre(nombre);
        
        
        anticipoRepository.save(anticipoEntity);
             
        
    
    }*/
    
    public AnticipoEntity generarPdf2(AnticipoEntity anticipoEntity)throws IOException {
        Date fecha = new Date();
        Long lastId = anticipoRepository.findLastAnticipo().getId();
        //String path = "src/main/resources/recibos/";
        String nombre = "src/main/resources/recibos/Recibo_" + lastId+1+".pdf";
        Document document = new Document(PageSize.A5);
        
        PdfWriter.getInstance(document, new FileOutputStream(nombre));
        
        
        document.open();

        document.setMargins(30f, 30f, 30f, 30f);
        
        
        Font fontParagraph = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontParagraph.setStyle(12);
        fontParagraph.setStyle( Font.NORMAL);
        
        Paragraph paragraph1 = new Paragraph("JOhana Carolia \nSan lorenzo", fontParagraph);
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        paragraph1.setSpacingAfter(10f);
        paragraph1.setSpacingBefore(1f);
  
        Image img = Image.getInstance("src/main/resources/logo.png");
        img.scaleAbsolute(80, 80);
        img.setAlignment(Image.ALIGN_LEFT);
        PdfPTable table1 = new PdfPTable(2);
        float secondcolWidth[]= {40f,60f};
        table1.setWidths(secondcolWidth);
        table1.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        PdfPCell cell2 = new PdfPCell();
        
        cell.addElement(img);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //cell.setBorder(0);
        cell2.addElement(paragraph1);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //cell2.setBorder(0);
        table1.addCell(cell);
        table1.addCell(cell2);
        
        table1.getDefaultCell().setBorder(0);
        //document.add(table1);
        
        float secondcolWidth_[]= {50f,50f};
        
        PdfPTable table2 = new PdfPTable(2);
         table2.setWidths(secondcolWidth_);
        table2.setWidthPercentage(100);
        
        PdfPCell cell3 = new PdfPCell();
        cell3.addElement(img);
        cell3.setBorder(0);
        cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
        table2.addCell(cell3);
        table2.setSpacingAfter(20f);
        Paragraph paragraph2 = new Paragraph();
        paragraph2.clear();
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontTitle.setStyle(Font.NORMAL);
        fontTitle.setSize(20f);
        fontTitle.setColor(Color.pink);
        
        
        paragraph2.setFont(fontTitle);
        paragraph2.add("Recibo N° 1");
        paragraph2.setAlignment(Paragraph.ALIGN_RIGHT);
        paragraph2.setExtraParagraphSpace(0f);
        //table2.addCell("Recibo N° 1");
        
        PdfPCell cell4= new PdfPCell();
        cell4.addElement(paragraph2);
        cell4.setPaddingTop(0f);
        cell4.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell4.setBorder(0);
        table2.addCell(cell4);
        table2.setHorizontalAlignment(5);
        table2.completeRow();
        
        PdfPTable table3 = new PdfPTable(2);
        
        table3.setWidthPercentage(50);
        table3.setSpacingAfter(20f);
        table3.setHorizontalAlignment(Element.ALIGN_LEFT);
        table3.getDefaultCell().setBorder(0);
        //table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table3.addCell("Cliente: ");
        table3.addCell("Johana");
        table3.addCell("Fecha: ");
        table3.addCell("2024/06/02");
        table3.addCell("Telefono:");
        table3.addCell("0985569264");
        
        PdfPTable table4 = new PdfPTable(5);
        table4.setWidthPercentage(100);
        //table4.getDefaultCell().setBorder(0);
        
        table4.getDefaultCell().setBackgroundColor(Color.PINK);
        Paragraph p = new Paragraph("Item", fontParagraph);
        p.setFont(fontTitle);
        table4.addCell(p);
        table4.addCell("Descripcion");
        table4.addCell("Precio");
        table4.addCell("Cantitdad");
        table4.addCell("Subtotal");
        
        
        PdfPTable table6 = new PdfPTable(5);
        table6.setWidthPercentage(100);
        table6.addCell("1");
        table6.addCell("Producto 1");
        table6.addCell("50.000");
        table6.addCell("1");
        table6.addCell("50.000");
        table6.addCell("2");
        table6.addCell("Producto 2");
        table6.addCell("40.000");
        table6.addCell("2");
        table6.addCell("80.000");
        table6.setSpacingAfter(20f);
        
        
        PdfPTable table5 = new PdfPTable(2);
        table5.setWidthPercentage(40);
        table5.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table5.getDefaultCell().setBorder(0);
        table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        table5.addCell("Total: ");
        table5.addCell("130.000 Gs");
        
        document.add(table2);
        document.add(table3);
        document.add(table4);
        document.add(table6);
        document.add(table5);
        document.close();
        
        
        anticipoEntity.setFecha(fecha);
        
        return anticipoEntity;
    
    }
    
    @Override
    public void getAnticipoPdf(HttpServletResponse response, Long idAnticipo) {
        /*
        try {
            File file = new File("src/main/resources/recibos/Recibo_"+idAnticipo+".pdf");
            InputStream in = new FileInputStream(file);
            org.apache.tomcat.util.http.fileupload.IOUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnticipoServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnticipoServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } */
        try {
        Date fecha = new Date();
        //Long lastId = anticipoRepository.findLastAnticipo().getId();
        //String path = "src/main/resources/recibos/";
        String nombre = "src/main/resources/recibos/Recibo_" + idAnticipo +".pdf";
        Document document = new Document(PageSize.A6);
        document.getPageSize().setRotation(90);
        
        
        
        PdfWriter.getInstance(document, response.getOutputStream());
        
        document.open();

        document.setMargins(20f, 20f, 20f, 20f);
        
        
        Font fontParagraph = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontParagraph.setStyle(12);
        fontParagraph.setStyle( Font.NORMAL);
        
        Font fontParagraph2 = FontFactory.getFont(FontFactory.TIMES);
        fontParagraph2.setStyle(11);
        fontParagraph2.setStyle( Font.NORMAL);
        
  
        Image img = Image.getInstance("src/main/resources/logo.png");
        img.scaleAbsolute(80, 80);
        img.setAlignment(Image.ALIGN_LEFT);
        PdfPTable table1 = new PdfPTable(2);
        float secondcolWidth[]= {40f,60f};
        table1.setWidths(secondcolWidth);
        table1.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        PdfPCell cell2 = new PdfPCell();
        
        cell.addElement(img);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //cell.setBorder(0);
  
        //cell2.setBorder(0);
        table1.addCell(cell);
       
        
                
        table1.getDefaultCell().setBorder(0);
        //document.add(table1);
        
        float secondcolWidth_[]= {50f,50f};
        float colors[] = null;
        PdfPTable table2 = new PdfPTable(2);
         table2.setWidths(secondcolWidth_);
        table2.setWidthPercentage(100);
        
        PdfPCell cell3 = new PdfPCell();
        cell3.addElement(img);
        cell3.setBorder(0);
        cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
        
        table2.addCell(cell3);
        table2.setSpacingAfter(20f);
        Paragraph paragraph2 = new Paragraph();
        paragraph2.clear();
        
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontTitle.setStyle(Font.NORMAL);
        fontTitle.setSize(20f);
        colors = Color.RGBtoHSB(142, 42, 121, colors);
        
        fontTitle.setColor(Color.getHSBColor(colors[0],colors[1],colors[2]));
        
        
        paragraph2.setFont(fontTitle);
        paragraph2.add("Recibo N° "+ 10);
        paragraph2.setAlignment(Paragraph.ALIGN_RIGHT);
        paragraph2.setExtraParagraphSpace(0f);
        //table2.addCell("Recibo N° 1");
        
        PdfPCell cell4= new PdfPCell();
        
        cell4.addElement(paragraph2);
        cell4.setPaddingTop(0f);
        cell4.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell4.setBorder(0);
        table2.addCell(cell4);
        table2.setHorizontalAlignment(5);
        table2.completeRow();
        
        PdfPTable table3 = new PdfPTable(2);
        
        table3.setWidthPercentage(50);
        table3.setSpacingAfter(20f);
        table3.setHorizontalAlignment(Element.ALIGN_LEFT);
        table3.getDefaultCell().setBorder(0);
        //table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        
        Font fontH1 = new Font();

        fontH1.setSize(11f);

table3.addCell(new PdfPCell(new Phrase("sueño",fontH1))).setBorder(0);
        Font fontText = FontFactory.getFont(FontFactory.TIMES);
        fontText.setStyle(Font.NORMAL);
        fontText.setSize(9f);
        Phrase p1 = new Phrase("hola");
        p1.setFont(fontText);
        Paragraph p = new Paragraph("Cliente: ");
        p.setFont(fontText);
        
        table3.addCell(p1);
        
        table3.addCell("johana");
        table3.addCell("Fecha: ");
        table3.addCell("09/02/2024");
        table3.addCell("Telefono:");
        table3.addCell("098555566226");
        
        float[] widths2 = new float[] { 3f, 1f };
        PdfPTable table4 = new PdfPTable(2);
        table4.setWidthPercentage(100);
        table4.setWidths(widths2);
        //table4.getDefaultCell().setBorder(0);
        
        table4.getDefaultCell().setBackgroundColor(Color.magenta);
        
        table4.addCell("Descripcion");
        table4.addCell("Importe");
        
        
        PdfPTable table6 = new PdfPTable(2);
        
        table6.setWidthPercentage(100);
        table6.setWidths(widths2);
        
            
            table6.addCell("Pago de anticipo");
            table6.addCell("60000");
         
       
        
        table6.setSpacingAfter(20f);
        
        
        PdfPTable table5 = new PdfPTable(2);
        table5.setWidthPercentage(40);
        table5.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table5.getDefaultCell().setBorder(0);
        table5.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        table5.addCell("Total: ");
        table5.addCell("50000 Gs");
        
        document.add(table2);
        document.add(table3);
        document.add(table4);
        document.add(table6);
        document.add(table5);
        document.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnticipoServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnticipoServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
  
    }
    
    @Override
    public List<AnticipoEntity> findByIdPedido(Long id, TipoPedido tipoPedido) {
        return anticipoRepository.findByIdPedido(id, tipoPedido.getCodigo());
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
    
    
}
