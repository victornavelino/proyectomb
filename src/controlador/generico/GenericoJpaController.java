/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.generico;

import controladores.exceptions.NonexistentEntityException;
import entidades.articulo.TipoIva;
import entidades.persona.TipoTelefono;
import facade.ConexionFacade;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author franco
 */
public class GenericoJpaController<T> implements Serializable {

    public GenericoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(T entidad) {

        System.out.println("Entro al persist");

        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(entidad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(T entidad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(entidad);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Class[] sinParametro = null;
                Method campo = entidad.getClass().getDeclaredMethod("getId", sinParametro);
                Object objetoId = campo.invoke(entidad, (Object) null);

                Long id = Long.parseLong(String.valueOf(objetoId));
                if (em.find(entidad.getClass(), id) == null) {
                    throw new NonexistentEntityException("El dato que intenta eliminar no existe. Actualizar la pantalla");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(T entidad) throws NonexistentEntityException, Exception {
        EntityManager em = null;

        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Class[] sinParametro = null;
            Method campo = entidad.getClass().getDeclaredMethod("getId", sinParametro);
            Object objetoId = campo.invoke(entidad, new Object[]{});
            try {
                Long id = Long.parseLong(String.valueOf(objetoId));

                em.remove(em.getReference(entidad.getClass(), id));
                em.getTransaction().commit();

            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El dato que intenta eliminar no existe. Actualizar la pantalla");
            }

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public T findByDescripcion(String cadena, String nombreEntidad, Long id) {
        EntityManager em = getEntityManager();

        try {
            Query query = null;
            String consulta = null;

            if (id == null) {
                query = em.createQuery("SELECT g FROM " + nombreEntidad + " AS g WHERE TRIM(LOWER(g.descripcion)) = :cadena");
            } else {
                query = em.createQuery("SELECT g FROM " + nombreEntidad + " AS g WHERE TRIM(LOWER(g.descripcion)) = :cadena AND "
                        + "g.id <> :id");
                query.setParameter("id", id);
            }//fin if-else

            System.out.println("Consulta: " + consulta);

            query.setParameter("cadena", cadena.trim().toLowerCase());

            return (T) query.getSingleResult();

        } catch (NoResultException nr) {

            return null;
        } finally {
            System.out.println("Entro al finali");
            em.close();
        }

    }//fin findByDescripcion

    public List<T> findLike(String cadena, String nombreEntidad) {
        EntityManager em = getEntityManager();

        Query query = null;
        if (!cadena.isEmpty()) {
            query = em.createQuery("SELECT g FROM " + nombreEntidad + " g WHERE TRIM(LOWER(g.descripcion)) "
                    + "LIKE :cadena ORDER BY g.descripcion");
            query.setParameter("cadena", "%" + cadena.trim().toLowerCase() + "%");
        } else {
            //emf = null;
            EntityManagerFactory emfa = Persistence.createEntityManagerFactory("ProyectoDosPU", ConexionFacade.PROPIEDADES);
            EntityManager ema = emfa.createEntityManager();
            query = ema.createQuery("SELECT g FROM " + nombreEntidad + " g ORDER BY g.descripcion");
            ema.getEntityManagerFactory().getCache().evictAll();
        }
        System.out.println("lista de entidades: " + query.getResultList());
        return query.getResultList();

    }//fin findLike

    public T buscar(String cadena, String nombreEntidad) {
        EntityManager em = getEntityManager();

        Query query = null;
        if (!cadena.isEmpty()) {
            query = em.createQuery("SELECT g FROM " + nombreEntidad + " g WHERE TRIM(LOWER(g.descripcion)) "
                    + "LIKE :cadena ORDER BY g.descripcion");
            query.setParameter("cadena", "%" + cadena.trim().toLowerCase() + "%");
        } else {
            query = em.createQuery("SELECT g FROM " + nombreEntidad + " g ORDER BY g.descripcion");
        }

        return (T) query.getSingleResult();

    }//fin findLike

    public List<T> find(String nombreEntidad) {
        EntityManager em = getEntityManager();
        Query query = null;
        query = em.createQuery("SELECT g FROM " + nombreEntidad + " g ORDER BY g.descripcion");
        return query.getResultList();

    }//fin findLike

}
