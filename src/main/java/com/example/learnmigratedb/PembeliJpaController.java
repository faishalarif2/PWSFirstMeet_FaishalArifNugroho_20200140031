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
public class PembeliJpaController implements Serializable {

    public PembeliJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.example_learnmigratedb_jar_0.0.1-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pembeli pembeli) throws PreexistingEntityException, Exception {
        if (pembeli.getTransaksiCollection() == null) {
            pembeli.setTransaksiCollection(new ArrayList<Transaksi>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Transaksi> attachedTransaksiCollection = new ArrayList<Transaksi>();
            for (Transaksi transaksiCollectionTransaksiToAttach : pembeli.getTransaksiCollection()) {
                transaksiCollectionTransaksiToAttach = em.getReference(transaksiCollectionTransaksiToAttach.getClass(), transaksiCollectionTransaksiToAttach.getIdTransaksi());
                attachedTransaksiCollection.add(transaksiCollectionTransaksiToAttach);
            }
            pembeli.setTransaksiCollection(attachedTransaksiCollection);
            em.persist(pembeli);
            for (Transaksi transaksiCollectionTransaksi : pembeli.getTransaksiCollection()) {
                Pembeli oldIdPembeliOfTransaksiCollectionTransaksi = transaksiCollectionTransaksi.getIdPembeli();
                transaksiCollectionTransaksi.setIdPembeli(pembeli);
                transaksiCollectionTransaksi = em.merge(transaksiCollectionTransaksi);
                if (oldIdPembeliOfTransaksiCollectionTransaksi != null) {
                    oldIdPembeliOfTransaksiCollectionTransaksi.getTransaksiCollection().remove(transaksiCollectionTransaksi);
                    oldIdPembeliOfTransaksiCollectionTransaksi = em.merge(oldIdPembeliOfTransaksiCollectionTransaksi);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPembeli(pembeli.getIdPembeli()) != null) {
                throw new PreexistingEntityException("Pembeli " + pembeli + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pembeli pembeli) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pembeli persistentPembeli = em.find(Pembeli.class, pembeli.getIdPembeli());
            Collection<Transaksi> transaksiCollectionOld = persistentPembeli.getTransaksiCollection();
            Collection<Transaksi> transaksiCollectionNew = pembeli.getTransaksiCollection();
            List<String> illegalOrphanMessages = null;
            for (Transaksi transaksiCollectionOldTransaksi : transaksiCollectionOld) {
                if (!transaksiCollectionNew.contains(transaksiCollectionOldTransaksi)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Transaksi " + transaksiCollectionOldTransaksi + " since its idPembeli field is not nullable.");
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
            pembeli.setTransaksiCollection(transaksiCollectionNew);
            pembeli = em.merge(pembeli);
            for (Transaksi transaksiCollectionNewTransaksi : transaksiCollectionNew) {
                if (!transaksiCollectionOld.contains(transaksiCollectionNewTransaksi)) {
                    Pembeli oldIdPembeliOfTransaksiCollectionNewTransaksi = transaksiCollectionNewTransaksi.getIdPembeli();
                    transaksiCollectionNewTransaksi.setIdPembeli(pembeli);
                    transaksiCollectionNewTransaksi = em.merge(transaksiCollectionNewTransaksi);
                    if (oldIdPembeliOfTransaksiCollectionNewTransaksi != null && !oldIdPembeliOfTransaksiCollectionNewTransaksi.equals(pembeli)) {
                        oldIdPembeliOfTransaksiCollectionNewTransaksi.getTransaksiCollection().remove(transaksiCollectionNewTransaksi);
                        oldIdPembeliOfTransaksiCollectionNewTransaksi = em.merge(oldIdPembeliOfTransaksiCollectionNewTransaksi);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pembeli.getIdPembeli();
                if (findPembeli(id) == null) {
                    throw new NonexistentEntityException("The pembeli with id " + id + " no longer exists.");
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
            Pembeli pembeli;
            try {
                pembeli = em.getReference(Pembeli.class, id);
                pembeli.getIdPembeli();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pembeli with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Transaksi> transaksiCollectionOrphanCheck = pembeli.getTransaksiCollection();
            for (Transaksi transaksiCollectionOrphanCheckTransaksi : transaksiCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pembeli (" + pembeli + ") cannot be destroyed since the Transaksi " + transaksiCollectionOrphanCheckTransaksi + " in its transaksiCollection field has a non-nullable idPembeli field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pembeli);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pembeli> findPembeliEntities() {
        return findPembeliEntities(true, -1, -1);
    }

    public List<Pembeli> findPembeliEntities(int maxResults, int firstResult) {
        return findPembeliEntities(false, maxResults, firstResult);
    }

    private List<Pembeli> findPembeliEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pembeli.class));
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

    public Pembeli findPembeli(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pembeli.class, id);
        } finally {
            em.close();
        }
    }

    public int getPembeliCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pembeli> rt = cq.from(Pembeli.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
