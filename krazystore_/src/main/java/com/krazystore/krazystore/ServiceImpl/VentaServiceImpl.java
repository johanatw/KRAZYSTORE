/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import Utils.NumberToLetterConverter;
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.ConceptoEntity;
import com.krazystore.krazystore.Entity.DetallePedidoEntity;
import com.krazystore.krazystore.Entity.DetalleVentaEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import com.krazystore.krazystore.Entity.PersonaEntity;
import com.krazystore.krazystore.Entity.TimbradoEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import com.krazystore.krazystore.Repository.VentaRepository;
import com.krazystore.krazystore.Service.AnticipoService;
import com.krazystore.krazystore.Service.DetallePedidoService;
import com.krazystore.krazystore.Service.DetalleVentaService;
import com.krazystore.krazystore.Service.MovimientoService;
import com.krazystore.krazystore.Service.PagoService;
import com.krazystore.krazystore.Service.PedidoService;
import com.krazystore.krazystore.Service.TimbradoService;
import com.krazystore.krazystore.Service.VentaService;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;

import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.File;
import java.text.SimpleDateFormat;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Service
public class VentaServiceImpl implements VentaService{
    private final VentaRepository ventarepository;

    public VentaServiceImpl(VentaRepository ventarepository) {
        this.ventarepository = ventarepository;
    }
    @Autowired
    private DetalleVentaService detalleVentaService;
    
    
    @Autowired
    private MovimientoService movimientoService;
    
    
    @Autowired
    private TimbradoService timbradoService;
    
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private DetallePedidoService detallePedidoService;
    
    @Override
    public List<VentaEntity> findAll() {
        return ventarepository.findAllByOrderByIdDesc();
    }

    @Override
    public Optional<VentaEntity> findById(Long id) {
        return ventarepository.findById(id);
    }

