/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.venta.OrdenDeCompraArticulo;
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
 * @author franco
 */
public class OrdenDeCompraArticuloJpaController implements Serializable {

    public OrdenDeCompraArticuloJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(OrdenDeCompraArticulo ordenDeCompraArticulo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(ordenDeCompraArticulo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(OrdenDeCompraArticulo ordenDeCompraArticulo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ordenDeCompraArticulo = em.merge(ordenDeCompraArticulo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = ordenDeCompraArticulo.getId();
                if (findOrdenDeCompraArticulo(id) == null) {
                    throw new NonexistentEntityException("The ordenDeCompraArticulo with id " + id + " no longer exists.");
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
            OrdenDeCompraArticulo ordenDeCompraArticulo;
            try {
                ordenDeCompraArticulo = em.getReference(OrdenDeCompraArticulo.class, id);
                ordenDeCompraArticulo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ordenDeCompraArticulo with id " + id + " no longer exists.", enfe);
            }
            em.remove(ordenDeCompraArticulo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<OrdenDeCompraArticulo> findOrdenDeCompraArticuloEntities() {
        return findOrdenDeCompraArticuloEntities(true, -1, -1);
    }

    public List<OrdenDeCompraArticulo> findOrdenDeCompraArticuloEntities(int maxResults, int firstResult) {
        return findOrdenDeCompraArticuloEntities(false, maxResults, firstResult);
    }

    private List<OrdenDeCompraArticulo> findOrdenDeCompraArticuloEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(OrdenDeCompraArticulo.class));
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

    public OrdenDeCompraArticulo findOrdenDeCompraArticulo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OrdenDeCompraArticulo.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdenDeCompraArticuloCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<OrdenDeCompraArticulo> rt = cq.from(OrdenDeCompraArticulo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
