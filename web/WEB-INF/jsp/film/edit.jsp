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
                        <h1>${movie.getNazov()} / <h2>zmena údajov</h2></h1>
                        <div class="right_col">
                            
                            <form:form method="POST" action="/movies" modelAttribute="movie">
   <table>
    <tr>
        <td><form:label path="nazov" >Názov</form:label></td>
        <td><form:input path="nazov" size="1000" maxlength="1000"/></td>
    </tr>
    <tr>
        <td><form:label path="popis">Popis</form:label></td>
        <td><form:input path="popis" /></td>
    </tr>
    <tr>
        <td><form:label path="originalnyNazov">Originálny názov</form:label></td>
        <td><form:input path="originalnyNazov" /></td>
    </tr>
    <tr>
        <td><form:label path="rokVydania">Rok vydania</form:label></td>
        <td><form:input path="rokVydania" /></td>
    </tr>
    <tr>
        <td><form:label path="minutaz">Minutaz</form:label></td>
        <td><form:input path="minutaz" /></td>
    </tr>
    <tr>
        <td><form:label path="pocetCasti">Pocet častí</form:label></td>
        <td><form:input path="pocetCasti" /></td>
    </tr>
    <tr>
        <td><form:label path="typ">Typ</form:label></td>
        <td><form:select path="typ" items="${types}"/></td>
    </tr>
    
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
