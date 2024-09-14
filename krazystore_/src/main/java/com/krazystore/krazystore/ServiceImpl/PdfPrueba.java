/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.ServiceImpl;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.springframework.stereotype.Service;

/**
 *
 * @author HP
 */

@Service
public class PdfPrueba {
    public void export(HttpServletResponse response) throws IOException{
        Document document = new Document(PageSize.A5);
        
        PdfWriter.getInstance(document, response.getOutputStream());
        
        
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

        
    }
}
