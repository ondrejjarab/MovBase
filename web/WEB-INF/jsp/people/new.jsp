<%-- 
    Document   : new
    Created on : 15.12.2013, 14:42:57
    Author     : Ondrej
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <meta name="description" content="">
        <meta name="keywords" content="">
        <meta name="robots" content="index,follow">
        <link rel="stylesheet" type="text/css" href="/resources/style.css">
        <link rel="shortcut icon" href="/resources/img/favicon.gif">
        <script type="text/javascript" src="/resources/js/jquery.js"></script>
        <script type="text/javascript" src="/resources/js/jquery.lightbox-0.5.min.js"></script>
        <script type="text/javascript" src="/resources/js/scripts.js"></script>
        <title>Movied.com</title>
    </head>
    <body>
        <div id="holder">
            <p class="center"><img src="/resources/img/logo.png" alt="logo" /></p>
            <h2>Nová osobnosť</h2>
            <div class="right_col">
                <form:form method="POST" action="/people" modelAttribute="person" enctype="multipart/form-data">
                    <table>
                        <tr>
                            <td><form:label path="meno">Meno a priezvisko</form:label></td>
                            <td><form:input path="meno" /></td>
                        </tr>
                        <tr>
                            <td><form:label path="popis">Popis</form:label></td>
                            <td><form:input path="popis" /></td>
                        </tr>
                        
                        <tr>
        <td><form:label name = "typ" path="typ">Typ</form:label></td>
        <td><form:select name = "typ" path="typ" items="${types}" itemValue="profesiaId" itemLabel="nazov"/></td>
    </tr>
    
              <tr>
        <td><form:label name="narodnost" path="narodnost">Narodnost</form:label></td>
        <td><form:select name="narodnost" path = "narodnost" items="${nations}" itemValue="krajinaId" itemLabel="nazov"/></td>
    </tr>
    
    <input type="file" name="fotografia" />
                        
                       

                        



                        <tr>
                            <td colspan="2">
                                <input type="submit" value="Submit"/>
                            </td>
                        </tr>
                    </table>  
                </form:form>
            </div>

        </div>
    </body>
</html>
