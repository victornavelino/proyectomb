/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.caja.CuponTarjeta;
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
public class CuponTarjetaJpaController implements Serializable {

    public CuponTarjetaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CuponTarjeta cuponTarjeta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cuponTarjeta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CuponTarjeta cuponTarjeta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cuponTarjeta = em.merge(cuponTarjeta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cuponTarjeta.getId();
                if (findCuponTarjeta(id) == null) {
                    throw new NonexistentEntityException("The cuponTarjeta with id " + id + " no longer exists.");
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
            CuponTarjeta cuponTarjeta;
            try {
                cuponTarjeta = em.getReference(CuponTarjeta.class, id);
                cuponTarjeta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cuponTarjeta with id " + id + " no longer exists.", enfe);
            }
            em.remove(cuponTarjeta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CuponTarjeta> findCuponTarjetaEntities() {
        return findCuponTarjetaEntities(true, -1, -1);
    }

    public List<CuponTarjeta> findCuponTarjetaEntities(int maxResults, int firstResult) {
        return findCuponTarjetaEntities(false, maxResults, firstResult);
    }

    private List<CuponTarjeta> findCuponTarjetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CuponTarjeta.class));
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

    public CuponTarjeta findCuponTarjeta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CuponTarjeta.class, id);
        } finally {
            em.close();
        }
    }

    public int getCuponTarjetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CuponTarjeta> rt = cq.from(CuponTarjeta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
