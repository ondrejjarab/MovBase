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
import sk.movbase.models.Film;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import sk.movbase.jpaControllers.exceptions.IllegalOrphanException;
import sk.movbase.jpaControllers.exceptions.NonexistentEntityException;
import sk.movbase.jpaControllers.exceptions.RollbackFailureException;
import sk.movbase.models.Comment;
import sk.movbase.models.Country;
import sk.movbase.models.People;
import sk.movbase.models.Genre;
import sk.movbase.models.User;

/**
 *
 * @author Ondrej
 */
public class UserJpaController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        emf.getCache().evictAll();
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) throws RollbackFailureException, Exception {
        if (user.getFilmCollection() == null) {
            user.setFilmCollection(new ArrayList<Film>());
        }
        if (user.getCommentCollection() == null) {
            user.setCommentCollection(new ArrayList<Comment>());
        }
        if (user.getCountryCollection() == null) {
            user.setCountryCollection(new ArrayList<Country>());
        }
        if (user.getPeopleCollection() == null) {
            user.setPeopleCollection(new ArrayList<People>());
        }
        if (user.getGenreCollection() == null) {
            user.setGenreCollection(new ArrayList<Genre>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Film> attachedFilmCollection = new ArrayList<Film>();
            for (Film filmCollectionFilmToAttach : user.getFilmCollection()) {
                filmCollectionFilmToAttach = em.getReference(filmCollectionFilmToAttach.getClass(), filmCollectionFilmToAttach.getFilmId());
                attachedFilmCollection.add(filmCollectionFilmToAttach);
            }
            user.setFilmCollection(attachedFilmCollection);
            Collection<Comment> attachedCommentCollection = new ArrayList<Comment>();
            for (Comment commentCollectionCommentToAttach : user.getCommentCollection()) {
                commentCollectionCommentToAttach = em.getReference(commentCollectionCommentToAttach.getClass(), commentCollectionCommentToAttach.getKomentarId());
                attachedCommentCollection.add(commentCollectionCommentToAttach);
            }
            user.setCommentCollection(attachedCommentCollection);
            Collection<Country> attachedCountryCollection = new ArrayList<Country>();
            for (Country countryCollectionCountryToAttach : user.getCountryCollection()) {
                countryCollectionCountryToAttach = em.getReference(countryCollectionCountryToAttach.getClass(), countryCollectionCountryToAttach.getKrajinaId());
                attachedCountryCollection.add(countryCollectionCountryToAttach);
            }
            user.setCountryCollection(attachedCountryCollection);
            Collection<People> attachedPeopleCollection = new ArrayList<People>();
            for (People peopleCollectionPeopleToAttach : user.getPeopleCollection()) {
                peopleCollectionPeopleToAttach = em.getReference(peopleCollectionPeopleToAttach.getClass(), peopleCollectionPeopleToAttach.getOsobnostId());
                attachedPeopleCollection.add(peopleCollectionPeopleToAttach);
            }
            user.setPeopleCollection(attachedPeopleCollection);
            Collection<Genre> attachedGenreCollection = new ArrayList<Genre>();
            for (Genre genreCollectionGenreToAttach : user.getGenreCollection()) {
                genreCollectionGenreToAttach = em.getReference(genreCollectionGenreToAttach.getClass(), genreCollectionGenreToAttach.getZanerId());
                attachedGenreCollection.add(genreCollectionGenreToAttach);
            }
            user.setGenreCollection(attachedGenreCollection);
            em.persist(user);
            for (Film filmCollectionFilm : user.getFilmCollection()) {
                User oldAutorIdOfFilmCollectionFilm = filmCollectionFilm.getAutorId();
                filmCollectionFilm.setAutorId(user);
                filmCollectionFilm = em.merge(filmCollectionFilm);
                if (oldAutorIdOfFilmCollectionFilm != null) {
                    oldAutorIdOfFilmCollectionFilm.getFilmCollection().remove(filmCollectionFilm);
                    oldAutorIdOfFilmCollectionFilm = em.merge(oldAutorIdOfFilmCollectionFilm);
                }
            }
            for (Comment commentCollectionComment : user.getCommentCollection()) {
                User oldAutorIdOfCommentCollectionComment = commentCollectionComment.getAutorId();
                commentCollectionComment.setAutorId(user);
                commentCollectionComment = em.merge(commentCollectionComment);
                if (oldAutorIdOfCommentCollectionComment != null) {
                    oldAutorIdOfCommentCollectionComment.getCommentCollection().remove(commentCollectionComment);
                    oldAutorIdOfCommentCollectionComment = em.merge(oldAutorIdOfCommentCollectionComment);
                }
            }
            for (Country countryCollectionCountry : user.getCountryCollection()) {
                User oldAutorIdOfCountryCollectionCountry = countryCollectionCountry.getAutorId();
                countryCollectionCountry.setAutorId(user);
                countryCollectionCountry = em.merge(countryCollectionCountry);
                if (oldAutorIdOfCountryCollectionCountry != null) {
                    oldAutorIdOfCountryCollectionCountry.getCountryCollection().remove(countryCollectionCountry);
                    oldAutorIdOfCountryCollectionCountry = em.merge(oldAutorIdOfCountryCollectionCountry);
                }
            }
            for (People peopleCollectionPeople : user.getPeopleCollection()) {
                User oldAutorIdOfPeopleCollectionPeople = peopleCollectionPeople.getAutorId();
                peopleCollectionPeople.setAutorId(user);
                peopleCollectionPeople = em.merge(peopleCollectionPeople);
                if (oldAutorIdOfPeopleCollectionPeople != null) {
                    oldAutorIdOfPeopleCollectionPeople.getPeopleCollection().remove(peopleCollectionPeople);
                    oldAutorIdOfPeopleCollectionPeople = em.merge(oldAutorIdOfPeopleCollectionPeople);
                }
            }
            for (Genre genreCollectionGenre : user.getGenreCollection()) {
                User oldAutorIdOfGenreCollectionGenre = genreCollectionGenre.getAutorId();
                genreCollectionGenre.setAutorId(user);
                genreCollectionGenre = em.merge(genreCollectionGenre);
                if (oldAutorIdOfGenreCollectionGenre != null) {
                    oldAutorIdOfGenreCollectionGenre.getGenreCollection().remove(genreCollectionGenre);
                    oldAutorIdOfGenreCollectionGenre = em.merge(oldAutorIdOfGenreCollectionGenre);
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

    public void edit(User user) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getPouzivatelId());
            Collection<Film> filmCollectionOld = persistentUser.getFilmCollection();
            Collection<Film> filmCollectionNew = user.getFilmCollection();
            Collection<Comment> commentCollectionOld = persistentUser.getCommentCollection();
            Collection<Comment> commentCollectionNew = user.getCommentCollection();
            Collection<Country> countryCollectionOld = persistentUser.getCountryCollection();
            Collection<Country> countryCollectionNew = user.getCountryCollection();
            Collection<People> peopleCollectionOld = persistentUser.getPeopleCollection();
            Collection<People> peopleCollectionNew = user.getPeopleCollection();
            Collection<Genre> genreCollectionOld = persistentUser.getGenreCollection();
            Collection<Genre> genreCollectionNew = user.getGenreCollection();
            List<String> illegalOrphanMessages = null;
            for (Film filmCollectionOldFilm : filmCollectionOld) {
                if (!filmCollectionNew.contains(filmCollectionOldFilm)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Film " + filmCollectionOldFilm + " since its autorId field is not nullable.");
                }
            }
            for (Comment commentCollectionOldComment : commentCollectionOld) {
                if (!commentCollectionNew.contains(commentCollectionOldComment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comment " + commentCollectionOldComment + " since its autorId field is not nullable.");
                }
            }
            for (Country countryCollectionOldCountry : countryCollectionOld) {
                if (!countryCollectionNew.contains(countryCollectionOldCountry)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Country " + countryCollectionOldCountry + " since its autorId field is not nullable.");
                }
            }
            for (People peopleCollectionOldPeople : peopleCollectionOld) {
                if (!peopleCollectionNew.contains(peopleCollectionOldPeople)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain People " + peopleCollectionOldPeople + " since its autorId field is not nullable.");
                }
            }
            for (Genre genreCollectionOldGenre : genreCollectionOld) {
                if (!genreCollectionNew.contains(genreCollectionOldGenre)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Genre " + genreCollectionOldGenre + " since its autorId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Film> attachedFilmCollectionNew = new ArrayList<Film>();
            for (Film filmCollectionNewFilmToAttach : filmCollectionNew) {
                filmCollectionNewFilmToAttach = em.getReference(filmCollectionNewFilmToAttach.getClass(), filmCollectionNewFilmToAttach.getFilmId());
                attachedFilmCollectionNew.add(filmCollectionNewFilmToAttach);
            }
            filmCollectionNew = attachedFilmCollectionNew;
            user.setFilmCollection(filmCollectionNew);
            Collection<Comment> attachedCommentCollectionNew = new ArrayList<Comment>();
            for (Comment commentCollectionNewCommentToAttach : commentCollectionNew) {
                commentCollectionNewCommentToAttach = em.getReference(commentCollectionNewCommentToAttach.getClass(), commentCollectionNewCommentToAttach.getKomentarId());
                attachedCommentCollectionNew.add(commentCollectionNewCommentToAttach);
            }
            commentCollectionNew = attachedCommentCollectionNew;
            user.setCommentCollection(commentCollectionNew);
            Collection<Country> attachedCountryCollectionNew = new ArrayList<Country>();
            for (Country countryCollectionNewCountryToAttach : countryCollectionNew) {
                countryCollectionNewCountryToAttach = em.getReference(countryCollectionNewCountryToAttach.getClass(), countryCollectionNewCountryToAttach.getKrajinaId());
                attachedCountryCollectionNew.add(countryCollectionNewCountryToAttach);
            }
            countryCollectionNew = attachedCountryCollectionNew;
            user.setCountryCollection(countryCollectionNew);
            Collection<People> attachedPeopleCollectionNew = new ArrayList<People>();
            for (People peopleCollectionNewPeopleToAttach : peopleCollectionNew) {
                peopleCollectionNewPeopleToAttach = em.getReference(peopleCollectionNewPeopleToAttach.getClass(), peopleCollectionNewPeopleToAttach.getOsobnostId());
                attachedPeopleCollectionNew.add(peopleCollectionNewPeopleToAttach);
            }
            peopleCollectionNew = attachedPeopleCollectionNew;
            user.setPeopleCollection(peopleCollectionNew);
            Collection<Genre> attachedGenreCollectionNew = new ArrayList<Genre>();
            for (Genre genreCollectionNewGenreToAttach : genreCollectionNew) {
                genreCollectionNewGenreToAttach = em.getReference(genreCollectionNewGenreToAttach.getClass(), genreCollectionNewGenreToAttach.getZanerId());
                attachedGenreCollectionNew.add(genreCollectionNewGenreToAttach);
            }
            genreCollectionNew = attachedGenreCollectionNew;
            user.setGenreCollection(genreCollectionNew);
            user = em.merge(user);
            for (Film filmCollectionNewFilm : filmCollectionNew) {
                if (!filmCollectionOld.contains(filmCollectionNewFilm)) {
                    User oldAutorIdOfFilmCollectionNewFilm = filmCollectionNewFilm.getAutorId();
                    filmCollectionNewFilm.setAutorId(user);
                    filmCollectionNewFilm = em.merge(filmCollectionNewFilm);
                    if (oldAutorIdOfFilmCollectionNewFilm != null && !oldAutorIdOfFilmCollectionNewFilm.equals(user)) {
                        oldAutorIdOfFilmCollectionNewFilm.getFilmCollection().remove(filmCollectionNewFilm);
                        oldAutorIdOfFilmCollectionNewFilm = em.merge(oldAutorIdOfFilmCollectionNewFilm);
                    }
                }
            }
            for (Comment commentCollectionNewComment : commentCollectionNew) {
                if (!commentCollectionOld.contains(commentCollectionNewComment)) {
                    User oldAutorIdOfCommentCollectionNewComment = commentCollectionNewComment.getAutorId();
                    commentCollectionNewComment.setAutorId(user);
                    commentCollectionNewComment = em.merge(commentCollectionNewComment);
                    if (oldAutorIdOfCommentCollectionNewComment != null && !oldAutorIdOfCommentCollectionNewComment.equals(user)) {
                        oldAutorIdOfCommentCollectionNewComment.getCommentCollection().remove(commentCollectionNewComment);
                        oldAutorIdOfCommentCollectionNewComment = em.merge(oldAutorIdOfCommentCollectionNewComment);
                    }
                }
            }
            for (Country countryCollectionNewCountry : countryCollectionNew) {
                if (!countryCollectionOld.contains(countryCollectionNewCountry)) {
                    User oldAutorIdOfCountryCollectionNewCountry = countryCollectionNewCountry.getAutorId();
                    countryCollectionNewCountry.setAutorId(user);
                    countryCollectionNewCountry = em.merge(countryCollectionNewCountry);
                    if (oldAutorIdOfCountryCollectionNewCountry != null && !oldAutorIdOfCountryCollectionNewCountry.equals(user)) {
                        oldAutorIdOfCountryCollectionNewCountry.getCountryCollection().remove(countryCollectionNewCountry);
                        oldAutorIdOfCountryCollectionNewCountry = em.merge(oldAutorIdOfCountryCollectionNewCountry);
                    }
                }
            }
            for (People peopleCollectionNewPeople : peopleCollectionNew) {
                if (!peopleCollectionOld.contains(peopleCollectionNewPeople)) {
                    User oldAutorIdOfPeopleCollectionNewPeople = peopleCollectionNewPeople.getAutorId();
                    peopleCollectionNewPeople.setAutorId(user);
                    peopleCollectionNewPeople = em.merge(peopleCollectionNewPeople);
                    if (oldAutorIdOfPeopleCollectionNewPeople != null && !oldAutorIdOfPeopleCollectionNewPeople.equals(user)) {
                        oldAutorIdOfPeopleCollectionNewPeople.getPeopleCollection().remove(peopleCollectionNewPeople);
                        oldAutorIdOfPeopleCollectionNewPeople = em.merge(oldAutorIdOfPeopleCollectionNewPeople);
                    }
                }
            }
            for (Genre genreCollectionNewGenre : genreCollectionNew) {
                if (!genreCollectionOld.contains(genreCollectionNewGenre)) {
                    User oldAutorIdOfGenreCollectionNewGenre = genreCollectionNewGenre.getAutorId();
                    genreCollectionNewGenre.setAutorId(user);
                    genreCollectionNewGenre = em.merge(genreCollectionNewGenre);
                    if (oldAutorIdOfGenreCollectionNewGenre != null && !oldAutorIdOfGenreCollectionNewGenre.equals(user)) {
                        oldAutorIdOfGenreCollectionNewGenre.getGenreCollection().remove(genreCollectionNewGenre);
                        oldAutorIdOfGenreCollectionNewGenre = em.merge(oldAutorIdOfGenreCollectionNewGenre);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getPouzivatelId();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
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
            em = getEntityManager();
            em.getTransaction().begin();
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getPouzivatelId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Film> filmCollectionOrphanCheck = user.getFilmCollection();
            for (Film filmCollectionOrphanCheckFilm : filmCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Film " + filmCollectionOrphanCheckFilm + " in its filmCollection field has a non-nullable autorId field.");
            }
            Collection<Comment> commentCollectionOrphanCheck = user.getCommentCollection();
            for (Comment commentCollectionOrphanCheckComment : commentCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Comment " + commentCollectionOrphanCheckComment + " in its commentCollection field has a non-nullable autorId field.");
            }
            Collection<Country> countryCollectionOrphanCheck = user.getCountryCollection();
            for (Country countryCollectionOrphanCheckCountry : countryCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Country " + countryCollectionOrphanCheckCountry + " in its countryCollection field has a non-nullable autorId field.");
            }
            Collection<People> peopleCollectionOrphanCheck = user.getPeopleCollection();
            for (People peopleCollectionOrphanCheckPeople : peopleCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the People " + peopleCollectionOrphanCheckPeople + " in its peopleCollection field has a non-nullable autorId field.");
            }
            Collection<Genre> genreCollectionOrphanCheck = user.getGenreCollection();
            for (Genre genreCollectionOrphanCheckGenre : genreCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Genre " + genreCollectionOrphanCheckGenre + " in its genreCollection field has a non-nullable autorId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(user);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }
    
    public User findByFbId(long facebookId) {
        EntityManager em = getEntityManager();
        List<User> results= em.createNamedQuery("User.findByFbId").setParameter("fbId", facebookId).getResultList();
        if (!results.isEmpty()) {
            return results.get(0);
        }
        else {
            return null;
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
