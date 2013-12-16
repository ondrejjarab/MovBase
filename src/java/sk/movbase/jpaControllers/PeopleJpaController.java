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
import sk.movbase.models.Country;
import sk.movbase.models.Profession;
import sk.movbase.models.User;
import sk.movbase.models.Film;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import sk.movbase.constants.OrderFilmTypes;
import sk.movbase.jpaControllers.exceptions.NonexistentEntityException;
import sk.movbase.jpaControllers.exceptions.RollbackFailureException;
import sk.movbase.models.People;
import sk.movbase.models.People_;

/**
 *
 * @author Ondrej
 */
public class PeopleJpaController implements Serializable {

    public PeopleJpaController(EntityManagerFactory emf) {
        emf.getCache().evictAll();
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(People people) throws RollbackFailureException, Exception {
        if (people.getFilmCollection() == null) {
            people.setFilmCollection(new ArrayList<Film>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Country narodnost = people.getNarodnost();
            if (narodnost != null) {
                narodnost = em.getReference(narodnost.getClass(), narodnost.getKrajinaId());
                people.setNarodnost(narodnost);
            }
            Profession typ = people.getTyp();
            if (typ != null) {
                typ = em.getReference(typ.getClass(), typ.getProfesiaId());
                people.setTyp(typ);
            }
            User autorId = people.getAutorId();
            if (autorId != null) {
                autorId = em.getReference(autorId.getClass(), autorId.getPouzivatelId());
                people.setAutorId(autorId);
            }
            Collection<Film> attachedFilmCollection = new ArrayList<Film>();
            for (Film filmCollectionFilmToAttach : people.getFilmCollection()) {
                filmCollectionFilmToAttach = em.getReference(filmCollectionFilmToAttach.getClass(), filmCollectionFilmToAttach.getFilmId());
                attachedFilmCollection.add(filmCollectionFilmToAttach);
            }
            people.setFilmCollection(attachedFilmCollection);
            em.persist(people);
            if (narodnost != null) {
                narodnost.getPeopleCollection().add(people);
                narodnost = em.merge(narodnost);
            }
            if (typ != null) {
                typ.getPeopleCollection().add(people);
                typ = em.merge(typ);
            }
            if (autorId != null) {
                autorId.getPeopleCollection().add(people);
                autorId = em.merge(autorId);
            }
            for (Film filmCollectionFilm : people.getFilmCollection()) {
                filmCollectionFilm.getPeopleCollection().add(people);
                filmCollectionFilm = em.merge(filmCollectionFilm);
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

    public void edit(People people) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            People persistentPeople = em.find(People.class, people.getOsobnostId());
            Country narodnostOld = persistentPeople.getNarodnost();
            Country narodnostNew = people.getNarodnost();
            Profession typOld = persistentPeople.getTyp();
            Profession typNew = people.getTyp();
            User autorIdOld = persistentPeople.getAutorId();
            User autorIdNew = people.getAutorId();
            Collection<Film> filmCollectionOld = persistentPeople.getFilmCollection();
            Collection<Film> filmCollectionNew = people.getFilmCollection();
            if (narodnostNew != null) {
                narodnostNew = em.getReference(narodnostNew.getClass(), narodnostNew.getKrajinaId());
                people.setNarodnost(narodnostNew);
            }
            if (typNew != null) {
                typNew = em.getReference(typNew.getClass(), typNew.getProfesiaId());
                people.setTyp(typNew);
            }
            if (autorIdNew != null) {
                autorIdNew = em.getReference(autorIdNew.getClass(), autorIdNew.getPouzivatelId());
                people.setAutorId(autorIdNew);
            }
            Collection<Film> attachedFilmCollectionNew = new ArrayList<Film>();
            for (Film filmCollectionNewFilmToAttach : filmCollectionNew) {
                filmCollectionNewFilmToAttach = em.getReference(filmCollectionNewFilmToAttach.getClass(), filmCollectionNewFilmToAttach.getFilmId());
                attachedFilmCollectionNew.add(filmCollectionNewFilmToAttach);
            }
            filmCollectionNew = attachedFilmCollectionNew;
            people.setFilmCollection(filmCollectionNew);
            people = em.merge(people);
            if (narodnostOld != null && !narodnostOld.equals(narodnostNew)) {
                narodnostOld.getPeopleCollection().remove(people);
                narodnostOld = em.merge(narodnostOld);
            }
            if (narodnostNew != null && !narodnostNew.equals(narodnostOld)) {
                narodnostNew.getPeopleCollection().add(people);
                narodnostNew = em.merge(narodnostNew);
            }
            if (typOld != null && !typOld.equals(typNew)) {
                typOld.getPeopleCollection().remove(people);
                typOld = em.merge(typOld);
            }
            if (typNew != null && !typNew.equals(typOld)) {
                typNew.getPeopleCollection().add(people);
                typNew = em.merge(typNew);
            }
            if (autorIdOld != null && !autorIdOld.equals(autorIdNew)) {
                autorIdOld.getPeopleCollection().remove(people);
                autorIdOld = em.merge(autorIdOld);
            }
            if (autorIdNew != null && !autorIdNew.equals(autorIdOld)) {
                autorIdNew.getPeopleCollection().add(people);
                autorIdNew = em.merge(autorIdNew);
            }
            for (Film filmCollectionOldFilm : filmCollectionOld) {
                if (!filmCollectionNew.contains(filmCollectionOldFilm)) {
                    filmCollectionOldFilm.getPeopleCollection().remove(people);
                    filmCollectionOldFilm = em.merge(filmCollectionOldFilm);
                }
            }
            for (Film filmCollectionNewFilm : filmCollectionNew) {
                if (!filmCollectionOld.contains(filmCollectionNewFilm)) {
                    filmCollectionNewFilm.getPeopleCollection().add(people);
                    filmCollectionNewFilm = em.merge(filmCollectionNewFilm);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = people.getOsobnostId();
                if (findPeople(id) == null) {
                    throw new NonexistentEntityException("The people with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            People people;
            try {
                people = em.getReference(People.class, id);
                people.getOsobnostId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The people with id " + id + " no longer exists.", enfe);
            }
            Country narodnost = people.getNarodnost();
            if (narodnost != null) {
                narodnost.getPeopleCollection().remove(people);
                narodnost = em.merge(narodnost);
            }
            Profession typ = people.getTyp();
            if (typ != null) {
                typ.getPeopleCollection().remove(people);
                typ = em.merge(typ);
            }
            User autorId = people.getAutorId();
            if (autorId != null) {
                autorId.getPeopleCollection().remove(people);
                autorId = em.merge(autorId);
            }
            Collection<Film> filmCollection = people.getFilmCollection();
            for (Film filmCollectionFilm : filmCollection) {
                filmCollectionFilm.getPeopleCollection().remove(people);
                filmCollectionFilm = em.merge(filmCollectionFilm);
            }
            em.remove(people);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<People> findPeopleEntities() {
        return findPeopleEntities(true, -1, -1);
    }

    public List<People> findPeopleEntities(int maxResults, int firstResult) {
        return findPeopleEntities(false, maxResults, firstResult);
    }

    private List<People> findPeopleEntities(boolean all, int maxResults, int firstResult) {
		return this.findPeopleEntities(all, maxResults, firstResult, 0, 0, "");
    }
	
	public List<People> findPeopleEntities(boolean all, int maxResults, int firstResult, int orderby, Integer profession, String search) {
        EntityManager em = getEntityManager();
        try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
			Root<People> people_root = cq.from(People.class);
			cq.select(people_root);
			Order order;
			
			// TODO: implementovat vyhladavanie na zaklade profesie
			if(profession!=null && profession>0) {
				//Predicate predicate = em.getCriteriaBuilder().equal(cq.from(Film.class).join("genreCollection").get("zanerId"), genre);
				//cq.where(predicate).distinct(true);
			}
			
			// TODO: implementovat hladanie podla klucoveho slova z nazvu
			if(search!=null && search.length()>0) {
				//Predicate predicate;
				//cq.where(predicate);
			}
			
			switch(orderby) {
				case OrderFilmTypes.NAZOV_DESC: order = cb.desc(people_root.get(People_.meno)); break;
				case OrderFilmTypes.PRIDANY_ASC: order = cb.asc(people_root.get(People_.datumPridania)); break;
				case OrderFilmTypes.PRIDANY_DESC: order = cb.desc(people_root.get(People_.datumPridania)); break;
				default: 
					order = em.getCriteriaBuilder().asc(people_root.get(People_.meno));
			}
			
            cq.orderBy(order);
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
	

    public People findPeople(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(People.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeopleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<People> rt = cq.from(People.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
