/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.inventario.MovimientoInterno;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author hugo
 */
public class MovimientoInternoJpaController implements Serializable {

    public MovimientoInternoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MovimientoInterno movimientoInterno) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(movimientoInterno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MovimientoInterno movimientoInterno) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            movimientoInterno = em.merge(movimientoInterno);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = movimientoInterno.getId();
                if (findMovimientoInterno(id) == null) {
                    throw new NonexistentEntityException("The movimientoInterno with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MovimientoInterno movimientoInterno;
            try {
                movimientoInterno = em.getReference(MovimientoInterno.class, id);
                movimientoInterno.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movimientoInterno with id " + id + " no longer exists.", enfe);
            }
            em.remove(movimientoInterno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MovimientoInterno> findMovimientoInternoEntities() {
        return findMovimientoInternoEntities(true, -1, -1);
    }

    public List<MovimientoInterno> findMovimientoInternoEntities(int maxResults, int firstResult) {
        return findMovimientoInternoEntities(false, maxResults, firstResult);
    }

    private List<MovimientoInterno> findMovimientoInternoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MovimientoInterno.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public MovimientoInterno findMovimientoInterno(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MovimientoInterno.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovimientoInternoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MovimientoInterno> rt = cq.from(MovimientoInterno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