    @Override
    public VentaEntity saveVenta(VentaEntity ventaEntity, List<DetalleVentaEntity> detalle, List<PagoEntity> pagos) {
        TimbradoEntity timbrado = timbradoService.getTimbradoVigente().get();
        ventaEntity.setTimbrado(timbrado);
        ventaEntity.setNroFactura(timbradoService.getNroFactura(timbrado));
        VentaEntity venta = ventarepository.save(ventaEntity);
        timbrado.setCantUtilizada(timbrado.getCantUtilizada() + 1);
        timbrado.setUltimoRemitido(timbrado.getUltimoRemitido()+1);
        timbradoService.updateTimbrado(timbrado);
        
        detalleVentaService.saveDetalleVenta(venta, detalle);
        
        movimientoService.saveMovimiento(venta);
        
        if(venta.getPedido() != null){
            PedidoEntity pedido = venta.getPedido();
            detallePedidoService.updateDetallesFacturadas(detalle, pedido, "REGISTRAR");
        }
        
        try {
            generarPdf(venta);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VentaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return venta;
    }

    @Override
    public VentaEntity updateVenta(VentaEntity ventaEntity, Long id) {
        
        VentaEntity updatedVenta = ventarepository.findById(id).get();
        
        updatedVenta.setCliente(ventaEntity.getCliente());
        updatedVenta.setFecha(ventaEntity.getFecha());
        updatedVenta.setMontoIva(ventaEntity.getMontoIva());
        updatedVenta.setMontoTotal(ventaEntity.getMontoTotal());

        return ventarepository.save(updatedVenta);
    }
    
    @Transactional
    @Override
    public int anularFactura(Long id) {
        
        VentaEntity facturaVenta = ventarepository.findById(id).get();
        Boolean pagado = movimientoService.getEstadoPago(facturaVenta);
        
        if(pagado){
            return 1;
        }
        movimientoService.anularVenta(facturaVenta);
            
        facturaVenta.setActivo(false);
        List<DetalleVentaEntity> detalleVenta = detalleVentaService.findByIdVenta(id);



        if(facturaVenta.getPedido() != null){
            PedidoEntity pedido = facturaVenta.getPedido();
            detallePedidoService.updateDetallesFacturadas(detalleVenta, pedido, "ELIMINAR");

        }
        
        return 0;
       
    }

    @Override
    public void deleteVenta(Long id) {
        ventarepository.deleteById(id);
    }
    
    public void generarPdf(VentaEntity venta) throws FileNotFoundException {
        
        try {
        Date fecha = new Date();
        
        String nombre = "src/main/resources/recibos/Recibo_" + venta.getNroFactura() +".pdf";
        Document document = new Document(PageSize.A4);
        document.getPageSize().setRotation(90);
        
        PdfWriter.getInstance(document, new FileOutputStream(nombre));
        
        //PdfWriter.getInstance(document, response.getOutputStream());
        
        document.open();

        document.setMargins(20f, 20f, 20f, 20f);
        
        generarCabecera(venta, document);
        generarDatosEmision(venta, document);
        generarDetalle(venta, document);
        generarSubTotales(venta, document);
        /*
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
        document.add(table5);*/
        document.close();
        } catch (BadElementException ex) {
            //Logger.getLogger(AnticipoServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
  
    }
    
    public void generarCabecera(VentaEntity venta, Document document) throws BadElementException {
        
        try {
            // Crear una tabla principal con 2 columnas
            PdfPTable mainTable = new PdfPTable(2);
            float secondcolWidth[]= {60f,40f};
            mainTable.setWidths(secondcolWidth);
            mainTable.setWidthPercentage(100);
            
            // Configurar el espacio entre las dos tablas
            //mainTable.setSpacingBefore(2f);
            //mainTable.setSpacingAfter(2f);

            // Crear la primera tabla (por ejemplo, tabla1)
            PdfPTable table1 = new PdfPTable(2); // Primera tabla con 2 columnas
            float secondcolWidth2[]= {40f,60f};
            table1.setWidths(secondcolWidth2);
            table1.setWidthPercentage(100);
            
            Image img = Image.getInstance("src/main/resources/logo.png");
            img.scaleAbsolute(85, 60);
            img.setAlignment(Image.ALIGN_CENTER);
            
            PdfPCell cell3 = new PdfPCell();
            cell3.addElement(img);
            cell3.setBorder(PdfPCell.NO_BORDER);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 9);
            Font font2 = FontFactory.getFont(FontFactory.HELVETICA, 9);
            PdfPTable table4 = new PdfPTable(1); // Primera tabla con 2 columnas
            table4.setWidthPercentage(100);
            
            table1.addCell(cell3);
            table4.addCell(new PdfPCell(new Paragraph("OTROS COMERCIOS AL POR MENOR DE PRODUCTOS NUEVOS N.C.P. EN ALMACENES ESPECIALIZADOS", font))).setBorder(PdfPCell.NO_BORDER);
            PdfPCell cell5 = new PdfPCell();
            cell5.addElement(new Paragraph("Cel.: (0981) 482 153\nAvda. Pozo Favorito - Asunción, Paraguay", font2));
            cell5.setBorder(PdfPCell.NO_BORDER);                  
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(cell5);
            //table4.addCell(new PdfPCell(new Paragraph("10 de Agosto N° 338 - Asunción, Paraguay", font2))).setBorder(PdfPCell.NO_BORDER);
            
            PdfPCell cell4 = new PdfPCell();
            cell4.addElement(table4);
            cell4.setBorder(PdfPCell.NO_BORDER);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(cell4);
            // Crear la segunda tabla (por ejemplo, tabla2)
            PdfPTable table2 = new PdfPTable(1); // Segunda tabla con 2 columnas
            table2.setWidthPercentage(100);
            getDatosTimbrado(venta, table2);
            
            // Crear la segunda tabla (por ejemplo, tabla2)
            PdfPTable table3 = new PdfPTable(1); // Segunda tabla con 2 columnas
            table3.setWidthPercentage(100);
            table3.addCell(table1);
            

            // Crear celdas para cada tabla en la tabla principal
            PdfPCell cell1 = new PdfPCell();
            cell1.addElement(table3); // Agregar la primera tabla dentro de la primera celda
            
            
            cell1.setBorder(PdfPCell.NO_BORDER);
            
            PdfPCell cell2 = new PdfPCell();
            cell2.addElement(table2); // Agregar la segunda tabla dentro de la segunda celda
            cell2.setBorder(PdfPCell.NO_BORDER); // Opcional: quitar el borde de la celda

            // Agregar celdas a la tabla principal
            mainTable.addCell(cell1);
            mainTable.addCell(cell2);
            mainTable.setSpacingBefore(5f);
            mainTable.setSpacingAfter(5f);

            // Agregar la tabla principal al documento
            document.add(mainTable);
            

            
            //document.add(table2);
            
        } catch (IOException ex) {
            Logger.getLogger(VentaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
    }
    
    public void generarDatosEmision(VentaEntity venta, Document document) throws BadElementException {
        
            
            PersonaEntity persona = venta.getCliente();
            // Crear una tabla principal con 2 columnas
            PdfPTable mainTable = new PdfPTable(2);
            float secondcolWidth2[]= {50f,50f};
            mainTable.setWidths(secondcolWidth2);
            mainTable.setWidthPercentage(100);
            
            PdfPTable mainTable2 = new PdfPTable(1);
            mainTable2.setWidthPercentage(100);
            
            PdfPTable mainTable3 = new PdfPTable(1);
            mainTable3.setWidthPercentage(100);
            
           
            Font font2 = FontFactory.getFont(FontFactory.HELVETICA, 10);
            
            // Formato sin hora
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
            String fechaEmision = formatoFecha.format(venta.getFecha());
        
            String texto = "Fecha de Emisión: "+ fechaEmision;
       
            mainTable.addCell(new PdfPCell(new Paragraph(texto,font2)));
            mainTable.addCell(new PdfPCell(new Paragraph("Cond. de Venta:    \t\t\tContado(X)    \t\t\tCrédito( )",font2)));
            
            // Crear una celda para contener la tabla mainTable
            PdfPCell cell = new PdfPCell();
            cell.addElement(mainTable);
            cell.setBorder(PdfPCell.NO_BORDER);  // Quitar el borde de la celda
            
            
            // Crear la primera tabla (por ejemplo, tabla1)
            PdfPTable table1 = new PdfPTable(2); // Primera tabla con 2 columnas
            table1.setWidthPercentage(100);
            
            
            
            PdfPTable table2 = new PdfPTable(1); // Primera tabla con 2 columnas
            table2.setWidthPercentage(100);
            
            table1.addCell(new PdfPCell(new Paragraph("RUC/CI N°: "+ persona.getNroDoc(),font2 ))).setBorder(PdfPCell.NO_BORDER);
            if(persona.getDireccion() != null){
                table1.addCell(new PdfPCell(new Paragraph("Dirección: "+ persona.getDireccion(),font2 ))).setBorder(PdfPCell.NO_BORDER);
            }else{
                table1.addCell(new PdfPCell(new Paragraph("Dirección: ",font2 ))).setBorder(PdfPCell.NO_BORDER);
            }
            
            if(persona.getApellido()!= null ){
                table1.addCell(new PdfPCell(new Paragraph("Nombre o Razón Social: " +persona.getNombre()+" "+persona.getApellido(),font2 ))).setBorder(PdfPCell.NO_BORDER);
            }else{
                table1.addCell(new PdfPCell(new Paragraph("Nombre o Razón Social: " +persona.getNombre(),font2 ))).setBorder(PdfPCell.NO_BORDER);
            }
            
            if(persona.getTelefono()!= null ){
                table1.addCell(new PdfPCell(new Paragraph("Teléfono: "+persona.getTelefono(),font2))).setBorder(PdfPCell.NO_BORDER);
            }else{
                table1.addCell(new PdfPCell(new Paragraph("Teléfono: ",font2))).setBorder(PdfPCell.NO_BORDER);
            }
            
           
            PdfPCell cell7 = new PdfPCell();
            cell7.addElement(table1);
            cell7.setColspan(2);
            
            mainTable.addCell(cell7);
            
            PdfPCell cell1 = new PdfPCell();
            cell1.addElement(mainTable);
            cell1.setBorder(PdfPCell.NO_BORDER);
                        
            
            
            mainTable2.addCell(cell1);
            
            //PdfPCell cell2 = new PdfPCell();
            //cell2.addElement(mainTable2);
            //cell2.setBorder(PdfPCell.NO_BORDER);
            
            //mainTable3.addCell(cell);
            //mainTable3.addCell(cell2);
           
            // Agregar la tabla principal al documento
           mainTable2.setSpacingAfter(5f);
            document.add(mainTable2);

        
    }
    
    public void generarDetalle(VentaEntity venta, Document document) throws BadElementException {
            List<DetalleVentaEntity> detalle = detalleVentaService.findByIdVenta(venta.getId());
            PdfPTable mainTable = new PdfPTable(1);
            mainTable.setWidthPercentage(100);
            Font font2 = FontFactory.getFont(FontFactory.HELVETICA, 10);
           // Crear una tabla de 4 columnas
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            
            // Crear una celda que abarque 2 filas (rowspan)
            PdfPCell cell1 = new PdfPCell(new Paragraph("Cantidad",font2));
            cell1.setRowspan(2);  // Ocupa 2 filas
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);  // Alineación vertical
            
            // Crear una celda que abarque 2 filas (rowspan)
            PdfPCell cell2 = new PdfPCell(new Paragraph("Descripción",font2));
            cell2.setRowspan(2);  // Ocupa 2 filas
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);  // Alineación vertical
            
            // Crear una celda que abarque 2 filas (rowspan)
            PdfPCell cell3 = new PdfPCell(new Paragraph("Precio Unitario",font2));
            cell3.setRowspan(2);  // Ocupa 2 filas
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);  // Alineación vertical
            
            // Agregar otras celdas a la misma fila
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);

