/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.promocion.PromocionArticulo;
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
public class PromocionArticuloJpaController implements Serializable {

    public PromocionArticuloJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PromocionArticulo promocionArticulo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(promocionArticulo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PromocionArticulo promocionArticulo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            promocionArticulo = em.merge(promocionArticulo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = promocionArticulo.getId();
                if (findPromocionArticulo(id) == null) {
                    throw new NonexistentEntityException("The promocionArticulo with id " + id + " no longer exists.");
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
            PromocionArticulo promocionArticulo;
            try {
                promocionArticulo = em.getReference(PromocionArticulo.class, id);
                promocionArticulo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The promocionArticulo with id " + id + " no longer exists.", enfe);
            }
            em.remove(promocionArticulo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PromocionArticulo> findPromocionArticuloEntities() {
        return findPromocionArticuloEntities(true, -1, -1);
    }

    public List<PromocionArticulo> findPromocionArticuloEntities(int maxResults, int firstResult) {
        return findPromocionArticuloEntities(false, maxResults, firstResult);
    }

    private List<PromocionArticulo> findPromocionArticuloEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PromocionArticulo.class));
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

    public PromocionArticulo findPromocionArticulo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PromocionArticulo.class, id);
        } finally {
            em.close();
        }
    }

    public int getPromocionArticuloCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PromocionArticulo> rt = cq.from(PromocionArticulo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
