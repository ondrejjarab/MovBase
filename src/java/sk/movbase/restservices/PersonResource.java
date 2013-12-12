/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.restservices;

import java.util.List;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.stereotype.Component;
import sk.movbase.jpaControllers.PeopleJpaController;
import sk.movbase.models.People;
import sk.movbase.restservicesexceptions.EntityNotFoundException;

/**
 *
 * @author Ondrej
 */
@Component
@Path("/people")
public class PersonResource {
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public People getPerson(@PathParam("id") String id) {
        People person = null;
        int personId;
        try {
            personId = Integer.parseInt(id);    //ak zadane cislo nieje id tiez vypise ze resource neexistije
        } catch(NumberFormatException ex) {
            throw new EntityNotFoundException("Person", id);
        }
        PeopleJpaController pJpa = new PeopleJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        person = pJpa.findPeople(personId);
            if (person == null) {
                throw new EntityNotFoundException("Person", id);
            }
        return person;
    }
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_XML)
    public List<People> getAll() {
        List<People> list = null;
        PeopleJpaController pJpa = new PeopleJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        list = pJpa.findPeopleEntities();
        return list;
    }
    
}
