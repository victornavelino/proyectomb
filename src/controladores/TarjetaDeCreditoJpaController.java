/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.caja.TarjetaDeCredito;
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
 * @author root
 */
public class TarjetaDeCreditoJpaController implements Serializable {

    public TarjetaDeCreditoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TarjetaDeCredito tarjetaDeCredito) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tarjetaDeCredito);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TarjetaDeCredito tarjetaDeCredito) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tarjetaDeCredito = em.merge(tarjetaDeCredito);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = tarjetaDeCredito.getId();
                if (findTarjetaDeCredito(id) == null) {
                    throw new NonexistentEntityException("The tarjetaDeCredito with id " + id + " no longer exists.");
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
            TarjetaDeCredito tarjetaDeCredito;
            try {
                tarjetaDeCredito = em.getReference(TarjetaDeCredito.class, id);
                tarjetaDeCredito.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarjetaDeCredito with id " + id + " no longer exists.", enfe);
            }
            em.remove(tarjetaDeCredito);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TarjetaDeCredito> findTarjetaDeCreditoEntities() {
        return findTarjetaDeCreditoEntities(true, -1, -1);
    }

    public List<TarjetaDeCredito> findTarjetaDeCreditoEntities(int maxResults, int firstResult) {
        return findTarjetaDeCreditoEntities(false, maxResults, firstResult);
    }

    private List<TarjetaDeCredito> findTarjetaDeCreditoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TarjetaDeCredito.class));
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

    public TarjetaDeCredito findTarjetaDeCredito(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TarjetaDeCredito.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarjetaDeCreditoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TarjetaDeCredito> rt = cq.from(TarjetaDeCredito.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
