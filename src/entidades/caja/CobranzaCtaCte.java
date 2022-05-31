/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.caja;

import entidades.cliente.Cliente;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author root
 */
@Entity
@Table(name = "movimientocaja_cobranzactacte")
public class CobranzaCtaCte extends MovimientoCaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @OneToOne
    private Cliente cliente;
    private int numero;
    @Column(scale = 3, precision = 12)
    private BigDecimal saldoCobranza;
    private String observaciones;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }


    public BigDecimal getSaldoCobranza() {
        return saldoCobranza;
    }

    public void setSaldoCobranza(BigDecimal saldoCobranza) {
        this.saldoCobranza = saldoCobranza;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }


    

    
}
