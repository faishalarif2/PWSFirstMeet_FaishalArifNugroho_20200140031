/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.learnmigratedb;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author NITRO
 */
@Entity
@Table(name = "transaksi")
@NamedQueries({
    @NamedQuery(name = "Transaksi.findAll", query = "SELECT t FROM Transaksi t"),
    @NamedQuery(name = "Transaksi.findByIdTransaksi", query = "SELECT t FROM Transaksi t WHERE t.idTransaksi = :idTransaksi"),
    @NamedQuery(name = "Transaksi.findByTanggal", query = "SELECT t FROM Transaksi t WHERE t.tanggal = :tanggal"),
    @NamedQuery(name = "Transaksi.findByJenisTransaksi", query = "SELECT t FROM Transaksi t WHERE t.jenisTransaksi = :jenisTransaksi")})
public class Transaksi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_Transaksi")
    private Integer idTransaksi;
    @Basic(optional = false)
    @Column(name = "Tanggal")
    @Temporal(TemporalType.DATE)
    private Date tanggal;
    @Basic(optional = false)
    @Column(name = "Jenis_Transaksi")
    private String jenisTransaksi;
    @JoinColumn(name = "Id_Barang", referencedColumnName = "Id_Barang")
    @ManyToOne(optional = false)
    private Barang idBarang;
    @JoinColumn(name = "Id_Penjual", referencedColumnName = "Id_Penjual")
    @ManyToOne(optional = false)
    private Penjual idPenjual;
    @JoinColumn(name = "Id_Pembeli", referencedColumnName = "Id_Pembeli")
    @ManyToOne(optional = false)
    private Pembeli idPembeli;

    public Transaksi() {
    }

    public Transaksi(Integer idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public Transaksi(Integer idTransaksi, Date tanggal, String jenisTransaksi) {
        this.idTransaksi = idTransaksi;
        this.tanggal = tanggal;
        this.jenisTransaksi = jenisTransaksi;
    }

    public Integer getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(Integer idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public String getJenisTransaksi() {
        return jenisTransaksi;
    }

    public void setJenisTransaksi(String jenisTransaksi) {
        this.jenisTransaksi = jenisTransaksi;
    }

    public Barang getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(Barang idBarang) {
        this.idBarang = idBarang;
    }

    public Penjual getIdPenjual() {
        return idPenjual;
    }

    public void setIdPenjual(Penjual idPenjual) {
        this.idPenjual = idPenjual;
    }

    public Pembeli getIdPembeli() {
        return idPembeli;
    }

    public void setIdPembeli(Pembeli idPembeli) {
        this.idPembeli = idPembeli;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTransaksi != null ? idTransaksi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaksi)) {
            return false;
        }
        Transaksi other = (Transaksi) object;
        if ((this.idTransaksi == null && other.idTransaksi != null) || (this.idTransaksi != null && !this.idTransaksi.equals(other.idTransaksi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.learnmigratedb.Transaksi[ idTransaksi=" + idTransaksi + " ]";
    }
    
}
