/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.ServiceImpl.PdfPrueba;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/pdf")
public class PdfControladorPrueba {
    private final PdfPrueba pdfPrueba;
    public PdfControladorPrueba(PdfPrueba pdfPrueba) {
        this.pdfPrueba = pdfPrueba;
    }
    
    @GetMapping
    public void generatePdf(HttpServletResponse response) throws IOException{
        response.setContentType("application/pdf");
        
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_.pdf";
        
        response.setHeader(headerKey, headerValue);
        
        pdfPrueba.export(response);
    }
}
