/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.controllers;

import java.util.Collection;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sk.movbase.jpaControllers.FilmJpaController;
import sk.movbase.models.Comment;

/**
 *
 * @author Ondrej
 */
@Controller
@RequestMapping("/movie/{id}/comments")          //komentare pre film/serial s id ...
public class CommentController {
    
    @RequestMapping(method = RequestMethod.GET)
    public String allComments(@PathVariable int movieId, ModelMap model) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MovBasePU");
        FilmJpaController fJpa = new FilmJpaController(emf);
        Collection<Comment> comments = fJpa.findFilm(movieId).getCommentCollection();
        model.addAttribute("comments", comments);
        return "comment/index";
    }
    
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newComment() {
       
    return "comment/new";
}
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit() {
        return "comment/edit";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String create(@PathVariable int movieId) {
        return null;
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String update(@PathVariable int movieId) {
        return null;
    }
    
    
    
}
