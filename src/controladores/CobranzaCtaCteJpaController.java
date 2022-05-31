/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;


import controladores.exceptions.NonexistentEntityException;
import entidades.caja.CobranzaCtaCte;
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
 * @author vicky
 */
public class CobranzaCtaCteJpaController implements Serializable {

    public CobranzaCtaCteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CobranzaCtaCte cobranzaCtaCte) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cobranzaCtaCte);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CobranzaCtaCte cobranzaCtaCte) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cobranzaCtaCte = em.merge(cobranzaCtaCte);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cobranzaCtaCte.getId();
                if (findCobranzaCtaCte(id) == null) {
                    throw new NonexistentEntityException("The cobranzaCtaCte with id " + id + " no longer exists.");
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
            CobranzaCtaCte cobranzaCtaCte;
            try {
                cobranzaCtaCte = em.getReference(CobranzaCtaCte.class, id);
                cobranzaCtaCte.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cobranzaCtaCte with id " + id + " no longer exists.", enfe);
            }
            em.remove(cobranzaCtaCte);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CobranzaCtaCte> findCobranzaCtaCteEntities() {
        return findCobranzaCtaCteEntities(true, -1, -1);
    }

    public List<CobranzaCtaCte> findCobranzaCtaCteEntities(int maxResults, int firstResult) {
        return findCobranzaCtaCteEntities(false, maxResults, firstResult);
    }

    private List<CobranzaCtaCte> findCobranzaCtaCteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CobranzaCtaCte.class));
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

    public CobranzaCtaCte findCobranzaCtaCte(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CobranzaCtaCte.class, id);
        } finally {
            em.close();
        }
    }

    public int getCobranzaCtaCteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CobranzaCtaCte> rt = cq.from(CobranzaCtaCte.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
