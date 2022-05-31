/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades.persona;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

/**
 *
 * @author Carlos
 */
@Embeddable
public class DocumentoIdentidad implements Serializable {
    @OneToOne(optional = false)
    private TipoDocumento tipoDocumento;
    @Basic(optional = false)
    private String numero;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

   
    @Override
    public String toString() {
        return tipoDocumento + " " + numero;
    }

}
