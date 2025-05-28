/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author HP
 */
public enum TipoFacturaCompra {
    PRODUCTOS("PROD"),
    SERVICIO_TRANSPORTE_INTERNACIONAL("STI");
    
    private final String codigo;

    private TipoFacturaCompra(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
