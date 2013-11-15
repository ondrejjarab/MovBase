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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "wt_films")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Film.findAll", query = "SELECT f FROM Film f"),
    @NamedQuery(name = "Film.findByFilmId", query = "SELECT f FROM Film f WHERE f.filmId = :filmId"),
    @NamedQuery(name = "Film.findByDatumPridania", query = "SELECT f FROM Film f WHERE f.datumPridania = :datumPridania"),
    @NamedQuery(name = "Film.findByTyp", query = "SELECT f FROM Film f WHERE f.typ = :typ"),
    @NamedQuery(name = "Film.findByNazov", query = "SELECT f FROM Film f WHERE f.nazov = :nazov"),
    @NamedQuery(name = "Film.findByOriginalnyNazov", query = "SELECT f FROM Film f WHERE f.originalnyNazov = :originalnyNazov"),
    @NamedQuery(name = "Film.findByFotografia", query = "SELECT f FROM Film f WHERE f.fotografia = :fotografia"),
    @NamedQuery(name = "Film.findByRokVydania", query = "SELECT f FROM Film f WHERE f.rokVydania = :rokVydania"),
    @NamedQuery(name = "Film.findByMinutaz", query = "SELECT f FROM Film f WHERE f.minutaz = :minutaz"),
    @NamedQuery(name = "Film.findBySchvaleny", query = "SELECT f FROM Film f WHERE f.schvaleny = :schvaleny"),
    @NamedQuery(name = "Film.findByPocetCasti", query = "SELECT f FROM Film f WHERE f.pocetCasti = :pocetCasti")})
public class Film implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "film_id")
    private Integer filmId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datum_pridania")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumPridania;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "typ")
    private String typ;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nazov")
    private String nazov;
    @Size(max = 255)
    @Column(name = "originalny_nazov")
    private String originalnyNazov;
    @Size(max = 255)
    @Column(name = "fotografia")
    private String fotografia;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "popis")
    private String popis;
    @Column(name = "rok_vydania")
    private Short rokVydania;
    @Column(name = "minutaz")
    private Short minutaz;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "schvaleny")
    private String schvaleny;
    @Column(name = "pocet_casti")
    private Short pocetCasti;
    @JoinTable(name = "wt_film_genre", joinColumns = {
        @JoinColumn(name = "film_id", referencedColumnName = "film_id")}, inverseJoinColumns = {
        @JoinColumn(name = "zaner_id", referencedColumnName = "zaner_id")})
    @ManyToMany
    private Collection<Genre> genreCollection;
    @JoinTable(name = "wt_film_person", joinColumns = {
        @JoinColumn(name = "film_id", referencedColumnName = "film_id")}, inverseJoinColumns = {
        @JoinColumn(name = "osobnost_id", referencedColumnName = "osobnost_id")})
    @ManyToMany
    private Collection<People> peopleCollection;
    @JoinTable(name = "wt_film_country", joinColumns = {
        @JoinColumn(name = "film_id", referencedColumnName = "film_id")}, inverseJoinColumns = {
        @JoinColumn(name = "krajina_id", referencedColumnName = "krajina_id")})
    @ManyToMany
    private Collection<Country> countryCollection;
    @JoinColumn(name = "autor_id", referencedColumnName = "pouzivatel_id")
    @ManyToOne(optional = false)
    private User autorId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "filmId")
    private Collection<Comment> commentCollection;

    public Film() {
    }

    public Film(Integer filmId) {
        this.filmId = filmId;
    }

    public Film(Integer filmId, Date datumPridania, String typ, String nazov, String popis, String schvaleny) {
        this.filmId = filmId;
        this.datumPridania = datumPridania;
        this.typ = typ;
        this.nazov = nazov;
        this.popis = popis;
        this.schvaleny = schvaleny;
    }

    public Integer getFilmId() {
        return filmId;
    }

    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }

    public Date getDatumPridania() {
        return datumPridania;
    }

    public void setDatumPridania(Date datumPridania) {
        this.datumPridania = datumPridania;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getOriginalnyNazov() {
        return originalnyNazov;
    }

    public void setOriginalnyNazov(String originalnyNazov) {
        this.originalnyNazov = originalnyNazov;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public Short getRokVydania() {
        return rokVydania;
    }

    public void setRokVydania(Short rokVydania) {
        this.rokVydania = rokVydania;
    }

    public Short getMinutaz() {
        return minutaz;
    }

    public void setMinutaz(Short minutaz) {
        this.minutaz = minutaz;
    }

    public String getSchvaleny() {
        return schvaleny;
    }

    public void setSchvaleny(String schvaleny) {
        this.schvaleny = schvaleny;
    }

    public Short getPocetCasti() {
        return pocetCasti;
    }

    public void setPocetCasti(Short pocetCasti) {
        this.pocetCasti = pocetCasti;
    }

    @XmlTransient
    public Collection<Genre> getGenreCollection() {
        return genreCollection;
    }

    public void setGenreCollection(Collection<Genre> genreCollection) {
        this.genreCollection = genreCollection;
    }

    @XmlTransient
    public Collection<People> getPeopleCollection() {
        return peopleCollection;
    }

    public void setPeopleCollection(Collection<People> peopleCollection) {
        this.peopleCollection = peopleCollection;
    }

    @XmlTransient
    public Collection<Country> getCountryCollection() {
        return countryCollection;
    }

    public void setCountryCollection(Collection<Country> countryCollection) {
        this.countryCollection = countryCollection;
    }

    public User getAutorId() {
        return autorId;
    }

    public void setAutorId(User autorId) {
        this.autorId = autorId;
    }

    @XmlTransient
    public Collection<Comment> getCommentCollection() {
        return commentCollection;
    }

    public void setCommentCollection(Collection<Comment> commentCollection) {
        this.commentCollection = commentCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (filmId != null ? filmId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Film)) {
            return false;
        }
        Film other = (Film) object;
        if ((this.filmId == null && other.filmId != null) || (this.filmId != null && !this.filmId.equals(other.filmId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sk.movbase.models.Film[ filmId=" + filmId + " ]";
    }
    
}
