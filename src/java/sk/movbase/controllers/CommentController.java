/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import sk.movbase.jpaControllers.CommentJpaController;
import sk.movbase.jpaControllers.FilmJpaController;
import sk.movbase.jpaControllers.UserJpaController;
import sk.movbase.jpaControllers.exceptions.NonexistentEntityException;
import sk.movbase.jpaControllers.exceptions.RollbackFailureException;
import sk.movbase.models.Comment;
import sk.movbase.models.Film;
import sk.movbase.models.User;

/**
 *
 * @author Ondrej
 */
@Controller
@RequestMapping("/movie/{id}/comments")          //komentare pre film/serial s id ...
public class CommentController {
    
    //vsetky komentare ku filmu
    @RequestMapping(method = RequestMethod.GET)
    public String allComments(@PathVariable int movieId, ModelMap model) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MovBasePU");
        FilmJpaController fJpa = new FilmJpaController(emf);
        Collection<Comment> comments = fJpa.findFilm(movieId).getCommentCollection();
        model.addAttribute("comments", comments);
        return "comment/index";
    }
    
    
    //nic nerobi len zobrazi form
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newComment() {
       
    return "comment/new";
}
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit() {
        return "comment/edit";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(@PathVariable int id, @ModelAttribute("comment")Comment comment, 
            HttpServletRequest request, HttpServletResponse response) {
           
        FilmJpaController fJpa = new FilmJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        
        Film movie = fJpa.findFilm(id);
        if (request.getSession().getAttribute("userId") == null) {
               return new ModelAndView("index");    
       }
        
        if (!isValid(comment)) {
               return new ModelAndView("film/new", "film", new Film());  //ak je prazdny komentar  
       }
        
        User autorZaznamu = null;
        UserJpaController uJpa = new UserJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        autorZaznamu = uJpa.findUser(((int) request.getSession().getAttribute("userId")));
        comment.setAutorId(autorZaznamu);//nastavi usera ktory pridal komentar
        
        comment.setDatumPridania(new Date());
        comment.setFilmId(movie);
        
        CommentJpaController cJpa = new CommentJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        try {
            cJpa.create(comment);
        } catch (Exception ex) {
            Logger.getLogger(CommentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            response.sendRedirect("/movies/" + id);
        } catch (IOException ex) {
            Logger.getLogger(CommentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ModelAndView("film/index");
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String update(@PathVariable int movieId) {
        return null;
    }
    
    
    private boolean isValid(Comment comment) {
        return comment.getKomentar().length() != 0;
    }
    
    
}
