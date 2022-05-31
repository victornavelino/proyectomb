
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
@Table(name = "movimientocaja_ingreso")
public class Ingreso extends MovimientoCaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @OneToOne
    private TipoDeIngreso tipoDeIngreso;
    private String descripcion;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoDeIngreso getTipoDeIngreso() {
        return tipoDeIngreso;
    }

    public void setTipoDeIngreso(TipoDeIngreso tipoDeIngreso) {
        this.tipoDeIngreso = tipoDeIngreso;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
    
}
