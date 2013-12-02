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
import sk.movbase.constants.PhotoSize;
import sk.movbase.jpaControllers.PeopleJpaController;
import sk.movbase.jpaControllers.UserJpaController;
import sk.movbase.models.People;
import sk.movbase.models.User;

/**
 *
 * @author Ondrej
 */
@Controller
@RequestMapping("/people")
public class PeopleController {
    
    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap model) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MovBasePU");
        UserJpaController uJpa = new UserJpaController(emf);
        model.addAttribute("people", uJpa.findUserEntities());
        return "people/index";
    }
    
    @RequestMapping("{id}")
    public String show(@PathVariable int id, ModelMap model) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MovBasePU");
        PeopleJpaController pJpa = new PeopleJpaController(emf);
        People person = pJpa.findPeople(id);
        model.addAttribute("actor", person);
        model.addAttribute("tinyPhoto", PhotoSize.TINY);
		model.addAttribute("smallPhoto", PhotoSize.SMALL);
		model.addAttribute("bigPhoto", PhotoSize.BIG);
		model.addAttribute("descriptionCharacters", 200);
        return "people/show";
    }

    @RequestMapping("/new")
    public String newP() {
        return "people/new";
    }
    
    @RequestMapping("/edit")
    public String edit(@PathVariable int id) {
        return "people/edit";
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public String create() {
        return null;
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String update() {
        return null;
    }
}
