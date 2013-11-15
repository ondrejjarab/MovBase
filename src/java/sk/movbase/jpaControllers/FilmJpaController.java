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
import sk.movbase.models.Genre;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import sk.movbase.jpaControllers.exceptions.IllegalOrphanException;
import sk.movbase.jpaControllers.exceptions.NonexistentEntityException;
import sk.movbase.jpaControllers.exceptions.RollbackFailureException;
import sk.movbase.models.People;
import sk.movbase.models.Country;
import sk.movbase.models.Comment;
import sk.movbase.models.Film;

/**
 *
 * @author Ondrej
 */
public class FilmJpaController implements Serializable {

    public FilmJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Film film) throws RollbackFailureException, Exception {
        if (film.getGenreCollection() == null) {
            film.setGenreCollection(new ArrayList<Genre>());
        }
        if (film.getPeopleCollection() == null) {
            film.setPeopleCollection(new ArrayList<People>());
        }
        if (film.getCountryCollection() == null) {
            film.setCountryCollection(new ArrayList<Country>());
        }
        if (film.getCommentCollection() == null) {
            film.setCommentCollection(new ArrayList<Comment>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            User autorId = film.getAutorId();
            if (autorId != null) {
                autorId = em.getReference(autorId.getClass(), autorId.getPouzivatelId());
                film.setAutorId(autorId);
            }
            Collection<Genre> attachedGenreCollection = new ArrayList<Genre>();
            for (Genre genreCollectionGenreToAttach : film.getGenreCollection()) {
                genreCollectionGenreToAttach = em.getReference(genreCollectionGenreToAttach.getClass(), genreCollectionGenreToAttach.getZanerId());
                attachedGenreCollection.add(genreCollectionGenreToAttach);
            }
            film.setGenreCollection(attachedGenreCollection);
            Collection<People> attachedPeopleCollection = new ArrayList<People>();
            for (People peopleCollectionPeopleToAttach : film.getPeopleCollection()) {
                peopleCollectionPeopleToAttach = em.getReference(peopleCollectionPeopleToAttach.getClass(), peopleCollectionPeopleToAttach.getOsobnostId());
                attachedPeopleCollection.add(peopleCollectionPeopleToAttach);
            }
            film.setPeopleCollection(attachedPeopleCollection);
            Collection<Country> attachedCountryCollection = new ArrayList<Country>();
            for (Country countryCollectionCountryToAttach : film.getCountryCollection()) {
                countryCollectionCountryToAttach = em.getReference(countryCollectionCountryToAttach.getClass(), countryCollectionCountryToAttach.getKrajinaId());
                attachedCountryCollection.add(countryCollectionCountryToAttach);
            }
            film.setCountryCollection(attachedCountryCollection);
            Collection<Comment> attachedCommentCollection = new ArrayList<Comment>();
            for (Comment commentCollectionCommentToAttach : film.getCommentCollection()) {
                commentCollectionCommentToAttach = em.getReference(commentCollectionCommentToAttach.getClass(), commentCollectionCommentToAttach.getKomentarId());
                attachedCommentCollection.add(commentCollectionCommentToAttach);
            }
            film.setCommentCollection(attachedCommentCollection);
            em.persist(film);
            if (autorId != null) {
                autorId.getFilmCollection().add(film);
                autorId = em.merge(autorId);
            }
            for (Genre genreCollectionGenre : film.getGenreCollection()) {
                genreCollectionGenre.getFilmCollection().add(film);
                genreCollectionGenre = em.merge(genreCollectionGenre);
            }
            for (People peopleCollectionPeople : film.getPeopleCollection()) {
                peopleCollectionPeople.getFilmCollection().add(film);
                peopleCollectionPeople = em.merge(peopleCollectionPeople);
            }
            for (Country countryCollectionCountry : film.getCountryCollection()) {
                countryCollectionCountry.getFilmCollection().add(film);
                countryCollectionCountry = em.merge(countryCollectionCountry);
            }
            for (Comment commentCollectionComment : film.getCommentCollection()) {
                Film oldFilmIdOfCommentCollectionComment = commentCollectionComment.getFilmId();
                commentCollectionComment.setFilmId(film);
                commentCollectionComment = em.merge(commentCollectionComment);
                if (oldFilmIdOfCommentCollectionComment != null) {
                    oldFilmIdOfCommentCollectionComment.getCommentCollection().remove(commentCollectionComment);
                    oldFilmIdOfCommentCollectionComment = em.merge(oldFilmIdOfCommentCollectionComment);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Film film) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Film persistentFilm = em.find(Film.class, film.getFilmId());
            User autorIdOld = persistentFilm.getAutorId();
            User autorIdNew = film.getAutorId();
            Collection<Genre> genreCollectionOld = persistentFilm.getGenreCollection();
            Collection<Genre> genreCollectionNew = film.getGenreCollection();
            Collection<People> peopleCollectionOld = persistentFilm.getPeopleCollection();
            Collection<People> peopleCollectionNew = film.getPeopleCollection();
            Collection<Country> countryCollectionOld = persistentFilm.getCountryCollection();
            Collection<Country> countryCollectionNew = film.getCountryCollection();
            Collection<Comment> commentCollectionOld = persistentFilm.getCommentCollection();
            Collection<Comment> commentCollectionNew = film.getCommentCollection();
            List<String> illegalOrphanMessages = null;
            for (Comment commentCollectionOldComment : commentCollectionOld) {
                if (!commentCollectionNew.contains(commentCollectionOldComment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comment " + commentCollectionOldComment + " since its filmId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (autorIdNew != null) {
                autorIdNew = em.getReference(autorIdNew.getClass(), autorIdNew.getPouzivatelId());
                film.setAutorId(autorIdNew);
            }
            Collection<Genre> attachedGenreCollectionNew = new ArrayList<Genre>();
            for (Genre genreCollectionNewGenreToAttach : genreCollectionNew) {
                genreCollectionNewGenreToAttach = em.getReference(genreCollectionNewGenreToAttach.getClass(), genreCollectionNewGenreToAttach.getZanerId());
                attachedGenreCollectionNew.add(genreCollectionNewGenreToAttach);
            }
            genreCollectionNew = attachedGenreCollectionNew;
            film.setGenreCollection(genreCollectionNew);
            Collection<People> attachedPeopleCollectionNew = new ArrayList<People>();
            for (People peopleCollectionNewPeopleToAttach : peopleCollectionNew) {
                peopleCollectionNewPeopleToAttach = em.getReference(peopleCollectionNewPeopleToAttach.getClass(), peopleCollectionNewPeopleToAttach.getOsobnostId());
                attachedPeopleCollectionNew.add(peopleCollectionNewPeopleToAttach);
            }
            peopleCollectionNew = attachedPeopleCollectionNew;
            film.setPeopleCollection(peopleCollectionNew);
            Collection<Country> attachedCountryCollectionNew = new ArrayList<Country>();
            for (Country countryCollectionNewCountryToAttach : countryCollectionNew) {
                countryCollectionNewCountryToAttach = em.getReference(countryCollectionNewCountryToAttach.getClass(), countryCollectionNewCountryToAttach.getKrajinaId());
                attachedCountryCollectionNew.add(countryCollectionNewCountryToAttach);
            }
            countryCollectionNew = attachedCountryCollectionNew;
            film.setCountryCollection(countryCollectionNew);
            Collection<Comment> attachedCommentCollectionNew = new ArrayList<Comment>();
            for (Comment commentCollectionNewCommentToAttach : commentCollectionNew) {
                commentCollectionNewCommentToAttach = em.getReference(commentCollectionNewCommentToAttach.getClass(), commentCollectionNewCommentToAttach.getKomentarId());
                attachedCommentCollectionNew.add(commentCollectionNewCommentToAttach);
            }
            commentCollectionNew = attachedCommentCollectionNew;
            film.setCommentCollection(commentCollectionNew);
            film = em.merge(film);
            if (autorIdOld != null && !autorIdOld.equals(autorIdNew)) {
                autorIdOld.getFilmCollection().remove(film);
                autorIdOld = em.merge(autorIdOld);
            }
            if (autorIdNew != null && !autorIdNew.equals(autorIdOld)) {
                autorIdNew.getFilmCollection().add(film);
                autorIdNew = em.merge(autorIdNew);
            }
            for (Genre genreCollectionOldGenre : genreCollectionOld) {
                if (!genreCollectionNew.contains(genreCollectionOldGenre)) {
                    genreCollectionOldGenre.getFilmCollection().remove(film);
                    genreCollectionOldGenre = em.merge(genreCollectionOldGenre);
                }
            }
            for (Genre genreCollectionNewGenre : genreCollectionNew) {
                if (!genreCollectionOld.contains(genreCollectionNewGenre)) {
                    genreCollectionNewGenre.getFilmCollection().add(film);
                    genreCollectionNewGenre = em.merge(genreCollectionNewGenre);
                }
            }
            for (People peopleCollectionOldPeople : peopleCollectionOld) {
                if (!peopleCollectionNew.contains(peopleCollectionOldPeople)) {
                    peopleCollectionOldPeople.getFilmCollection().remove(film);
                    peopleCollectionOldPeople = em.merge(peopleCollectionOldPeople);
                }
            }
            for (People peopleCollectionNewPeople : peopleCollectionNew) {
                if (!peopleCollectionOld.contains(peopleCollectionNewPeople)) {
                    peopleCollectionNewPeople.getFilmCollection().add(film);
                    peopleCollectionNewPeople = em.merge(peopleCollectionNewPeople);
                }
            }
            for (Country countryCollectionOldCountry : countryCollectionOld) {
                if (!countryCollectionNew.contains(countryCollectionOldCountry)) {
                    countryCollectionOldCountry.getFilmCollection().remove(film);
                    countryCollectionOldCountry = em.merge(countryCollectionOldCountry);
                }
            }
            for (Country countryCollectionNewCountry : countryCollectionNew) {
                if (!countryCollectionOld.contains(countryCollectionNewCountry)) {
                    countryCollectionNewCountry.getFilmCollection().add(film);
                    countryCollectionNewCountry = em.merge(countryCollectionNewCountry);
                }
            }
            for (Comment commentCollectionNewComment : commentCollectionNew) {
                if (!commentCollectionOld.contains(commentCollectionNewComment)) {
                    Film oldFilmIdOfCommentCollectionNewComment = commentCollectionNewComment.getFilmId();
                    commentCollectionNewComment.setFilmId(film);
                    commentCollectionNewComment = em.merge(commentCollectionNewComment);
                    if (oldFilmIdOfCommentCollectionNewComment != null && !oldFilmIdOfCommentCollectionNewComment.equals(film)) {
                        oldFilmIdOfCommentCollectionNewComment.getCommentCollection().remove(commentCollectionNewComment);
                        oldFilmIdOfCommentCollectionNewComment = em.merge(oldFilmIdOfCommentCollectionNewComment);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = film.getFilmId();
                if (findFilm(id) == null) {
                    throw new NonexistentEntityException("The film with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Film film;
            try {
                film = em.getReference(Film.class, id);
                film.getFilmId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The film with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Comment> commentCollectionOrphanCheck = film.getCommentCollection();
            for (Comment commentCollectionOrphanCheckComment : commentCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Film (" + film + ") cannot be destroyed since the Comment " + commentCollectionOrphanCheckComment + " in its commentCollection field has a non-nullable filmId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User autorId = film.getAutorId();
            if (autorId != null) {
                autorId.getFilmCollection().remove(film);
                autorId = em.merge(autorId);
            }
            Collection<Genre> genreCollection = film.getGenreCollection();
            for (Genre genreCollectionGenre : genreCollection) {
                genreCollectionGenre.getFilmCollection().remove(film);
                genreCollectionGenre = em.merge(genreCollectionGenre);
            }
            Collection<People> peopleCollection = film.getPeopleCollection();
            for (People peopleCollectionPeople : peopleCollection) {
                peopleCollectionPeople.getFilmCollection().remove(film);
                peopleCollectionPeople = em.merge(peopleCollectionPeople);
            }
            Collection<Country> countryCollection = film.getCountryCollection();
            for (Country countryCollectionCountry : countryCollection) {
                countryCollectionCountry.getFilmCollection().remove(film);
                countryCollectionCountry = em.merge(countryCollectionCountry);
            }
            em.remove(film);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Film> findFilmEntities() {
        return findFilmEntities(true, -1, -1);
    }

    public List<Film> findFilmEntities(int maxResults, int firstResult) {
        return findFilmEntities(false, maxResults, firstResult);
    }

    private List<Film> findFilmEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Film.class));
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

    public Film findFilm(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Film.class, id);
        } finally {
            em.close();
        }
    }

    public int getFilmCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Film> rt = cq.from(Film.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
