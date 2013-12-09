/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.controllers;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sk.movbase.constants.PhotoSize;
import sk.movbase.jpaControllers.FilmJpaController;
import sk.movbase.jpaControllers.UserJpaController;
import sk.movbase.models.Film;
import sk.movbase.models.User;

/**
 *
 * @author Ondrej
 */
@Controller
@RequestMapping("/")
public class IndexController {
    
   
    @RequestMapping(method=RequestMethod.GET)
    public String index(ModelMap model, HttpServletRequest request) {
        FilmJpaController fJpa = new FilmJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        model.addAttribute("movies", fJpa.findFilmEntities());
        model.addAttribute("smallPhoto", PhotoSize.SMALL);
        ///////////////////////test 
        UserJpaController uJpa = new UserJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        User user = null;
        if (request.getSession().getAttribute("userId") != null) {
        user = uJpa.findUser((Integer) request.getSession().getAttribute("userId"));
        }
        model.addAttribute("user", user);
        ////////////////////
        return "index";
    }
}
