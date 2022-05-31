/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;


import controladores.TipoTelefonoJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.articulo.Articulo;
import entidades.cliente.Cliente;
import entidades.persona.TipoTelefono;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;


/**
 *
 * @author hugo
 */
public class TipoTelefonoFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    TipoTelefonoJpaController tipoTelefonoJpaController = new TipoTelefonoJpaController(emf);

    private static TipoTelefonoFacade instance = null;

    protected TipoTelefonoFacade() {
    }

    public static TipoTelefonoFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new TipoTelefonoFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(TipoTelefono tipoTelefono) {
        new TipoTelefonoJpaController(emf).create(tipoTelefono);
    }

    public TipoTelefono buscar(Long id) {
        return new TipoTelefonoJpaController(emf).findTipoTelefono(id);   
    }

    public void modificar(TipoTelefono tipoTelefono) {
        try {
            new TipoTelefonoJpaController(emf).edit(tipoTelefono);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TipoTelefonoFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TipoTelefonoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new TipoTelefonoJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TipoTelefonoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<TipoTelefono> getTodos() {
        return new TipoTelefonoJpaController(emf).findTipoTelefonoEntities();
    }
    
     public TipoTelefono buscarPorTipo(String tipo) {
        EntityManager em = emf.createEntityManager();
        Query qu = em.createQuery("SELECT t FROM TipoTelefono t WHERE t.descripcion=:descripcion");
        qu.setParameter("descripcion", tipo);
        try {
            return (TipoTelefono) qu.getSingleResult();
        }catch(NoResultException e){
            return null;
        }

    }

    
     
}
