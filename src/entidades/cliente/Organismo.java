/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.cliente;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author vouilloz
 */
@Entity
@Table(name = "cliente_organismo")
public class Organismo extends Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    private String razonSocial;
    @Basic(optional = false)
    private String cuit;

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCUIT() {
        return cuit;
    }

    public void setCUIT(String CUIT) {
        this.cuit = CUIT;
    }

    @Override
    public String toString() {
        return razonSocial;
    }

}
