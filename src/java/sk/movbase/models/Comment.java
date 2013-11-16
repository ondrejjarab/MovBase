/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ondrej
 */
@Entity
@Table(name = "wt_comments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comment.findAll", query = "SELECT c FROM Comment c"),
    @NamedQuery(name = "Comment.findByKomentarId", query = "SELECT c FROM Comment c WHERE c.komentarId = :komentarId"),
    @NamedQuery(name = "Comment.findByDatumPridania", query = "SELECT c FROM Comment c WHERE c.datumPridania = :datumPridania"),
    @NamedQuery(name = "Comment.findByHodnotenie", query = "SELECT c FROM Comment c WHERE c.hodnotenie = :hodnotenie")})
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "komentar_id")
    private Integer komentarId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datum_pridania")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumPridania;
    @Column(name = "hodnotenie")
    private Short hodnotenie;
    @Lob
    @Size(max = 65535)
    @Column(name = "komentar")
    private String komentar;
    @JoinColumn(name = "film_id", referencedColumnName = "film_id")
    @ManyToOne(optional = false)
    private Film filmId;
    @JoinColumn(name = "autor_id", referencedColumnName = "pouzivatel_id")
    @ManyToOne(optional = false)
    private User autorId;

    public Comment() {
    }

    public Comment(Date datumPridania) {
        this.datumPridania = datumPridania;
    }

    public Integer getKomentarId() {
        return komentarId;
    }

    public void setKomentarId(Integer komentarId) {
        this.komentarId = komentarId;
    }

    public Date getDatumPridania() {
        return datumPridania;
    }

    public void setDatumPridania(Date datumPridania) {
        this.datumPridania = datumPridania;
    }

    public Short getHodnotenie() {
        return hodnotenie;
    }

    public void setHodnotenie(Short hodnotenie) {
        this.hodnotenie = hodnotenie;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public Film getFilmId() {
        return filmId;
    }

    public void setFilmId(Film filmId) {
        this.filmId = filmId;
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
        hash += (komentarId != null ? komentarId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comment)) {
            return false;
        }
        Comment other = (Comment) object;
        if ((this.komentarId == null && other.komentarId != null) || (this.komentarId != null && !this.komentarId.equals(other.komentarId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sk.movbase.models.Comment[ komentarId=" + komentarId + " ]";
    }
    
}
