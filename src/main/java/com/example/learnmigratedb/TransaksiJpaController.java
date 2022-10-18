/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.learnmigratedb;

import com.example.learnmigratedb.exceptions.NonexistentEntityException;
import com.example.learnmigratedb.exceptions.PreexistingEntityException;
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
 * @author NITRO
 */
public class TransaksiJpaController implements Serializable {

    public TransaksiJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Transaksi transaksi) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Barang idBarang = transaksi.getIdBarang();
            if (idBarang != null) {
                idBarang = em.getReference(idBarang.getClass(), idBarang.getIdBarang());
                transaksi.setIdBarang(idBarang);
            }
            Penjual idPenjual = transaksi.getIdPenjual();
            if (idPenjual != null) {
                idPenjual = em.getReference(idPenjual.getClass(), idPenjual.getIdPenjual());
                transaksi.setIdPenjual(idPenjual);
            }
            Pembeli idPembeli = transaksi.getIdPembeli();
            if (idPembeli != null) {
                idPembeli = em.getReference(idPembeli.getClass(), idPembeli.getIdPembeli());
                transaksi.setIdPembeli(idPembeli);
            }
            em.persist(transaksi);
            if (idBarang != null) {
                idBarang.getTransaksiCollection().add(transaksi);
                idBarang = em.merge(idBarang);
            }
            if (idPenjual != null) {
                idPenjual.getTransaksiCollection().add(transaksi);
                idPenjual = em.merge(idPenjual);
            }
            if (idPembeli != null) {
                idPembeli.getTransaksiCollection().add(transaksi);
                idPembeli = em.merge(idPembeli);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTransaksi(transaksi.getIdTransaksi()) != null) {
                throw new PreexistingEntityException("Transaksi " + transaksi + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Transaksi transaksi) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaksi persistentTransaksi = em.find(Transaksi.class, transaksi.getIdTransaksi());
            Barang idBarangOld = persistentTransaksi.getIdBarang();
            Barang idBarangNew = transaksi.getIdBarang();
            Penjual idPenjualOld = persistentTransaksi.getIdPenjual();
            Penjual idPenjualNew = transaksi.getIdPenjual();
            Pembeli idPembeliOld = persistentTransaksi.getIdPembeli();
            Pembeli idPembeliNew = transaksi.getIdPembeli();
            if (idBarangNew != null) {
                idBarangNew = em.getReference(idBarangNew.getClass(), idBarangNew.getIdBarang());
                transaksi.setIdBarang(idBarangNew);
            }
            if (idPenjualNew != null) {
                idPenjualNew = em.getReference(idPenjualNew.getClass(), idPenjualNew.getIdPenjual());
                transaksi.setIdPenjual(idPenjualNew);
            }
            if (idPembeliNew != null) {
                idPembeliNew = em.getReference(idPembeliNew.getClass(), idPembeliNew.getIdPembeli());
                transaksi.setIdPembeli(idPembeliNew);
            }
            transaksi = em.merge(transaksi);
            if (idBarangOld != null && !idBarangOld.equals(idBarangNew)) {
                idBarangOld.getTransaksiCollection().remove(transaksi);
                idBarangOld = em.merge(idBarangOld);
            }
            if (idBarangNew != null && !idBarangNew.equals(idBarangOld)) {
                idBarangNew.getTransaksiCollection().add(transaksi);
                idBarangNew = em.merge(idBarangNew);
            }
            if (idPenjualOld != null && !idPenjualOld.equals(idPenjualNew)) {
                idPenjualOld.getTransaksiCollection().remove(transaksi);
                idPenjualOld = em.merge(idPenjualOld);
            }
            if (idPenjualNew != null && !idPenjualNew.equals(idPenjualOld)) {
                idPenjualNew.getTransaksiCollection().add(transaksi);
                idPenjualNew = em.merge(idPenjualNew);
            }
            if (idPembeliOld != null && !idPembeliOld.equals(idPembeliNew)) {
                idPembeliOld.getTransaksiCollection().remove(transaksi);
                idPembeliOld = em.merge(idPembeliOld);
            }
            if (idPembeliNew != null && !idPembeliNew.equals(idPembeliOld)) {
                idPembeliNew.getTransaksiCollection().add(transaksi);
                idPembeliNew = em.merge(idPembeliNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = transaksi.getIdTransaksi();
                if (findTransaksi(id) == null) {
                    throw new NonexistentEntityException("The transaksi with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaksi transaksi;
            try {
                transaksi = em.getReference(Transaksi.class, id);
                transaksi.getIdTransaksi();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transaksi with id " + id + " no longer exists.", enfe);
            }
            Barang idBarang = transaksi.getIdBarang();
            if (idBarang != null) {
                idBarang.getTransaksiCollection().remove(transaksi);
                idBarang = em.merge(idBarang);
            }
            Penjual idPenjual = transaksi.getIdPenjual();
            if (idPenjual != null) {
                idPenjual.getTransaksiCollection().remove(transaksi);
                idPenjual = em.merge(idPenjual);
            }
            Pembeli idPembeli = transaksi.getIdPembeli();
            if (idPembeli != null) {
                idPembeli.getTransaksiCollection().remove(transaksi);
                idPembeli = em.merge(idPembeli);
            }
            em.remove(transaksi);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Transaksi> findTransaksiEntities() {
        return findTransaksiEntities(true, -1, -1);
    }

    public List<Transaksi> findTransaksiEntities(int maxResults, int firstResult) {
        return findTransaksiEntities(false, maxResults, firstResult);
    }

    private List<Transaksi> findTransaksiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Transaksi.class));
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

    public Transaksi findTransaksi(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Transaksi.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransaksiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Transaksi> rt = cq.from(Transaksi.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
