<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="sk.movbase.jpaControllers.CommentJpaController"%>
<%@page import="sk.movbase.models.Comment"%>
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
            ahoj ${films}             </p>

            <c:forEach var="i" items="${films}">
            <p>${i.meno}</p>
                
            </c:forEach>
        
    </body>
</html>