            // Crear una celda que abarque 2 columnas (colspan)
            PdfPCell cell4 = new PdfPCell(new Paragraph("Valor de venta",font2));
            cell4.setColspan(3);  // Ocupa 2 columnas
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);  // Alineación centrada
            
            table.addCell(cell4);


            // Agregar más celdas
            table.addCell(new PdfPCell(new Paragraph("Exentas",font2)));
            table.addCell(new PdfPCell(new Paragraph("5%",font2)));
            table.addCell(new PdfPCell(new Paragraph("10%",font2)));
            

            // Agregar la tabla al documento
            
            for(DetalleVentaEntity d : detalle)
            {
                table.addCell(new PdfPCell(new Paragraph(Integer.toString(d.getCantidad()),font2)));
                table.addCell(new PdfPCell(new Paragraph(d.getProducto().getNombre(),font2)));
                table.addCell(new PdfPCell(new Paragraph(Integer.toString(d.getProducto().getPrecio()),font2)));
                table.addCell(new PdfPCell(new Paragraph(" ",font2)));
                table.addCell(new PdfPCell(new Paragraph(" ",font2)));
                table.addCell(new PdfPCell(new Paragraph(Integer.toString(d.getSubTotal()),font2)));
            }
            PdfPCell cell = new PdfPCell();
            cell.addElement(table);
            cell.setBorder(PdfPCell.NO_BORDER);
            mainTable.addCell(cell);
            
            mainTable.setSpacingAfter(5f);
            document.add(mainTable);
        
    }
    
    public void generarSubTotales(VentaEntity venta, Document document) throws BadElementException {
            
            PdfPTable mainTable = new PdfPTable(1);
            mainTable.setWidthPercentage(100);
            Font font2 = FontFactory.getFont(FontFactory.HELVETICA, 10);
           // Crear una tabla de 4 columnas
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            
            // Crear una celda que abarque 2 filas (rowspan)
            PdfPCell cell1 = new PdfPCell(new Paragraph("SUBTOTALES", font2));
            cell1.setColspan(5);  // Ocupa 2 filas
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);  // Alineación vertical
            
            table.addCell(cell1);
            table.addCell(new PdfPCell(new Paragraph(Integer.toString(venta.getMontoTotal()),font2 )));
            
            // Crear una celda que abarque 2 filas (rowspan)
            String monto = NumberToLetterConverter.convertNumberToLetter(venta.getMontoTotal());
            PdfPCell cell2 = new PdfPCell(new Paragraph("TOTAL A PAGAR    "+monto,font2));
            cell2.setColspan(5);  // Ocupa 2 filas
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);  // Alineación vertical
            
            table.addCell(cell2);
            table.addCell(new PdfPCell(new Paragraph(Integer.toString(venta.getMontoTotal()),font2 )));
            
            PdfPTable table2 = new PdfPTable(2);
            float secondcolWidth2[]= {60f,40f};
            table2.setWidths(secondcolWidth2);
            table2.setWidthPercentage(100);
            
            // Crear una celda que abarque 2 filas (rowspan)
            PdfPCell cell3 = new PdfPCell(new Paragraph("LIQUIDACION DEL IVA  \t\t\t5%            \t\t\t\t\t\t\t\t\t\t\t\t\t\t10%  "+ Integer.toString(venta.getMontoIva()),font2));
            //cell3.setColspan(4);  // Ocupa 2 filas
           // cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);  // Alineación vertical
            
            // Agregar otras celdas a la misma fila
            table2.addCell(cell3).setBorder(PdfPCell.NO_BORDER);

            // Crear una celda que abarque 2 columnas (colspan)
            PdfPCell cell4 = new PdfPCell(new Paragraph("TOTAL IVA: " + Integer.toString(venta.getMontoIva()),font2));
            //cell4.setColspan(2);  // Ocupa 2 columnas
            //cell4.setHorizontalAlignment(Element.ALIGN_LEFT);  // Alineación centrada
            
            table2.addCell(cell4).setBorder(PdfPCell.NO_BORDER);
            
            PdfPCell cell5 = new PdfPCell(table2);
            cell5.setColspan(6);
            //cell5.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell5);
            table.addCell(new PdfPCell(new Paragraph(" " )));
            table.addCell(new PdfPCell(new Paragraph(" " )));
            table.addCell(new PdfPCell(new Paragraph(" " )));
            
            

            PdfPCell cell = new PdfPCell();
            cell.addElement(table);
            cell.setBorder(PdfPCell.NO_BORDER);
            mainTable.addCell(cell);
            
            mainTable.setSpacingAfter(5f);
            document.add(mainTable);
            
        
    }
    
    public void getDatosTimbrado(VentaEntity venta, PdfPTable table){
        TimbradoEntity timbrado = venta.getTimbrado();
        
       // Formato sin hora
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
        String fechaInicio = formatoFecha.format(timbrado.getVigenciaInicio());
        String fechaFin = formatoFecha.format(timbrado.getVigenciaFin());
        
        // Crear una fuente con el tamaño deseado (por ejemplo, tamaño 11)
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        
        // Texto que deseas agregar
        String texto = "Timbrado N° "+ Integer.toString(timbrado.getNumeroTimbrado()) +
                "\nFecha Inicio Vigencia: "+ fechaInicio + 
                "\nFecha Fin Vigencia: " + fechaFin +
                "\nFACTURA \n\n"+ venta.getNroFactura();
        
        // Crear un párrafo con la fuente y el texto
        Paragraph paragraph = new Paragraph(texto, font);
                
        // Crear una celda con el texto
        PdfPCell cell = new PdfPCell(new Paragraph(texto, font));
            
        // Centrar el contenido en la celda
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);

        // Eliminar bordes de la celda (opcional)
       // cell.setBorder(PdfPCell.NO_BORDER);
        
        //Agrega la celda a tabla
        table.addCell(cell);      
        
    }
    
    @Override
    public void getFacturaPdf(HttpServletResponse response, Long id) {
        
        try {
            VentaEntity venta = ventarepository.findById(id).get();
                    
            File file = new File("src/main/resources/recibos/Recibo_"+venta.getNroFactura()+".pdf");
            InputStream in = new FileInputStream(file);
            org.apache.tomcat.util.http.fileupload.IOUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnticipoServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnticipoServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
