/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ondrej
 */
@Entity
@Table(name = "wt_genres")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Genre.findAll", query = "SELECT g FROM Genre g"),
    @NamedQuery(name = "Genre.findByZanerId", query = "SELECT g FROM Genre g WHERE g.zanerId = :zanerId"),
    @NamedQuery(name = "Genre.findByDatumPridania", query = "SELECT g FROM Genre g WHERE g.datumPridania = :datumPridania"),
    @NamedQuery(name = "Genre.findByNazov", query = "SELECT g FROM Genre g WHERE g.nazov = :nazov"),
    @NamedQuery(name = "Genre.findBySchvaleny", query = "SELECT g FROM Genre g WHERE g.schvaleny = :schvaleny")})
public class Genre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "zaner_id")
    private Short zanerId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datum_pridania")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumPridania;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nazov")
    private String nazov;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "schvaleny")
    private String schvaleny;
    @ManyToMany(mappedBy = "genreCollection")
    private Collection<Film> filmCollection;
    @JoinColumn(name = "autor_id", referencedColumnName = "pouzivatel_id")
    @ManyToOne(optional = false)
    private User autorId;

    public Genre() {
    }

    public Genre(Date datumPridania, String nazov, String schvaleny) {
        this.zanerId = zanerId;
        this.datumPridania = datumPridania;
        this.nazov = nazov;
        this.schvaleny = schvaleny;
    }

    public Short getZanerId() {
        return zanerId;
    }

    public void setZanerId(Short zanerId) {
        this.zanerId = zanerId;
    }

    public Date getDatumPridania() {
        return datumPridania;
    }

    public void setDatumPridania(Date datumPridania) {
        this.datumPridania = datumPridania;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getSchvaleny() {
        return schvaleny;
    }

    public void setSchvaleny(String schvaleny) {
        this.schvaleny = schvaleny;
    }

    @XmlTransient
    public Collection<Film> getFilmCollection() {
        return filmCollection;
    }

    public void setFilmCollection(Collection<Film> filmCollection) {
        this.filmCollection = filmCollection;
    }

    public User getAutorId() {
        return autorId;
    }

    public void setAutorId(User autorId) {
        this.autorId = autorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (zanerId != null ? zanerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Genre)) {
            return false;
        }
        Genre other = (Genre) object;
        if ((this.zanerId == null && other.zanerId != null) || (this.zanerId != null && !this.zanerId.equals(other.zanerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sk.movbase.models.Genre[ zanerId=" + zanerId + " ]";
    }
    
}
