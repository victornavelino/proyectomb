/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.ArticuloJpaController;
import controladores.CategoriaJpaController;
import controladores.ListaPrecioJpaController;
import controladores.LocalidadJpaController;
import controladores.SucursalJpaController;
import controladores.TipoDocumentoJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.Sucursal;
import entidades.articulo.Articulo;
import entidades.articulo.Categoria;
import entidades.articulo.ListaPrecio;
import entidades.localidad.Localidad;
import entidades.persona.TipoDocumento;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;

/**
 *
 * @author hugo
 */
public class TipoDocumentoFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
    EntityManager em = emf.createEntityManager();
    TipoDocumentoJpaController tipoDocumentoJpaController = new TipoDocumentoJpaController(emf);

    private static TipoDocumentoFacade instance = null;

    protected TipoDocumentoFacade() {
    }

    public static TipoDocumentoFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new TipoDocumentoFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(TipoDocumento tipoDocumento) {
        new TipoDocumentoJpaController(emf).create(tipoDocumento);
    }

    public TipoDocumento buscar(Long id) {
        return new TipoDocumentoJpaController(emf).findTipoDocumento(id);
    }

    public void modificar(TipoDocumento tipoDocumento) {
        try {
            new TipoDocumentoJpaController(emf).edit(tipoDocumento);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TipoDocumentoFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TipoDocumentoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new TipoDocumentoJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TipoDocumentoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<TipoDocumento> getTodos() {
        return new TipoDocumentoJpaController(emf).findTipoDocumentoEntities();
    }

    public TipoDocumento buscarPorTipo(String tipo) {
        EntityManager em = emf.createEntityManager();
        Query qu = em.createQuery("SELECT t FROM TipoDocumento t WHERE t.descripcion=:descripcion");
        qu.setParameter("descripcion", tipo);
        try {
            return (TipoDocumento) qu.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public boolean buscarExisteTipo(String descripcion) {
        boolean flag = false;
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT t FROM TipoDocumento t WHERE t.descripcion=:descripcion");
        quBuscar.setParameter("descripcion", descripcion);
        try {
            if (quBuscar.getResultList().isEmpty()) {
                flag = false;
            } else {
                flag = true;
            }
            return flag;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Salio por el catch, no se encontro el Tipo de Documento");
            return false;
        }
    }
    
}
