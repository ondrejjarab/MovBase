/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.movbase.restservices;

import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author Ondrej
 */
public class Config extends ResourceConfig {
    public Config() {
        packages("sk.movbase.restservices");
    }
}
