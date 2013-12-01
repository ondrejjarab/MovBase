<%-- 
    Document   : show
    Created on : 17.11.2013, 10:49:28
    Author     : Ondrej
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	<title>Movied.com - ${movie.nazov}</title>
  </head>
    <body>
		<div id="holder">
			<p class="center"><img src="/resources/img/logo.png" alt="logo" /></p>
			<div class="left_col">
				<a href="${movie.getPhotoURL(bigPhoto)}" class="lbox"><img src="${movie.getPhotoURL(smallPhoto)}" class="movie_photo" alt="${movie.nazov}" /></a>
			</div>
			<div class="right_col">
				<h2>${movie.nazov} (${movie.rokVydania})</h2>
				<p class="subtitle">${movie.getRating()}, ${movie.minutaz} min</p>
				<p class="description">
					${movie.getDescription()}
				</p>
				<c:if test="${!empty movie.getDirector()}">
					<p>Režisér: <a href="${movie.getDirector().getProfileURL()}">${movie.getDirector().getMeno()}</a></p>
				</c:if>
				<c:if test="${!empty movie.getActors(5)}">
					<c:forEach var="actor" items="${movie.getActors(5)}" varStatus="loopCounter" >
						<c:out value="count: ${loopCounter.count}"/>
						<c:out value="${window}"/>
					</c:forEach>
				</c:if>
			</div>
		</div>
    </body>
</html>
