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
import sk.movbase.jpaControllers.FilmJpaController;
import sk.movbase.models.Film;
import sk.movbase.restservicesexceptions.EntityNotFoundException;

/**
 *
 * @author Ondrej
 */
@Component
@Path("/movies")
public class FilmResource {
    
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Film getMovie(@PathParam("id") String id) {
        Film movie = null;
        int filmId;
        
        try {
            filmId = Integer.parseInt(id);
        } catch(NumberFormatException ex) {
            throw new EntityNotFoundException("Film", id);
        }
        
        FilmJpaController fJpa = new FilmJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        movie = fJpa.findFilm(Integer.parseInt(id));
        if (movie == null) {
            System.out.println("Polozka neexistuje");
            throw new EntityNotFoundException("Film", id);
        }
        return movie;  
    }
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_XML)
    public List<Film> getAll() {
        List<Film> list;
        FilmJpaController fJpa = new FilmJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        list = fJpa.findFilmEntities();
        return list;
        
    }
    
}
