/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sk.movbase.constants.OrderFilmTypes;
import sk.movbase.constants.PhotoSize;
import sk.movbase.jpaControllers.FilmJpaController;
import sk.movbase.jpaControllers.GenreJpaController;
import sk.movbase.jpaControllers.UserJpaController;
import sk.movbase.models.Comment;
import sk.movbase.models.Film;
import sk.movbase.models.User;

/**
 *
 * @author Ondrej
 */
@Controller
@RequestMapping("/movies")
public class FilmController {
    
    
	
    @RequestMapping(method=RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap model, 
						@RequestParam(value="page", required=false) Integer page, 
						@RequestParam(value="order", required=false) Integer order, 
						@RequestParam(value="items", required=false) Integer items_per_page,
						@RequestParam(value="genre", required=false) Integer genre,
						@RequestParam(value="search", required=false) String search) {
		if(items_per_page==null) items_per_page=10; else if(items_per_page<10) items_per_page = 10; else if(items_per_page>50) items_per_page = 50;
		if(order==null) order=0;
		if(genre==null) genre=0;
		if(search==null) search="";
        FilmJpaController fJpa = new FilmJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
		GenreJpaController gJpa = new GenreJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
		Integer lastpage = (int)(long)Math.round(Math.ceil(fJpa.getFilmCount()/items_per_page));
		if(page==null || page<1 || page>lastpage) page = 1;
		Integer from = (page-1)*items_per_page;
        model.addAttribute("movies", fJpa.findFilmEntities(false, items_per_page, from, order, genre, search));
		model.addAttribute("menu_filmy", true); 
		model.addAttribute("tinyPhoto", PhotoSize.TINY);
		model.addAttribute("actualpage", page);
		model.addAttribute("lastpage", lastpage);
		model.addAttribute("search", search);
		model.addAttribute("genre", genre);
		model.addAttribute("order", order);
		model.addAttribute("genres", gJpa.findGenreEntities());
		model.addAttribute("items", items_per_page);
		model.addAttribute("paging_url", "/movies?order="+order+"&amp;items="+items_per_page+"&amp;genre="+genre+"&amp;search="+search+"&amp;");
		UserJpaController uJpa = new UserJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        User user = null;
        if (request.getSession().getAttribute("userId") != null) {
			user = uJpa.findUser((Integer) request.getSession().getAttribute("userId"));
        }
		model.addAttribute("user", user);
		model.addAttribute("session", request.getSession());
        return "film/index";
    }
     
    @RequestMapping("{id}")
    public String show(@PathVariable int id, HttpServletRequest request, ModelMap model) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MovBasePU");
        FilmJpaController fJpa = new FilmJpaController(emf);
        Film film = fJpa.findFilm(id);
        UserJpaController uJpa = new UserJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        User user = null;
        if (request.getSession().getAttribute("userId") != null) {
        user = uJpa.findUser((Integer) request.getSession().getAttribute("userId"));
        }
        Comment comment = null;
        if (user != null)
            comment = user.hasRatedFilm(film.getFilmId());
        
        if (comment == null) {
            comment = new Comment();
        }
        
        
        model.addAttribute("user", user);
        model.addAttribute("movie", film); 
        model.addAttribute("menu_filmy", true); 
		model.addAttribute("tinyPhoto", PhotoSize.TINY);
		model.addAttribute("smallPhoto", PhotoSize.SMALL);
		model.addAttribute("bigPhoto", PhotoSize.BIG);
		model.addAttribute("descriptionCharacters", 200);
                model.addAttribute("comment", comment);
                //////////////
                model.addAttribute("session", request.getSession());
                ///////////
        return "film/show"; 
    }
    
	
	@RequestMapping(value = "/new", method = RequestMethod.GET) 
    public ModelAndView newMovie(ModelMap model) {
        List<String> list = new ArrayList<String>();
        list.add("film");
        list.add("seriál");
        model.addAttribute("film", new Film()); 
        model.addAttribute("types", list);
		model.addAttribute("menu_filmy", true); 
        return new ModelAndView("film/new", model);
    }
    
    @RequestMapping(value = "{id}/edit", method = RequestMethod.GET) 
    public ModelAndView editMovie(@PathVariable int id, ModelMap model,
            HttpServletRequest request, HttpServletResponse response) {
        User user = null;
        if (request.getSession().getAttribute("userId") != null) {
        UserJpaController uJpa = new UserJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        user = uJpa.findUser((Integer) request.getSession().getAttribute("userId"));
        } else {             //kontrola ci je prihlaseny
            try {
            response.sendRedirect("movies/" + id);
            return null;
            } catch (IOException ex) {
                Logger.getLogger(FilmController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (!user.isAdmin()) {try {
            //editovat moze len administrator
            response.sendRedirect("/movies/" + id);
            return null;
            } catch (IOException ex) {
                Logger.getLogger(FilmController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        FilmJpaController fJpa = new FilmJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        Film film = fJpa.findFilm(id);
        
        model.addAttribute("movie", film);
        model.addAttribute("user", user);
        return new ModelAndView("film/edit", model);
         
    }
    
   @RequestMapping(method = RequestMethod.POST)
   public ModelAndView addFilm(@ModelAttribute("SpringWeb")Film film, ModelMap model, 
           HttpServletRequest request, HttpServletResponse response) {
       if (request.getSession().getAttribute("userId") == null) {
           try {
               response.sendRedirect("/");//ak nieje prihlaseny presmeruje ho
               return null;
           } catch (IOException ex) {
               Logger.getLogger(FilmController.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
       
       if (!isValid(film)) {
               return new ModelAndView("film/new", "film", new Film());
            
       }
        User autorZaznamu = null;
        UserJpaController uJpa = new UserJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        autorZaznamu = uJpa.findUser(((int) request.getSession().getAttribute("userId")));
        
        film.setAutorId(autorZaznamu);//povinne udaje
        film.setDatumPridania(new Date());
        film.setSchvaleny("1");//potom zmenit podla typu prihláseného
        
        FilmJpaController fJpa = new FilmJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        try {
            fJpa.create(film);    
        } catch (Exception ex) {
            Logger.getLogger(FilmController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            response.sendRedirect("/movies");
        } catch (IOException ex) {
            Logger.getLogger(FilmController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    return null;
   }
   
   private boolean isValid(Film film) {
       return !(film.getNazov().length() == 0 || film.getPopis().length() == 0);//treba porovnavat na dlzku nie na null
           
   }

    
    
    
}
