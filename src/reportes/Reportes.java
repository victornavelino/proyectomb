/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;

import entidades.caja.Caja;
import facade.ConexionFacade;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JDialog;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory;
//import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Administrador
 */
public class Reportes {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();

    public void dialogoReporte(JasperPrint jasperPrint, String titulo) {
        //abro el reporte en un dialog
        JDialog dialogo = new JDialog();
        dialogo.getContentPane().add(new JRViewer(jasperPrint));
        dialogo.setModal(true);
        dialogo.setTitle(titulo);
        dialogo.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        dialogo.pack();
        //dialogo.setAlwaysOnTop(true);
        dialogo.setVisible(true);
    }
//
//    public void reporteCierreCaja(Caja caja) {
//        try {
//            Map parameters = new HashMap();
//            parameters.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER, em);
//            parameters.put("CAJA_ID", caja.getId());
//            JasperPrint jasperPrint = JasperFillManager.fillReport(getClass().getResourceAsStream("/reportes/Caja.jasper"), parameters);
//            dialogoReporte(jasperPrint, "Cierre de Caja");
//        } catch (JRException ex) {
//            Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
//
//        }
//    }

  
}
