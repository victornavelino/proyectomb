/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.CobranzaCtaCteJpaController;
import controladores.ArticuloJpaController;
import controladores.CategoriaJpaController;
import controladores.CuentaCorrienteJpaController;
import controladores.DomicilioJpaController;
import controladores.ListaPrecioJpaController;
import controladores.LocalidadJpaController;
import controladores.SucursalJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.caja.CobranzaCtaCte;
import entidades.caja.CuentaCorriente;
import entidades.cliente.Cliente;
import entidades.localidad.Localidad;
import entidades.persona.Domicilio;
import includes.Comunes;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author hugo
 */
public class CobranzaCtaCteFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);

    private static CobranzaCtaCteFacade instance = null;

    protected CobranzaCtaCteFacade() {
    }

    public static CobranzaCtaCteFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new CobranzaCtaCteFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(CobranzaCtaCte cobranzaCtaCte) {
        new CobranzaCtaCteJpaController(emf).create(cobranzaCtaCte);
    }

    public CobranzaCtaCte buscar(Long id) {
        return new CobranzaCtaCteJpaController(emf).findCobranzaCtaCte(id);
    }

    public void modificar(CobranzaCtaCte cobranzaCtaCte) {
        try {
            new CobranzaCtaCteJpaController(emf).edit(cobranzaCtaCte);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CobranzaCtaCteFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CobranzaCtaCteFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   /* public void eliminar(long id) throws Imagenes.exceptions.NonexistentEntityException {
        try {
            new CobranzaCtaCteJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CobranzaCtaCteFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

    public List<CobranzaCtaCte> getTodos() {
        return new CobranzaCtaCteJpaController(emf).findCobranzaCtaCteEntities();
    }


    public List<BigDecimal> getSaldoporFechayTicket(Date fechaDesde, Date fechaHasta, int numeroTicket) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT c.saldoCobranza FROM CobranzaCtaCte c WHERE c.numero =:numeroTicket AND c.fecha BETWEEN :fechaDesde AND :fechaHasta");
        quBuscar.setParameter("numeroTicket", numeroTicket);
        quBuscar.setParameter("fechaDesde", fechaDesde);
        quBuscar.setParameter("fechaHasta", fechaHasta);
        return quBuscar.getResultList();
    }

  
}
