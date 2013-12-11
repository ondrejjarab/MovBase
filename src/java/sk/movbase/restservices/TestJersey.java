/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.restservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType; 
import org.springframework.stereotype.Component;


/**
 *
 * @author Ondrej
 */

@Component
@Path("/service")
public class TestJersey {
    
    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String getText() {
        System.out.println("test");
        return "Toto je Jersey test.";
    }
    
    @GET
    @Produces(MediaType.TEXT_XML)
    public String getXmlText() {
        return "Toto je Jersey XML test";
    }
    
    
    
}
