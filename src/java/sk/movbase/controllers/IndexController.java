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
@RequestMapping("/")
public class IndexController {
    
   
    @RequestMapping(method=RequestMethod.GET)
    public String index() {
        return "index";
    }
}
