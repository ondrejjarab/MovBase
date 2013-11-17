/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.jpaControllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sk.movbase.models.User;
import sk.movbase.models.Film;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import sk.movbase.jpaControllers.exceptions.NonexistentEntityException;
import sk.movbase.jpaControllers.exceptions.RollbackFailureException;
import sk.movbase.models.Country;
import sk.movbase.models.People;

/**
 *
 * @author Ondrej
 */
public class CountryJpaController implements Serializable {

    public CountryJpaController(EntityManagerFactory emf) {
        emf.getCache().evictAll();
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Country country) throws RollbackFailureException, Exception {
        if (country.getFilmCollection() == null) {
            country.setFilmCollection(new ArrayList<Film>());
        }
        if (country.getPeopleCollection() == null) {
            country.setPeopleCollection(new ArrayList<People>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User autorId = country.getAutorId();
            if (autorId != null) {
                autorId = em.getReference(autorId.getClass(), autorId.getPouzivatelId());
                country.setAutorId(autorId);
            }
            Collection<Film> attachedFilmCollection = new ArrayList<Film>();
            for (Film filmCollectionFilmToAttach : country.getFilmCollection()) {
                filmCollectionFilmToAttach = em.getReference(filmCollectionFilmToAttach.getClass(), filmCollectionFilmToAttach.getFilmId());
                attachedFilmCollection.add(filmCollectionFilmToAttach);
            }
            country.setFilmCollection(attachedFilmCollection);
            Collection<People> attachedPeopleCollection = new ArrayList<People>();
            for (People peopleCollectionPeopleToAttach : country.getPeopleCollection()) {
                peopleCollectionPeopleToAttach = em.getReference(peopleCollectionPeopleToAttach.getClass(), peopleCollectionPeopleToAttach.getOsobnostId());
                attachedPeopleCollection.add(peopleCollectionPeopleToAttach);
            }
            country.setPeopleCollection(attachedPeopleCollection);
            em.persist(country);
            if (autorId != null) {
                autorId.getCountryCollection().add(country);
                autorId = em.merge(autorId);
            }
            for (Film filmCollectionFilm : country.getFilmCollection()) {
                filmCollectionFilm.getCountryCollection().add(country);
                filmCollectionFilm = em.merge(filmCollectionFilm);
            }
            for (People peopleCollectionPeople : country.getPeopleCollection()) {
                Country oldNarodnostOfPeopleCollectionPeople = peopleCollectionPeople.getNarodnost();
                peopleCollectionPeople.setNarodnost(country);
                peopleCollectionPeople = em.merge(peopleCollectionPeople);
                if (oldNarodnostOfPeopleCollectionPeople != null) {
                    oldNarodnostOfPeopleCollectionPeople.getPeopleCollection().remove(peopleCollectionPeople);
                    oldNarodnostOfPeopleCollectionPeople = em.merge(oldNarodnostOfPeopleCollectionPeople);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Country country) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Country persistentCountry = em.find(Country.class, country.getKrajinaId());
            User autorIdOld = persistentCountry.getAutorId();
            User autorIdNew = country.getAutorId();
            Collection<Film> filmCollectionOld = persistentCountry.getFilmCollection();
            Collection<Film> filmCollectionNew = country.getFilmCollection();
            Collection<People> peopleCollectionOld = persistentCountry.getPeopleCollection();
            Collection<People> peopleCollectionNew = country.getPeopleCollection();
            if (autorIdNew != null) {
                autorIdNew = em.getReference(autorIdNew.getClass(), autorIdNew.getPouzivatelId());
                country.setAutorId(autorIdNew);
            }
            Collection<Film> attachedFilmCollectionNew = new ArrayList<Film>();
            for (Film filmCollectionNewFilmToAttach : filmCollectionNew) {
                filmCollectionNewFilmToAttach = em.getReference(filmCollectionNewFilmToAttach.getClass(), filmCollectionNewFilmToAttach.getFilmId());
                attachedFilmCollectionNew.add(filmCollectionNewFilmToAttach);
            }
            filmCollectionNew = attachedFilmCollectionNew;
            country.setFilmCollection(filmCollectionNew);
            Collection<People> attachedPeopleCollectionNew = new ArrayList<People>();
            for (People peopleCollectionNewPeopleToAttach : peopleCollectionNew) {
                peopleCollectionNewPeopleToAttach = em.getReference(peopleCollectionNewPeopleToAttach.getClass(), peopleCollectionNewPeopleToAttach.getOsobnostId());
                attachedPeopleCollectionNew.add(peopleCollectionNewPeopleToAttach);
            }
            peopleCollectionNew = attachedPeopleCollectionNew;
            country.setPeopleCollection(peopleCollectionNew);
            country = em.merge(country);
            if (autorIdOld != null && !autorIdOld.equals(autorIdNew)) {
                autorIdOld.getCountryCollection().remove(country);
                autorIdOld = em.merge(autorIdOld);
            }
            if (autorIdNew != null && !autorIdNew.equals(autorIdOld)) {
                autorIdNew.getCountryCollection().add(country);
                autorIdNew = em.merge(autorIdNew);
            }
            for (Film filmCollectionOldFilm : filmCollectionOld) {
                if (!filmCollectionNew.contains(filmCollectionOldFilm)) {
                    filmCollectionOldFilm.getCountryCollection().remove(country);
                    filmCollectionOldFilm = em.merge(filmCollectionOldFilm);
                }
            }
            for (Film filmCollectionNewFilm : filmCollectionNew) {
                if (!filmCollectionOld.contains(filmCollectionNewFilm)) {
                    filmCollectionNewFilm.getCountryCollection().add(country);
                    filmCollectionNewFilm = em.merge(filmCollectionNewFilm);
                }
            }
            for (People peopleCollectionOldPeople : peopleCollectionOld) {
                if (!peopleCollectionNew.contains(peopleCollectionOldPeople)) {
                    peopleCollectionOldPeople.setNarodnost(null);
                    peopleCollectionOldPeople = em.merge(peopleCollectionOldPeople);
                }
            }
            for (People peopleCollectionNewPeople : peopleCollectionNew) {
                if (!peopleCollectionOld.contains(peopleCollectionNewPeople)) {
                    Country oldNarodnostOfPeopleCollectionNewPeople = peopleCollectionNewPeople.getNarodnost();
                    peopleCollectionNewPeople.setNarodnost(country);
                    peopleCollectionNewPeople = em.merge(peopleCollectionNewPeople);
                    if (oldNarodnostOfPeopleCollectionNewPeople != null && !oldNarodnostOfPeopleCollectionNewPeople.equals(country)) {
                        oldNarodnostOfPeopleCollectionNewPeople.getPeopleCollection().remove(peopleCollectionNewPeople);
                        oldNarodnostOfPeopleCollectionNewPeople = em.merge(oldNarodnostOfPeopleCollectionNewPeople);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = country.getKrajinaId();
                if (findCountry(id) == null) {
                    throw new NonexistentEntityException("The country with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Country country;
            try {
                country = em.getReference(Country.class, id);
                country.getKrajinaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The country with id " + id + " no longer exists.", enfe);
            }
            User autorId = country.getAutorId();
            if (autorId != null) {
                autorId.getCountryCollection().remove(country);
                autorId = em.merge(autorId);
            }
            Collection<Film> filmCollection = country.getFilmCollection();
            for (Film filmCollectionFilm : filmCollection) {
                filmCollectionFilm.getCountryCollection().remove(country);
                filmCollectionFilm = em.merge(filmCollectionFilm);
            }
            Collection<People> peopleCollection = country.getPeopleCollection();
            for (People peopleCollectionPeople : peopleCollection) {
                peopleCollectionPeople.setNarodnost(null);
                peopleCollectionPeople = em.merge(peopleCollectionPeople);
            }
            em.remove(country);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Country> findCountryEntities() {
        return findCountryEntities(true, -1, -1);
    }

    public List<Country> findCountryEntities(int maxResults, int firstResult) {
        return findCountryEntities(false, maxResults, firstResult);
    }

    private List<Country> findCountryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Country.class));
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

    public Country findCountry(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Country.class, id);
        } finally {
            em.close();
        }
    }

    public int getCountryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Country> rt = cq.from(Country.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
