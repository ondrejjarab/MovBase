/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.jpaControllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import sk.movbase.jpaControllers.exceptions.NonexistentEntityException;
import sk.movbase.jpaControllers.exceptions.RollbackFailureException;
import sk.movbase.models.Comment;
import sk.movbase.models.Film;
import sk.movbase.models.User;

/**
 *
 * @author Ondrej
 */
public class CommentJpaController implements Serializable {

    public CommentJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Comment comment) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Film filmId = comment.getFilmId();
            if (filmId != null) {
                filmId = em.getReference(filmId.getClass(), filmId.getFilmId());
                comment.setFilmId(filmId);
            }
            User autorId = comment.getAutorId();
            if (autorId != null) {
                autorId = em.getReference(autorId.getClass(), autorId.getPouzivatelId());
                comment.setAutorId(autorId);
            }
            em.persist(comment);
            if (filmId != null) {
                filmId.getCommentCollection().add(comment);
                filmId = em.merge(filmId);
            }
            if (autorId != null) {
                autorId.getCommentCollection().add(comment);
                autorId = em.merge(autorId);
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

    public void edit(Comment comment) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Comment persistentComment = em.find(Comment.class, comment.getKomentarId());
            Film filmIdOld = persistentComment.getFilmId();
            Film filmIdNew = comment.getFilmId();
            User autorIdOld = persistentComment.getAutorId();
            User autorIdNew = comment.getAutorId();
            if (filmIdNew != null) {
                filmIdNew = em.getReference(filmIdNew.getClass(), filmIdNew.getFilmId());
                comment.setFilmId(filmIdNew);
            }
            if (autorIdNew != null) {
                autorIdNew = em.getReference(autorIdNew.getClass(), autorIdNew.getPouzivatelId());
                comment.setAutorId(autorIdNew);
            }
            comment = em.merge(comment);
            if (filmIdOld != null && !filmIdOld.equals(filmIdNew)) {
                filmIdOld.getCommentCollection().remove(comment);
                filmIdOld = em.merge(filmIdOld);
            }
            if (filmIdNew != null && !filmIdNew.equals(filmIdOld)) {
                filmIdNew.getCommentCollection().add(comment);
                filmIdNew = em.merge(filmIdNew);
            }
            if (autorIdOld != null && !autorIdOld.equals(autorIdNew)) {
                autorIdOld.getCommentCollection().remove(comment);
                autorIdOld = em.merge(autorIdOld);
            }
            if (autorIdNew != null && !autorIdNew.equals(autorIdOld)) {
                autorIdNew.getCommentCollection().add(comment);
                autorIdNew = em.merge(autorIdNew);
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
                Integer id = comment.getKomentarId();
                if (findComment(id) == null) {
                    throw new NonexistentEntityException("The comment with id " + id + " no longer exists.");
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
            utx.begin();
            em = getEntityManager();
            Comment comment;
            try {
                comment = em.getReference(Comment.class, id);
                comment.getKomentarId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comment with id " + id + " no longer exists.", enfe);
            }
            Film filmId = comment.getFilmId();
            if (filmId != null) {
                filmId.getCommentCollection().remove(comment);
                filmId = em.merge(filmId);
            }
            User autorId = comment.getAutorId();
            if (autorId != null) {
                autorId.getCommentCollection().remove(comment);
                autorId = em.merge(autorId);
            }
            em.remove(comment);
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

    public List<Comment> findCommentEntities() {
        return findCommentEntities(true, -1, -1);
    }

    public List<Comment> findCommentEntities(int maxResults, int firstResult) {
        return findCommentEntities(false, maxResults, firstResult);
    }

    private List<Comment> findCommentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comment.class));
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

    public Comment findComment(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comment.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Comment> rt = cq.from(Comment.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
