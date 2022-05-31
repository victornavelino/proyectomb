/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.venta.EntregaParcial;
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
 * @author vouilloz
 */
public class EntregaParcialJpaController implements Serializable {

    public EntregaParcialJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EntregaParcial entregaParcial) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(entregaParcial);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EntregaParcial entregaParcial) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            entregaParcial = em.merge(entregaParcial);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = entregaParcial.getId();
                if (findEntregaParcial(id) == null) {
                    throw new NonexistentEntityException("The entregaParcial with id " + id + " no longer exists.");
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
            EntregaParcial entregaParcial;
            try {
                entregaParcial = em.getReference(EntregaParcial.class, id);
                entregaParcial.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entregaParcial with id " + id + " no longer exists.", enfe);
            }
            em.remove(entregaParcial);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EntregaParcial> findEntregaParcialEntities() {
        return findEntregaParcialEntities(true, -1, -1);
    }

    public List<EntregaParcial> findEntregaParcialEntities(int maxResults, int firstResult) {
        return findEntregaParcialEntities(false, maxResults, firstResult);
    }

    private List<EntregaParcial> findEntregaParcialEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EntregaParcial.class));
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

    public EntregaParcial findEntregaParcial(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EntregaParcial.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntregaParcialCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EntregaParcial> rt = cq.from(EntregaParcial.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
