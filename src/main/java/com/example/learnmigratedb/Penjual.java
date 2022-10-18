/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.learnmigratedb;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author NITRO
 */
@Entity
@Table(name = "penjual")
@NamedQueries({
    @NamedQuery(name = "Penjual.findAll", query = "SELECT p FROM Penjual p"),
    @NamedQuery(name = "Penjual.findByIdPenjual", query = "SELECT p FROM Penjual p WHERE p.idPenjual = :idPenjual"),
    @NamedQuery(name = "Penjual.findByNamaPenjual", query = "SELECT p FROM Penjual p WHERE p.namaPenjual = :namaPenjual"),
    @NamedQuery(name = "Penjual.findByNoTelp", query = "SELECT p FROM Penjual p WHERE p.noTelp = :noTelp"),
    @NamedQuery(name = "Penjual.findByAlamat", query = "SELECT p FROM Penjual p WHERE p.alamat = :alamat")})
public class Penjual implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_Penjual")
    private Integer idPenjual;
    @Basic(optional = false)
    @Column(name = "Nama_Penjual")
    private String namaPenjual;
    @Basic(optional = false)
    @Column(name = "No_Telp")
    private String noTelp;
    @Basic(optional = false)
    @Column(name = "Alamat")
    private String alamat;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPenjual")
    private Collection<Transaksi> transaksiCollection;

    public Penjual() {
    }

    public Penjual(Integer idPenjual) {
        this.idPenjual = idPenjual;
    }

    public Penjual(Integer idPenjual, String namaPenjual, String noTelp, String alamat) {
        this.idPenjual = idPenjual;
        this.namaPenjual = namaPenjual;
        this.noTelp = noTelp;
        this.alamat = alamat;
    }

    public Integer getIdPenjual() {
        return idPenjual;
    }

    public void setIdPenjual(Integer idPenjual) {
        this.idPenjual = idPenjual;
    }

    public String getNamaPenjual() {
        return namaPenjual;
    }

    public void setNamaPenjual(String namaPenjual) {
        this.namaPenjual = namaPenjual;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Collection<Transaksi> getTransaksiCollection() {
        return transaksiCollection;
    }

    public void setTransaksiCollection(Collection<Transaksi> transaksiCollection) {
        this.transaksiCollection = transaksiCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPenjual != null ? idPenjual.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Penjual)) {
            return false;
        }
        Penjual other = (Penjual) object;
        if ((this.idPenjual == null && other.idPenjual != null) || (this.idPenjual != null && !this.idPenjual.equals(other.idPenjual))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.learnmigratedb.Penjual[ idPenjual=" + idPenjual + " ]";
    }
    
}
