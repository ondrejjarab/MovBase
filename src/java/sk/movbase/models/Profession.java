/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.models;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ondrej
 */
@Entity
@Table(name = "wt_professions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Profession.findAll", query = "SELECT p FROM Profession p"),
    @NamedQuery(name = "Profession.findByProfesiaId", query = "SELECT p FROM Profession p WHERE p.profesiaId = :profesiaId"),
    @NamedQuery(name = "Profession.findByNazov", query = "SELECT p FROM Profession p WHERE p.nazov = :nazov")})
public class Profession implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "profesia_id")
    private Short profesiaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nazov")
    private String nazov;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typ")
    private Collection<People> peopleCollection;

    public Profession() {
    }

    public Profession(String nazov) {
        this.profesiaId = profesiaId;
        this.nazov = nazov;
    }

    public Short getProfesiaId() {
        return profesiaId;
    }

    public void setProfesiaId(Short profesiaId) {
        this.profesiaId = profesiaId;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    @XmlTransient
    public Collection<People> getPeopleCollection() {
        return peopleCollection;
    }

    public void setPeopleCollection(Collection<People> peopleCollection) {
        this.peopleCollection = peopleCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (profesiaId != null ? profesiaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Profession)) {
            return false;
        }
        Profession other = (Profession) object;
        if ((this.profesiaId == null && other.profesiaId != null) || (this.profesiaId != null && !this.profesiaId.equals(other.profesiaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sk.movbase.models.Profession[ profesiaId=" + profesiaId + " ]";
    }
    
}
