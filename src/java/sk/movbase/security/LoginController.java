/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.movbase.security;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sk.movbase.jpaControllers.UserJpaController;
import sk.movbase.models.User;

/**
 *
 * @author Ondrej
 */
@RequestMapping(value = "/")
@Controller
public class LoginController {

    private static final String SCOPE = "email,offline_access,user_about_me";
    private static final String REDIRECT_URI = "http://localhost:8080/";
    private static final String CLIENT_ID = "227583140736531";
    private static final String APP_SECRET = "...";
    private static final String DIALOG_OAUTH = "https://www.facebook.com/dialog/oauth";
    private static final String ACCESS_TOKEN = "https://graph.facebook.com/oauth/access_token";
    private static final String USER_INFO_FB = "https://graph.facebook.com/me?access_token=";

    //vyziada od facebooku access code, ktory je potom potrebny pri ziadany access tokenu
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public void requestAccessCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            response.sendRedirect(DIALOG_OAUTH + "?client_id=" + CLIENT_ID
                    + "&redirect_uri=" + REDIRECT_URI + "&scope=" + SCOPE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(value = "", params = "code", method = RequestMethod.GET)
    @ResponseBody
    public void requestToken(@RequestParam("code") String code,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String responseString = buildResponseString(code);
        String accessToken = getAccessToken(responseString);

        JSONObject userFbData = getUserData(accessToken);
        User user = authenticate(userFbData);                                   //ziska udaje o aktualnom pouzivatelovi
        request.getSession(false).setAttribute("userId", user.getPouzivatelId());   //a nastavi session
        request.getSession(false).setAttribute("accessToken", accessToken);

        try {

            response.sendRedirect(REDIRECT_URI);

        } catch (IOException e) {
        }
    }

    /**
     * vytvori poziadavku pre ziskanie access tokenu
     * @param code
     * @return 
     */
    private String buildResponseString(String code) {
        StringBuilder responseString = new StringBuilder();

        responseString.append(ACCESS_TOKEN);
        responseString.append("?client_id=" + CLIENT_ID);
        responseString.append("&redirect_uri=" + REDIRECT_URI);
        responseString.append("&code=" + code);
        responseString.append("&client_secret=" + APP_SECRET);
        return responseString.toString();
    }

    
    private String getAccessToken(String responseString) {
        String accessToken = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(responseString);
            HttpResponse respone = client.execute(get);

            HttpEntity entity = respone.getEntity();
            String fullAccessToken = EntityUtils.toString(entity);

            String accessTokenWPrefix = fullAccessToken.split("&")[0];
            accessToken = accessTokenWPrefix.split("=")[1];

        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return accessToken;
    }

    /**
     * vytvori poziadavku pre ziskanie udajov o pouzivatelovi
     * @param aToken
     * @return 
     */
    private JSONObject getUserData(String aToken) {
        HttpClient client = new DefaultHttpClient();
        HttpGet hGet = new HttpGet(USER_INFO_FB + aToken);
        HttpResponse response = null;
        JSONObject userData = null;
        try {
            response = client.execute(hGet);
            HttpEntity entity;
            entity = response.getEntity();
            userData = parseData(EntityUtils.toString(entity, Charset.forName("UTF-8")));
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return userData;
    }

    private JSONObject parseData(String data) {
        JSONParser json = new JSONParser();
        JSONObject jObject = null;
        try {
            jObject = (JSONObject) json.parse(data);
        } catch (ParseException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jObject;
    }

    //vrati prihlaseneho pouzivatela, podla FB id zisti ci je v databaze, ak hej ho vrati, ak nie
    // tak podla udajov z FB vytvori noveho
    private User authenticate(JSONObject data) {
        UserJpaController uJpa = new UserJpaController(Persistence.createEntityManagerFactory("MovBasePU"));

        long fbId = Long.parseLong((String) data.remove("id"));
        User user = uJpa.findByFbId(fbId);
        if (user != null) {
            return user;
        } else {
            user = new User(fbId, (String) data.remove("name"), (String) data.remove("email"), new Date(), "u");
            try {
                uJpa.create(user);
            } catch (Exception ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return user;
        }

    }
    
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logOut(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("userId", null);
        request.getSession().setAttribute("accessToken", null);
        try {
            response.sendRedirect("/");
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
