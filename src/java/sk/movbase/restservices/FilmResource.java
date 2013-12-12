/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.restservices;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Film getMovie(HttpServletResponse response, @PathParam("id") String id) {
        Film movie = null;
        FilmJpaController fJpa = new FilmJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        movie = fJpa.findFilm(Integer.parseInt(id));
        if (movie == null) {
            System.out.println("Polozka neexistuje");
            try {
                response.sendRedirect("/");
            } catch (IOException ex) {
                Logger.getLogger(FilmResource.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }
        return movie;  
    }
    
}
