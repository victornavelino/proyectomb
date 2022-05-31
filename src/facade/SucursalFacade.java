/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.SucursalJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;

/**
 *
 * @author hugo
 */
public class SucursalFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    SucursalJpaController sucursalJpaController = new SucursalJpaController(emf);

    private static SucursalFacade instance = null;

    protected SucursalFacade() {
    }

    public static SucursalFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new SucursalFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(Sucursal sucursal) {
        new SucursalJpaController(emf).create(sucursal);
    }

    public Sucursal buscar(Long id) {
        return new SucursalJpaController(emf).findSucursal(id);
    }

    public void modificar(Sucursal sucursal) {
        try {
            new SucursalJpaController(emf).edit(sucursal);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SucursalFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SucursalFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new SucursalJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SucursalFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Sucursal> getTodos() {
        EntityManagerFactory emfs = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ems = emfs.createEntityManager();
        Query qu = ems.createQuery("SELECT s FROM Sucursal s");
        ems.getEntityManagerFactory().getCache().evictAll();
        return qu.getResultList();
    }

    public List<Sucursal> buscarPorNombre(String nombre) {
        EntityManagerFactory emfs = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ems = emfs.createEntityManager();
        Query qu = ems.createQuery("SELECT s FROM Sucursal s WHERE s.nombre LIKE :nombre");
        qu.setParameter("nombre", "%" + nombre + "%");
        return qu.getResultList();
    }

    public Sucursal buscarPorCodigo(String codigo) {
        EntityManager em = emf.createEntityManager();
        Query qu = em.createQuery("SELECT s FROM Sucursal s WHERE s.codigo=:codigo");
        qu.setParameter("codigo", codigo);
        qu.setMaxResults(1);
        try {
            return (Sucursal) qu.getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    public boolean buscarExisteCodigo(String codigo) {
        try {
            EntityManager em = emf.createEntityManager();
            Query qu = em.createQuery("SELECT s FROM Sucursal s WHERE s.codigo=:codigo");
            qu.setParameter("codigo", codigo);
            qu.setMaxResults(1);
            System.out.println("susucrsal: " + ((Sucursal) qu.getSingleResult()).getNombre());
            qu.getSingleResult();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "");
            return false;
        }

    }

    List<Sucursal> getSucursalesNoCentral() {
        EntityManagerFactory emfs = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ems = emfs.createEntityManager();
        Query qu = ems.createQuery("SELECT s FROM Sucursal s WHERE s.codigo<>:codigo ");
        qu.setParameter("codigo", "1");
        ems.getEntityManagerFactory().getCache().evictAll();
        return qu.getResultList();
    }
    
    public Sucursal buscarPorNombre_exacto(String nombre) {

        Query qu = em.createQuery("SELECT s FROM Sucursal s WHERE s.nombre =:nombre");
        qu.setParameter("nombre", "%" + nombre + "%");
        return (Sucursal) qu.getSingleResult();
    }    

}
