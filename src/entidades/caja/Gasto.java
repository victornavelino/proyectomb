/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades.caja;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author root
 */
@Entity
@Table(name = "movimientocaja_gasto")
public class Gasto extends MovimientoCaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @OneToOne
    private TipoDeGasto tipoDeGasto;
    @OneToOne
    private TipoDeFactura tipoDeFactura;
    private String cuit;
    private String numeroFactura;
    private String descripcion;

    public TipoDeFactura getTipoDeFactura() {
        return tipoDeFactura;
    }

    public void setTipoDeFactura(TipoDeFactura tipoDeFactura) {
        this.tipoDeFactura = tipoDeFactura;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }
    
    public TipoDeGasto getTipoDeGasto() {
        return tipoDeGasto;
    }

    public void setTipoDeGasto(TipoDeGasto tipoDeGasto) {
        this.tipoDeGasto = tipoDeGasto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
    
}
