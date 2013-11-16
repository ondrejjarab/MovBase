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
@Table(name = "wt_users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByPouzivatelId", query = "SELECT u FROM User u WHERE u.pouzivatelId = :pouzivatelId"),
    @NamedQuery(name = "User.findByFbId", query = "SELECT u FROM User u WHERE u.fbId = :fbId"),
    @NamedQuery(name = "User.findByMeno", query = "SELECT u FROM User u WHERE u.meno = :meno"),
    @NamedQuery(name = "User.findByPohlavie", query = "SELECT u FROM User u WHERE u.pohlavie = :pohlavie"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByDatumRegistracie", query = "SELECT u FROM User u WHERE u.datumRegistracie = :datumRegistracie")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pouzivatel_id")
    private Integer pouzivatelId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fb_id")
    private long fbId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "meno")
    private String meno;
    @Size(max = 2)
    @Column(name = "pohlavie")
    private String pohlavie;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datum_registracie")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumRegistracie;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "autorId")
    private Collection<Film> filmCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "autorId")
    private Collection<Comment> commentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "autorId")
    private Collection<Country> countryCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "autorId")
    private Collection<People> peopleCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "autorId")
    private Collection<Genre> genreCollection;

    public User() {
    }

    public User(Integer pouzivatelId) {
        this.pouzivatelId = pouzivatelId;
    }

    public User(long fbId, String meno, String email, Date datumRegistracie) {
        this.fbId = fbId;
        this.meno = meno;
        this.email = email;
        this.datumRegistracie = datumRegistracie;
    }

    public Integer getPouzivatelId() {
        return pouzivatelId;
    }

    public void setPouzivatelId(Integer pouzivatelId) {
        this.pouzivatelId = pouzivatelId;
    }

    public long getFbId() {
        return fbId;
    }

    public void setFbId(long fbId) {
        this.fbId = fbId;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public String getPohlavie() {
        return pohlavie;
    }

    public void setPohlavie(String pohlavie) {
        this.pohlavie = pohlavie;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDatumRegistracie() {
        return datumRegistracie;
    }

    public void setDatumRegistracie(Date datumRegistracie) {
        this.datumRegistracie = datumRegistracie;
    }

    @XmlTransient
    public Collection<Film> getFilmCollection() {
        return filmCollection;
    }

    public void setFilmCollection(Collection<Film> filmCollection) {
        this.filmCollection = filmCollection;
    }

    @XmlTransient
    public Collection<Comment> getCommentCollection() {
        return commentCollection;
    }

    public void setCommentCollection(Collection<Comment> commentCollection) {
        this.commentCollection = commentCollection;
    }

    @XmlTransient
    public Collection<Country> getCountryCollection() {
        return countryCollection;
    }

    public void setCountryCollection(Collection<Country> countryCollection) {
        this.countryCollection = countryCollection;
    }

    @XmlTransient
    public Collection<People> getPeopleCollection() {
        return peopleCollection;
    }

    public void setPeopleCollection(Collection<People> peopleCollection) {
        this.peopleCollection = peopleCollection;
    }

    @XmlTransient
    public Collection<Genre> getGenreCollection() {
        return genreCollection;
    }

    public void setGenreCollection(Collection<Genre> genreCollection) {
        this.genreCollection = genreCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pouzivatelId != null ? pouzivatelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.pouzivatelId == null && other.pouzivatelId != null) || (this.pouzivatelId != null && !this.pouzivatelId.equals(other.pouzivatelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sk.movbase.models.User[ pouzivatelId=" + pouzivatelId + " ]";
    }
    
}
