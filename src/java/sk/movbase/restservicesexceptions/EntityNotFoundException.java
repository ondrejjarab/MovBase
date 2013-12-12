/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.restservicesexceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;


/**
 *
 * @author Ondrej
 */
public class EntityNotFoundException extends WebApplicationException{
    
    public EntityNotFoundException(String entityName, String id) {
        super(Response.status(Response.Status.NOT_FOUND).
                entity(entityName + " with id '" + id + "' cannot be found").type("text/plain").build());
        
    }
}
