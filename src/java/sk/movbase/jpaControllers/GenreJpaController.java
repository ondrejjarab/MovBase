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
import sk.movbase.models.Genre;

/**
 *
 * @author Ondrej
 */
public class GenreJpaController implements Serializable {

    public GenreJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Genre genre) throws RollbackFailureException, Exception {
        if (genre.getFilmCollection() == null) {
            genre.setFilmCollection(new ArrayList<Film>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User autorId = genre.getAutorId();
            if (autorId != null) {
                autorId = em.getReference(autorId.getClass(), autorId.getPouzivatelId());
                genre.setAutorId(autorId);
            }
            Collection<Film> attachedFilmCollection = new ArrayList<Film>();
            for (Film filmCollectionFilmToAttach : genre.getFilmCollection()) {
                filmCollectionFilmToAttach = em.getReference(filmCollectionFilmToAttach.getClass(), filmCollectionFilmToAttach.getFilmId());
                attachedFilmCollection.add(filmCollectionFilmToAttach);
            }
            genre.setFilmCollection(attachedFilmCollection);
            em.persist(genre);
            if (autorId != null) {
                autorId.getGenreCollection().add(genre);
                autorId = em.merge(autorId);
            }
            for (Film filmCollectionFilm : genre.getFilmCollection()) {
                filmCollectionFilm.getGenreCollection().add(genre);
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

    public void edit(Genre genre) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Genre persistentGenre = em.find(Genre.class, genre.getZanerId());
            User autorIdOld = persistentGenre.getAutorId();
            User autorIdNew = genre.getAutorId();
            Collection<Film> filmCollectionOld = persistentGenre.getFilmCollection();
            Collection<Film> filmCollectionNew = genre.getFilmCollection();
            if (autorIdNew != null) {
                autorIdNew = em.getReference(autorIdNew.getClass(), autorIdNew.getPouzivatelId());
                genre.setAutorId(autorIdNew);
            }
            Collection<Film> attachedFilmCollectionNew = new ArrayList<Film>();
            for (Film filmCollectionNewFilmToAttach : filmCollectionNew) {
                filmCollectionNewFilmToAttach = em.getReference(filmCollectionNewFilmToAttach.getClass(), filmCollectionNewFilmToAttach.getFilmId());
                attachedFilmCollectionNew.add(filmCollectionNewFilmToAttach);
            }
            filmCollectionNew = attachedFilmCollectionNew;
            genre.setFilmCollection(filmCollectionNew);
            genre = em.merge(genre);
            if (autorIdOld != null && !autorIdOld.equals(autorIdNew)) {
                autorIdOld.getGenreCollection().remove(genre);
                autorIdOld = em.merge(autorIdOld);
            }
            if (autorIdNew != null && !autorIdNew.equals(autorIdOld)) {
                autorIdNew.getGenreCollection().add(genre);
                autorIdNew = em.merge(autorIdNew);
            }
            for (Film filmCollectionOldFilm : filmCollectionOld) {
                if (!filmCollectionNew.contains(filmCollectionOldFilm)) {
                    filmCollectionOldFilm.getGenreCollection().remove(genre);
                    filmCollectionOldFilm = em.merge(filmCollectionOldFilm);
                }
            }
            for (Film filmCollectionNewFilm : filmCollectionNew) {
                if (!filmCollectionOld.contains(filmCollectionNewFilm)) {
                    filmCollectionNewFilm.getGenreCollection().add(genre);
                    filmCollectionNewFilm = em.merge(filmCollectionNewFilm);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = genre.getZanerId();
                if (findGenre(id) == null) {
                    throw new NonexistentEntityException("The genre with id " + id + " no longer exists.");
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
            Genre genre;
            try {
                genre = em.getReference(Genre.class, id);
                genre.getZanerId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The genre with id " + id + " no longer exists.", enfe);
            }
            User autorId = genre.getAutorId();
            if (autorId != null) {
                autorId.getGenreCollection().remove(genre);
                autorId = em.merge(autorId);
            }
            Collection<Film> filmCollection = genre.getFilmCollection();
            for (Film filmCollectionFilm : filmCollection) {
                filmCollectionFilm.getGenreCollection().remove(genre);
                filmCollectionFilm = em.merge(filmCollectionFilm);
            }
            em.remove(genre);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Genre> findGenreEntities() {
        return findGenreEntities(true, -1, -1);
    }

    public List<Genre> findGenreEntities(int maxResults, int firstResult) {
        return findGenreEntities(false, maxResults, firstResult);
    }

    private List<Genre> findGenreEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Genre.class));
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

    public Genre findGenre(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Genre.class, id);
        } finally {
            em.close();
        }
    }

    public int getGenreCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Genre> rt = cq.from(Genre.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
