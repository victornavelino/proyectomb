/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controladores.ArticuloJpaController;
import controladores.exceptions.NonexistentEntityException;
import entidades.articulo.Articulo;
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
public class ArticuloFacade {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);

    private static ArticuloFacade instance = null;

    protected ArticuloFacade() {
    }

    public static ArticuloFacade getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new ArticuloFacade();
        }
    }

//El metodo "clone" es sobreescrito por el siguiente que arroja una excepción:
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void alta(Articulo articulo) {
        new ArticuloJpaController(emf).create(articulo);
    }

    public Articulo buscar(Long id) {
        return new ArticuloJpaController(emf).findArticulo(id);
    }

    public void modificar(Articulo articulo) {
        try {
            new ArticuloJpaController(emf).edit(articulo);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ArticuloFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ArticuloFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(long id) {
        try {
            new ArticuloJpaController(emf).destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ArticuloFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Articulo> getTodos() {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfa.createEntityManager();
        Query qu = em.createQuery("SELECT s FROM Articulo s");
        em.getEntityManagerFactory().getCache().evictAll();
        return qu.getResultList();
    }

    public List<Articulo> getTodosCodigo() {
        EntityManager em = emf.createEntityManager();
        Query qu = em.createQuery("SELECT s FROM Articulo s ORDER BY s.codigoBarra");
        return qu.getResultList();
    }

    public List<Articulo> getTodosDescripcion() {
        EntityManager em = emf.createEntityManager();
        Query qu = em.createQuery("SELECT s FROM Articulo s ORDER BY s.descripcion");
        return qu.getResultList();
    }

    public List<Articulo> buscarPorDescripcion(String descripcion) {
        EntityManager em = emf.createEntityManager();
        Query qu = em.createQuery("SELECT s FROM Articulo s WHERE s.descripcion LIKE :descripcion");
        qu.setParameter("descripcion", "%" + descripcion.toUpperCase() + "%");
        return qu.getResultList();
    }

    public List<Articulo> buscarPorDescDescCortayCodigo(String descripcion) {
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfa.createEntityManager();
        Query qu = em.createQuery("SELECT s FROM Articulo s WHERE "
                + "s.descripcionReducida LIKE :descripcion  OR "
                + "s.codigoBarra LIKE :descripcion OR "
                + "s.descripcion LIKE :descripcion ORDER BY s.descripcionReducida ASC");
        qu.setParameter("descripcion", "%" + descripcion.toUpperCase() + "%");
        em.getEntityManagerFactory().getCache().evictAll();
        return qu.getResultList();
    }

    public Articulo buscarPorCodigo(String codigo) {
        EntityManager em = emf.createEntityManager();
        Query qu = em.createQuery("SELECT s FROM Articulo s WHERE "
                + "s.codigoBarra=:codigo");
        qu.setParameter("codigo", codigo);
        try {
            return (Articulo) qu.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public boolean buscarCodigoBarra(String codigo) {
        try {
            EntityManager em = emf.createEntityManager();
            Query qu = em.createQuery("SELECT s FROM Articulo s WHERE s.codigoBarra=:codigo");
            qu.setParameter("codigo", codigo);
            qu.getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean buscarExistenciaCodigoBarra(String codigo) {
        boolean flag = false;
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT s FROM Articulo s WHERE s.codigoBarra=:codigo");
        quBuscar.setParameter("codigo", codigo);
        try {
            if (quBuscar.getResultList().isEmpty()) {
                flag = false;
            } else {
                flag = true;
            }
            return flag;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Salio por el catch, no se encontro el articulo");
            return false;
        }

    }

    public boolean getArticuloCodigo(Long id) {
        boolean flag = false;
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager ema = emfa.createEntityManager();
        Query quBuscar = ema.createQuery("SELECT s FROM Articulo s WHERE s.id=:id");
        quBuscar.setParameter("id", id);
        try {
            if (quBuscar.getResultList().isEmpty()) {
                flag = false;
            } else {
                flag = true;
            }
            return flag;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Salio por el catch");
            return false;
        }

    }

    public Articulo buscarArticuloDescripcion(String descripcion) {
        EntityManager em = emf.createEntityManager();

        Query qu = em.createQuery("SELECT a FROM Articulo a WHERE  a.descripcion = :descripcion");
        qu.setParameter("descripcion", descripcion);
        try {
            return (Articulo) qu.getResultList().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public void borrarArticulos() {
        System.out.println("entro borrado Articulos");
        EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
        EntityManager em = emfa.createEntityManager();
        em.getTransaction().begin();
        int qua = em.createQuery("DELETE FROM PrecioArticulo ap").executeUpdate();
        int qpromo = em.createQuery("DELETE FROM Promocion a").executeUpdate();
        int quas = em.createQuery("DELETE FROM PromocionArticulo s").executeUpdate();
        if (VentaFacade.getInstance().getTodos().isEmpty()) {
            int qu = em.createQuery("DELETE FROM Articulo a").executeUpdate();
            System.out.println("borrado articulo " + qu);
        }

        System.out.println("borrado PrecioArticulo " + qua);
        em.getTransaction().commit();

    }

}
