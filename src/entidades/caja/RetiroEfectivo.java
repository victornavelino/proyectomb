/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades.caja;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author root
 */
@Entity
@Table(name = "movimientocaja_retiroefectivo")
public class RetiroEfectivo extends MovimientoCaja implements Serializable {
    private static final long serialVersionUID = 1L;
   
    private String descripcion;

    
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
