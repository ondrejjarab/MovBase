/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.restservices;

import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.stereotype.Component;
import sk.movbase.jpaControllers.FilmJpaController;
import sk.movbase.models.Film;

/**
 *
 * @author Ondrej
 */
@Component
@Path("/movies")
public class FilmResource {
    
    
    @GET
    @Path("/movie")
    @Produces(MediaType.APPLICATION_XML)
    public Film getMovie() {
        Film movie = null;
        FilmJpaController fJpa = new FilmJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        movie = fJpa.findFilm(1);
        return movie;  
    }
    
}
