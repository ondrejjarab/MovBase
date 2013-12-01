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
import javax.persistence.Lob;
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
@Table(name = "wt_people")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "People.findAll", query = "SELECT p FROM People p"),
    @NamedQuery(name = "People.findByOsobnostId", query = "SELECT p FROM People p WHERE p.osobnostId = :osobnostId"),
    @NamedQuery(name = "People.findByDatumPridania", query = "SELECT p FROM People p WHERE p.datumPridania = :datumPridania"),
    @NamedQuery(name = "People.findByMeno", query = "SELECT p FROM People p WHERE p.meno = :meno"),
    @NamedQuery(name = "People.findByFotografia", query = "SELECT p FROM People p WHERE p.fotografia = :fotografia"),
    @NamedQuery(name = "People.findBySchvaleny", query = "SELECT p FROM People p WHERE p.schvaleny = :schvaleny")})
public class People implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "osobnost_id")
    private Integer osobnostId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datum_pridania")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumPridania;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "meno")
    private String meno;
    @Size(max = 255)
    @Column(name = "fotografia")
    private String fotografia;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "popis")
    private String popis;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "schvaleny")
    private String schvaleny;
    @ManyToMany(mappedBy = "peopleCollection")
    private Collection<Film> filmCollection;
    @JoinColumn(name = "narodnost", referencedColumnName = "krajina_id")
    @ManyToOne
    private Country narodnost;
    @JoinColumn(name = "typ", referencedColumnName = "profesia_id")
    @ManyToOne(optional = false)
    private Profession typ;
    @JoinColumn(name = "autor_id", referencedColumnName = "pouzivatel_id")
    @ManyToOne(optional = false)
    private User autorId;

    public People() {
    }

    public People(Date datumPridania, String meno, String popis, String schvaleny) {
        this.datumPridania = datumPridania;
        this.meno = meno;
        this.popis = popis;
        this.schvaleny = schvaleny;
    }

    public Integer getOsobnostId() {
        return osobnostId;
    }

    public void setOsobnostId(Integer osobnostId) {
        this.osobnostId = osobnostId;
    }

    public Date getDatumPridania() {
        return datumPridania;
    }

    public void setDatumPridania(Date datumPridania) {
        this.datumPridania = datumPridania;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
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

    public Country getNarodnost() {
        return narodnost;
    }

    public void setNarodnost(Country narodnost) {
        this.narodnost = narodnost;
    }

    public Profession getTyp() {
        return typ;
    }

    public void setTyp(Profession typ) {
        this.typ = typ;
    }

    public User getAutorId() {
        return autorId;
    }

    public void setAutorId(User autorId) {
        this.autorId = autorId;
    }
	
	public String getProfileURL() {
		// TODO: vratit odkaz na profil
		return "#";
	}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (osobnostId != null ? osobnostId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof People)) {
            return false;
        }
        People other = (People) object;
        if ((this.osobnostId == null && other.osobnostId != null) || (this.osobnostId != null && !this.osobnostId.equals(other.osobnostId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sk.movbase.models.People[ osobnostId=" + osobnostId + " ]";
    }
    
}
