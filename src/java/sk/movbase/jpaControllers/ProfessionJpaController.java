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
import sk.movbase.models.People;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import sk.movbase.jpaControllers.exceptions.IllegalOrphanException;
import sk.movbase.jpaControllers.exceptions.NonexistentEntityException;
import sk.movbase.jpaControllers.exceptions.RollbackFailureException;
import sk.movbase.models.Profession;

/**
 *
 * @author Ondrej
 */
public class ProfessionJpaController implements Serializable {

    public ProfessionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Profession profession) throws RollbackFailureException, Exception {
        if (profession.getPeopleCollection() == null) {
            profession.setPeopleCollection(new ArrayList<People>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<People> attachedPeopleCollection = new ArrayList<People>();
            for (People peopleCollectionPeopleToAttach : profession.getPeopleCollection()) {
                peopleCollectionPeopleToAttach = em.getReference(peopleCollectionPeopleToAttach.getClass(), peopleCollectionPeopleToAttach.getOsobnostId());
                attachedPeopleCollection.add(peopleCollectionPeopleToAttach);
            }
            profession.setPeopleCollection(attachedPeopleCollection);
            em.persist(profession);
            for (People peopleCollectionPeople : profession.getPeopleCollection()) {
                Profession oldTypOfPeopleCollectionPeople = peopleCollectionPeople.getTyp();
                peopleCollectionPeople.setTyp(profession);
                peopleCollectionPeople = em.merge(peopleCollectionPeople);
                if (oldTypOfPeopleCollectionPeople != null) {
                    oldTypOfPeopleCollectionPeople.getPeopleCollection().remove(peopleCollectionPeople);
                    oldTypOfPeopleCollectionPeople = em.merge(oldTypOfPeopleCollectionPeople);
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

    public void edit(Profession profession) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profession persistentProfession = em.find(Profession.class, profession.getProfesiaId());
            Collection<People> peopleCollectionOld = persistentProfession.getPeopleCollection();
            Collection<People> peopleCollectionNew = profession.getPeopleCollection();
            List<String> illegalOrphanMessages = null;
            for (People peopleCollectionOldPeople : peopleCollectionOld) {
                if (!peopleCollectionNew.contains(peopleCollectionOldPeople)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain People " + peopleCollectionOldPeople + " since its typ field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<People> attachedPeopleCollectionNew = new ArrayList<People>();
            for (People peopleCollectionNewPeopleToAttach : peopleCollectionNew) {
                peopleCollectionNewPeopleToAttach = em.getReference(peopleCollectionNewPeopleToAttach.getClass(), peopleCollectionNewPeopleToAttach.getOsobnostId());
                attachedPeopleCollectionNew.add(peopleCollectionNewPeopleToAttach);
            }
            peopleCollectionNew = attachedPeopleCollectionNew;
            profession.setPeopleCollection(peopleCollectionNew);
            profession = em.merge(profession);
            for (People peopleCollectionNewPeople : peopleCollectionNew) {
                if (!peopleCollectionOld.contains(peopleCollectionNewPeople)) {
                    Profession oldTypOfPeopleCollectionNewPeople = peopleCollectionNewPeople.getTyp();
                    peopleCollectionNewPeople.setTyp(profession);
                    peopleCollectionNewPeople = em.merge(peopleCollectionNewPeople);
                    if (oldTypOfPeopleCollectionNewPeople != null && !oldTypOfPeopleCollectionNewPeople.equals(profession)) {
                        oldTypOfPeopleCollectionNewPeople.getPeopleCollection().remove(peopleCollectionNewPeople);
                        oldTypOfPeopleCollectionNewPeople = em.merge(oldTypOfPeopleCollectionNewPeople);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = profession.getProfesiaId();
                if (findProfession(id) == null) {
                    throw new NonexistentEntityException("The profession with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profession profession;
            try {
                profession = em.getReference(Profession.class, id);
                profession.getProfesiaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profession with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<People> peopleCollectionOrphanCheck = profession.getPeopleCollection();
            for (People peopleCollectionOrphanCheckPeople : peopleCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Profession (" + profession + ") cannot be destroyed since the People " + peopleCollectionOrphanCheckPeople + " in its peopleCollection field has a non-nullable typ field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(profession);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Profession> findProfessionEntities() {
        return findProfessionEntities(true, -1, -1);
    }

    public List<Profession> findProfessionEntities(int maxResults, int firstResult) {
        return findProfessionEntities(false, maxResults, firstResult);
    }

    private List<Profession> findProfessionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profession.class));
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

    public Profession findProfession(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profession.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfessionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profession> rt = cq.from(Profession.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
