/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.learnmigratedb;

import com.example.learnmigratedb.exceptions.IllegalOrphanException;
import com.example.learnmigratedb.exceptions.NonexistentEntityException;
import com.example.learnmigratedb.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author NITRO
 */
public class PenjualJpaController implements Serializable {

    public PenjualJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.example_learnmigratedb_jar_0.0.1-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Penjual penjual) throws PreexistingEntityException, Exception {
        if (penjual.getTransaksiCollection() == null) {
            penjual.setTransaksiCollection(new ArrayList<Transaksi>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Transaksi> attachedTransaksiCollection = new ArrayList<Transaksi>();
            for (Transaksi transaksiCollectionTransaksiToAttach : penjual.getTransaksiCollection()) {
                transaksiCollectionTransaksiToAttach = em.getReference(transaksiCollectionTransaksiToAttach.getClass(), transaksiCollectionTransaksiToAttach.getIdTransaksi());
                attachedTransaksiCollection.add(transaksiCollectionTransaksiToAttach);
            }
            penjual.setTransaksiCollection(attachedTransaksiCollection);
            em.persist(penjual);
            for (Transaksi transaksiCollectionTransaksi : penjual.getTransaksiCollection()) {
                Penjual oldIdPenjualOfTransaksiCollectionTransaksi = transaksiCollectionTransaksi.getIdPenjual();
                transaksiCollectionTransaksi.setIdPenjual(penjual);
                transaksiCollectionTransaksi = em.merge(transaksiCollectionTransaksi);
                if (oldIdPenjualOfTransaksiCollectionTransaksi != null) {
                    oldIdPenjualOfTransaksiCollectionTransaksi.getTransaksiCollection().remove(transaksiCollectionTransaksi);
                    oldIdPenjualOfTransaksiCollectionTransaksi = em.merge(oldIdPenjualOfTransaksiCollectionTransaksi);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPenjual(penjual.getIdPenjual()) != null) {
                throw new PreexistingEntityException("Penjual " + penjual + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Penjual penjual) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Penjual persistentPenjual = em.find(Penjual.class, penjual.getIdPenjual());
            Collection<Transaksi> transaksiCollectionOld = persistentPenjual.getTransaksiCollection();
            Collection<Transaksi> transaksiCollectionNew = penjual.getTransaksiCollection();
            List<String> illegalOrphanMessages = null;
            for (Transaksi transaksiCollectionOldTransaksi : transaksiCollectionOld) {
                if (!transaksiCollectionNew.contains(transaksiCollectionOldTransaksi)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Transaksi " + transaksiCollectionOldTransaksi + " since its idPenjual field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Transaksi> attachedTransaksiCollectionNew = new ArrayList<Transaksi>();
            for (Transaksi transaksiCollectionNewTransaksiToAttach : transaksiCollectionNew) {
                transaksiCollectionNewTransaksiToAttach = em.getReference(transaksiCollectionNewTransaksiToAttach.getClass(), transaksiCollectionNewTransaksiToAttach.getIdTransaksi());
                attachedTransaksiCollectionNew.add(transaksiCollectionNewTransaksiToAttach);
            }
            transaksiCollectionNew = attachedTransaksiCollectionNew;
            penjual.setTransaksiCollection(transaksiCollectionNew);
            penjual = em.merge(penjual);
            for (Transaksi transaksiCollectionNewTransaksi : transaksiCollectionNew) {
                if (!transaksiCollectionOld.contains(transaksiCollectionNewTransaksi)) {
                    Penjual oldIdPenjualOfTransaksiCollectionNewTransaksi = transaksiCollectionNewTransaksi.getIdPenjual();
                    transaksiCollectionNewTransaksi.setIdPenjual(penjual);
                    transaksiCollectionNewTransaksi = em.merge(transaksiCollectionNewTransaksi);
                    if (oldIdPenjualOfTransaksiCollectionNewTransaksi != null && !oldIdPenjualOfTransaksiCollectionNewTransaksi.equals(penjual)) {
                        oldIdPenjualOfTransaksiCollectionNewTransaksi.getTransaksiCollection().remove(transaksiCollectionNewTransaksi);
                        oldIdPenjualOfTransaksiCollectionNewTransaksi = em.merge(oldIdPenjualOfTransaksiCollectionNewTransaksi);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = penjual.getIdPenjual();
                if (findPenjual(id) == null) {
                    throw new NonexistentEntityException("The penjual with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Penjual penjual;
            try {
                penjual = em.getReference(Penjual.class, id);
                penjual.getIdPenjual();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The penjual with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Transaksi> transaksiCollectionOrphanCheck = penjual.getTransaksiCollection();
            for (Transaksi transaksiCollectionOrphanCheckTransaksi : transaksiCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Penjual (" + penjual + ") cannot be destroyed since the Transaksi " + transaksiCollectionOrphanCheckTransaksi + " in its transaksiCollection field has a non-nullable idPenjual field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(penjual);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Penjual> findPenjualEntities() {
        return findPenjualEntities(true, -1, -1);
    }

    public List<Penjual> findPenjualEntities(int maxResults, int firstResult) {
        return findPenjualEntities(false, maxResults, firstResult);
    }

    private List<Penjual> findPenjualEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Penjual.class));
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

    public Penjual findPenjual(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Penjual.class, id);
        } finally {
            em.close();
        }
    }

    public int getPenjualCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Penjual> rt = cq.from(Penjual.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
