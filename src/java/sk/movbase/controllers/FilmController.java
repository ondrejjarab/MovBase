/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.controllers;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sk.movbase.jpaControllers.FilmJpaController;
import sk.movbase.models.Film;

/**
 *
 * @author Ondrej
 */
@Controller
@RequestMapping("/movies")
public class FilmController {
    
   
    @RequestMapping(method=RequestMethod.GET)
    public String index(ModelMap model) {
        FilmJpaController fJpa = new FilmJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        model.addAttribute("movies", fJpa.findFilmEntities());
        return "film/index";
    }
    
    @RequestMapping("{id}")
    public String show(@PathVariable int id, ModelMap model) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MovBasePU");
        FilmJpaController fJpa = new FilmJpaController(emf);
        Film film = fJpa.findFilm(id);
        model.addAttribute("movie", film); 
        return "film/show"; 
    }
}
