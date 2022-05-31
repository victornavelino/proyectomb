/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.caja;

import entidades.cliente.Cliente;
import entidades.venta.Venta;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author root
 */
@Entity
@Table(name = "movimientocaja_cobroventa")
public class CobroVenta extends MovimientoCaja implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToOne(mappedBy = "cobroVenta")
    private Venta venta;

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    @Override
    public String toString() {
        return venta + " cobranza";
    }

}
