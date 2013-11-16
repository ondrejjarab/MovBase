<%@page import="javax.persistence.EntityManager"%>
<%@page import="sk.movbase.models.Film"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="sk.movbase.models.User"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="sk.movbase.jpaControllers.UserJpaController"%>
<%@page import="javax.transaction.UserTransaction"%>
<%@page import="javax.persistence.Persistence"%>
<%@page import="javax.persistence.EntityManagerFactory"%>
<%@page import="javax.persistence.EntityManagerFactory"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ahoj</title>
        
    </head>

    <body>
        <p> 
            
            <%     
             EntityManagerFactory emf = Persistence.createEntityManagerFactory("MovBasePU");//to co je uvedene v persistence.xml
             UserTransaction t = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
             UserJpaController uJpa = new UserJpaController(emf);
        //     EntityManager em = emf.createEntityManager();
      //       em.getTransaction().begin();
    //         em.persist(new User(96, "abcd", "abcd@gmail.com", new Date()));
  //           em.getTransaction().commit();
             uJpa.create(new User(131, "abcd", "abcd@gmail.com", new Date()));  
             UserJpaController uJ = new UserJpaController(emf);
             List<User> u = uJ.findUserEntities(); 
                          System.out.println("ahoj");  
             
                          System.out.println(u);  

             //user.setPohlavie(null);
        %></p>
        
    </body>
</html>
