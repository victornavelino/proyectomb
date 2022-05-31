/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.DepartamentoJpaController;
import controladores.MovimientoInternoJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.ListaPrecio;
import entidades.articulo.PrecioArticulo;
import entidades.inventario.MovimientoInterno;
import entidades.localidad.Departamento;
import entidades.localidad.Provincia;
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
public class MovimientoInternoFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    MovimientoInternoJpaController movimientoInternoJpaController = new MovimientoInternoJpaController(emf);

    private static MovimientoInternoFacade instance = null;

    protected MovimientoInternoFacade() {
    }

    public static MovimientoInternoFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new MovimientoInternoFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(MovimientoInterno movimientoInterno) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
        //EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        new MovimientoInternoJpaController(emfa).create(movimientoInterno);
    }

    public MovimientoInterno buscar(Long id) {
        return new MovimientoInternoJpaController(emf).findMovimientoInterno(id);
    }

    public void modificar(MovimientoInterno movimientoInterno, Sucursal sucursal) {
        EntityManagerFactory emfa;
        if (sucursal.getNombre().equals("CENTRAL")) {
            emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            
        } else {
            emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
            //emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            
        }
        try {
            //System.out.println("paso x sin cbo"+ movimientoInterno.getId() + movimientoInterno.getNumeroLote());
            new MovimientoInternoJpaController(emfa).edit(movimientoInterno);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(MovimientoInternoFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MovimientoInternoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public void modificarSinCBOSucursal(MovimientoInterno movimientoInterno) {
         System.out.println("paso x sin cbo");
        EntityManagerFactory emfa;
        
            emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            
       
           
            //emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        
        try {
            System.out.println("paso x sin cbo"+ movimientoInterno.getId() + movimientoInterno.getNumeroLote());
            new MovimientoInternoJpaController(emfa).edit(movimientoInterno);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(MovimientoInternoFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MovimientoInternoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new MovimientoInternoJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(MovimientoInternoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<MovimientoInterno> getTodos() {
        return new MovimientoInternoJpaController(emf).findMovimientoInternoEntities();
    }

    public List<MovimientoInterno> buscarPorSucursalRangoFecha(Sucursal sucursal,
            Date fechaInicio, Date fechaFin) {
        EntityManagerFactory emfa;
        if (sucursal.getNombre().equals("CENTRAL")) {
            emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        } else {
            emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
            //emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        } //EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        ema.getEntityManagerFactory().getCache().evictAll();
        Query qu = ema.createQuery("SELECT m FROM MovimientoInterno m WHERE "
                + "(m.sucursal=:sucursal OR m.sucursalDestino=:sucursal)"
                + " AND (m.fecha BETWEEN :fechaI AND :fechaF) "
                + "AND m.anulado=FALSE");
        qu.setParameter("sucursal", sucursal);
        qu.setParameter("fechaI", fechaInicio);
        qu.setParameter("fechaF", fechaFin);
        ema.getEntityManagerFactory().getCache().evictAll();
        return qu.getResultList();
    }

    public List<MovimientoInterno> buscarPorRangoFecha(Date fechaInicio, Date fechaFin) {

        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();

        Query qu = ema.createQuery("SELECT m FROM MovimientoInterno m WHERE "
                + "m.fecha BETWEEN :fechaI AND :fechaF AND m.anulado=FALSE"
                + " ORDER BY m.fecha DESC");
        qu.setParameter("fechaI", fechaInicio);
        qu.setParameter("fechaF", fechaFin);
        ema.getEntityManagerFactory().getCache().evictAll();
        return qu.getResultList();
    }

    public List<MovimientoInterno> buscarPorRangoFechaSucursal(Date fechaInicio, Date fechaFin, Sucursal sucursal) {
        EntityManagerFactory emfa;
        if (sucursal.getNombre().equals("CENTRAL")) {
            emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        } else {
            emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
            //emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        }
        EntityManager ema = emfa.createEntityManager();

        Query qu = ema.createQuery("SELECT m FROM MovimientoInterno m WHERE "
                + "m.fecha BETWEEN :fechaI AND :fechaF AND m.anulado=FALSE AND m.sucursal =:sucursal"
                + " ORDER BY m.fecha DESC");
        qu.setParameter("fechaI", fechaInicio);
        qu.setParameter("fechaF", fechaFin);
        qu.setParameter("sucursal", sucursal);
        ema.getEntityManagerFactory().getCache().evictAll();
        return qu.getResultList();
    }

    public List<MovimientoInterno> buscarPorNumeroLote(int numLote) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
        //EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();

        Query qu = ema.createQuery("SELECT m FROM MovimientoInterno m "
                + "WHERE m.numeroLote=:numLote AND m.anulado=FALSE");
        qu.setParameter("numLote", numLote);
        ema.getEntityManagerFactory().getCache().evictAll();
        return qu.getResultList();
    }

    public int getUltimoNumero() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT MAX(m.numero) FROM MovimientoInterno m");
        quBuscar.setMaxResults(1);
        try {
            return (int) quBuscar.getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    public int getUltimoNumeroLote() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.CONEXIONCLIENTES);
        //EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT MAX(m.numeroLote) FROM MovimientoInterno m");
        quBuscar.setMaxResults(1);
        try {
            return (int) quBuscar.getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }
}
