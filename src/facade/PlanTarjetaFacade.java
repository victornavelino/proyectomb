/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.ArticuloJpaController;
import controladores.CategoriaJpaController;
import controladores.DomicilioJpaController;
import controladores.ListaPrecioJpaController;
import controladores.LocalidadJpaController;
import controladores.PlanTarjetaJpaController;
import controladores.SucursalJpaController;
import controladores.TarjetaDeCreditoJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.caja.PlanTarjeta;
import entidades.caja.TarjetaDeCredito;
import entidades.localidad.Localidad;
import entidades.persona.Domicilio;
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
public class PlanTarjetaFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    PlanTarjetaJpaController planTarjetaJpaController = new PlanTarjetaJpaController(emf);

    private static PlanTarjetaFacade instance = null;

    protected PlanTarjetaFacade() {
    }

    public static PlanTarjetaFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new PlanTarjetaFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(PlanTarjeta planTarjeta) {
        new PlanTarjetaJpaController(emf).create(planTarjeta);
    }

    public PlanTarjeta buscar(Long id) {
        return new PlanTarjetaJpaController(emf).findPlanTarjeta(id);
    }

    public void modificar(PlanTarjeta planTarjeta) {
        try {
            new PlanTarjetaJpaController(emf).edit(planTarjeta);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PlanTarjetaFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PlanTarjetaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new PlanTarjetaJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PlanTarjetaFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<PlanTarjeta> getTodos() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query qu = ema.createQuery("SELECT p FROM PlanTarjeta p");
        ema.getEntityManagerFactory().getCache().evictAll();
        return qu.getResultList();
    }

    public List<PlanTarjeta> getPlanes(TarjetaDeCredito tarjetaDeCredito) {
        EntityManager ema = emf.createEntityManager();
        Query qu = ema.createQuery("SELECT p FROM PlanTarjeta p WHERE p.tarjetaDeCredito=:tarjeta");
        qu.setParameter("tarjeta", tarjetaDeCredito);
        return qu.getResultList();
    }

    public List<PlanTarjeta> getPlanUnSoloPago(TarjetaDeCredito tarjetaDeCredito) {
        EntityManager ema = emf.createEntityManager();
        //ESTE METODO TRAE SOLO LA QUE TIENE UN PAGO, SIEMPRE SE CARGA ASI
        Query qu = ema.createQuery("SELECT p FROM PlanTarjeta p WHERE p.tarjetaDeCredito=:tarjeta AND TRIM(p.descripcion)=:descripcion");
        qu.setParameter("tarjeta", tarjetaDeCredito);
        qu.setParameter("descripcion", tarjetaDeCredito.getDescripcion().trim());
        return qu.getResultList();
    }

}
