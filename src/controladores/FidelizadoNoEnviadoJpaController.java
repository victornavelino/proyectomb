/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.fidelizacion.FidelizadoNoEnviado;
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
 * @author nago
 */
public class FidelizadoNoEnviadoJpaController implements Serializable {

    public FidelizadoNoEnviadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FidelizadoNoEnviado fidelizadoNoEnviado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(fidelizadoNoEnviado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FidelizadoNoEnviado fidelizadoNoEnviado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            fidelizadoNoEnviado = em.merge(fidelizadoNoEnviado);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = fidelizadoNoEnviado.getId();
                if (findFidelizadoNoEnviado(id) == null) {
                    throw new NonexistentEntityException("The fidelizadoNoEnviado with id " + id + " no longer exists.");
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
            FidelizadoNoEnviado fidelizadoNoEnviado;
            try {
                fidelizadoNoEnviado = em.getReference(FidelizadoNoEnviado.class, id);
                fidelizadoNoEnviado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fidelizadoNoEnviado with id " + id + " no longer exists.", enfe);
            }
            em.remove(fidelizadoNoEnviado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FidelizadoNoEnviado> findFidelizadoNoEnviadoEntities() {
        return findFidelizadoNoEnviadoEntities(true, -1, -1);
    }

    public List<FidelizadoNoEnviado> findFidelizadoNoEnviadoEntities(int maxResults, int firstResult) {
        return findFidelizadoNoEnviadoEntities(false, maxResults, firstResult);
    }

    private List<FidelizadoNoEnviado> findFidelizadoNoEnviadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FidelizadoNoEnviado.class));
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

    public FidelizadoNoEnviado findFidelizadoNoEnviado(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FidelizadoNoEnviado.class, id);
        } finally {
            em.close();
        }
    }

    public int getFidelizadoNoEnviadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FidelizadoNoEnviado> rt = cq.from(FidelizadoNoEnviado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
