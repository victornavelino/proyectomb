/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades.caja;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author vouilloz
 */

public class VentaCobranza implements Comparator {
   
    
    //Franquito: agregar un campo object con el objeto venta o cobranza, va a ser util para eliminar
    private Object objeto;
    private String nombreCliente;
    public String tipo;
    private Date fecha;
    private BigDecimal importe;
    private BigDecimal importeSaldo;
    private int numero;
    private BigDecimal saldoAcumulado;
    private String observaciones;
//Franquito: get y set objeto

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

 

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public BigDecimal getImporteSaldo() {
        return importeSaldo;
    }

    public void setImporteSaldo(BigDecimal importeSaldo) {
        this.importeSaldo = importeSaldo;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public BigDecimal getSaldoAcumulado() {
        return saldoAcumulado;
    }

    public void setSaldoAcumulado(BigDecimal saldoAcumulado) {
        this.saldoAcumulado = saldoAcumulado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Object getObjeto() {
        return objeto;
    }

    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }
    

    @Override
    public int compare(Object o1, Object o2) {
       VentaCobranza vc1 = (VentaCobranza) o1;
        VentaCobranza vc2 = (VentaCobranza) o2;
        return vc1.getFecha().compareTo(vc2.getFecha());
    }
    
}


