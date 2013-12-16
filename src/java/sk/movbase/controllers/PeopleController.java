/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.controllers;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import sk.movbase.constants.PhotoSize;
import sk.movbase.jpaControllers.CountryJpaController;
import sk.movbase.jpaControllers.PeopleJpaController;
import sk.movbase.jpaControllers.ProfessionJpaController;
import sk.movbase.jpaControllers.UserJpaController;
import sk.movbase.jpaControllers.exceptions.RollbackFailureException;
import sk.movbase.models.Country;
import sk.movbase.models.People;
import sk.movbase.models.Profession;
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
    public String show(@PathVariable int id, ModelMap model, HttpServletRequest request) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MovBasePU");
        PeopleJpaController pJpa = new PeopleJpaController(emf);
        People person = pJpa.findPeople(id);
        UserJpaController uJpa = new UserJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        User user = null;
        if (request.getSession().getAttribute("userId") != null) {
        user = uJpa.findUser((Integer) request.getSession().getAttribute("userId"));
        }
        model.addAttribute("user", user);
        model.addAttribute("actor", person);
        model.addAttribute("tinyPhoto", PhotoSize.TINY);
		model.addAttribute("smallPhoto", PhotoSize.SMALL);
		model.addAttribute("bigPhoto", PhotoSize.BIG);
		model.addAttribute("descriptionCharacters", 200);
        return "people/show";
    }
   

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public ModelAndView newPerson(ModelMap model) {
        CountryJpaController cJpa = new CountryJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        List<Country> list = cJpa.findCountryEntities();
        ProfessionJpaController pJpa = new ProfessionJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        List<Profession> listP = pJpa.findProfessionEntities();
        model.addAttribute("nations", list);
        model.addAttribute("types", listP);
        model.addAttribute("person", new People());
     
        return new ModelAndView("people/new", model);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView createPerson(ModelMap model, 
           HttpServletRequest request, HttpServletResponse response, 
           @RequestParam(value = "narodnost") Short krajinaId, @RequestParam(value = "typ") Short profesiaId, 
           @RequestParam(value = "meno") String meno, @RequestParam(value = "popis") String popis,
           @RequestParam(value = "fotografia") MultipartFile fotografia) {
        
       
       if (request.getSession().getAttribute("userId") == null) {
               return new ModelAndView("index");//ak nieje prihlaseny presmeruje ho  
       }
        User autorZaznamu = null;
        UserJpaController uJpa = new UserJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        autorZaznamu = uJpa.findUser(((int) request.getSession().getAttribute("userId")));
       
       People person = new People();
       person.setAutorId(autorZaznamu);
       person.setDatumPridania(new Date());
       person.setSchvaleny("1");
       person.setMeno(meno);
       person.setPopis(popis);
     
       
       if (!isValid(person)) {                        //overi povinne atributy
           return new ModelAndView("index");
       }
       
       CountryJpaController cJpa = new CountryJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
       Country country = cJpa.findCountry(krajinaId);
       person.setNarodnost(country);
       
        ProfessionJpaController pJpa = new ProfessionJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
       Profession profession = pJpa.findProfession(profesiaId);
       person.setTyp(profession);
       
       PeopleJpaController peopJpa = new PeopleJpaController(Persistence.createEntityManagerFactory("MovBasePU"));
        try {
            peopJpa.create(person);
        } catch (Exception ex) {
            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //test na fotografia == null;
        int photoId = person.getOsobnostId();
        person.setFotografia(photoId + "_small.jpg");
        try {
            peopJpa.edit(person);
            
        } catch (RollbackFailureException ex) {
            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.savePhotos(fotografia, photoId);
        try {
            response.sendRedirect("/");
        } catch (IOException ex) {
            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    return new ModelAndView("index");
   }
    
    @RequestMapping("/edit")
    public String edit(@PathVariable int id) {
        return "people/edit";
    }
   
    
    @RequestMapping(method = RequestMethod.PUT)
    public String update() {
        return null;
    }
    
    
    private boolean isValid(People person) {
       return !(person.getMeno().length() == 0 || person.getPopis().length() == 0);//treba porovnavat na dlzku nie na null
           
   }
    
    private BufferedImage createResizedCopy(Image originalImage, 
                int scaledWidth, int scaledHeight, 
                boolean preserveAlpha)
    {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
                g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
        g.dispose();
        return scaledBI;
    }


  private void savePhotos(MultipartFile file, int photoId) {
      File fileT = null;
      File fileS = null;
      File fileB = null;
      
        try {
        fileT = new File(PhotoSize.PHOTO_RES_PATH + "\\person_photos\\" + photoId + "\\" + photoId + "_tiny.jpg");
        fileT.mkdirs();
        fileT.createNewFile();
        fileS = new File(PhotoSize.PHOTO_RES_PATH + "\\person_photos\\" + photoId + "\\" + photoId + "_small.jpg");
        fileS.createNewFile();
        fileB = new File(PhotoSize.PHOTO_RES_PATH + "\\person_photos\\" + photoId  + "\\" + photoId + "_big.jpg");
        fileB.createNewFile();
        System.out.println(fileT.getAbsoluteFile());
        } catch(IOException ex) {
            ex.printStackTrace();
        }

        
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            BufferedImage tiny = this.createResizedCopy(image, 50, 67, false);
            BufferedImage small = this.createResizedCopy(image, 230, 290, false);
            BufferedImage bigg = this.createResizedCopy(image, 600, 800, false);

          
            ImageIO.write(tiny, "jpg", fileT);
            ImageIO.write(small, "jpg", fileS);
            ImageIO.write(bigg, "jpg", fileB);

            
        } catch (IOException ex) {
            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
    
    
}
