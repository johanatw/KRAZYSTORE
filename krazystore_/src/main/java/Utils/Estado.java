/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author HP
 */
public enum Estado {
    NUEVO('N'),
    PENDIENTEDEPAGO('P'),
    PAGADO('C'),
    CONANTICIPO('A'),
    ANULADO('A'),
    FACTURADO('F'),
    PENDIENTEDEFACTURA('P'),
    RECEPCINADO('R'),
    PARCIALMENTERECEPCINADO('M'),
    
    PENDIENTE('P'),
    FINALIZADO('F'),
    PENDIENTEAJUSTE('P'),
    AJUSTADO('A'),
    PAGOCOMPLETO('C'),
    
    CAJACERRADA('C'),
    CAJAABIERTA('A'),
    ENCURSO('S');
    
    private final char codigo;

    private Estado(char codigo) {
        this.codigo = codigo;
    }

    public char getCodigo() {
        return codigo;
    }

    
       
    
}
