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
				<div id="rating_box_graphic">
                    <div id="rating_box_graphic_background">&nbsp;</div>
                    <div id="rating_box_graphic_foreground" style="width:${movie.getGraphicRating()}px;">&nbsp;</div>
                </div>
			</div>
			<div class="right_col">
				<h2>${movie.nazov} (${movie.rokVydania})</h2>
				<p class="subtitle">${movie.getVerbalRating()}, ${movie.minutaz} min</p>
				<p class="description">
					${movie.getDescription(0,descriptionCharacters)}
					<c:if test="${movie.getPopis().length()>descriptionCharacters}">
						<span>... <a href="#" class="show_more">zobraziť všetko</a></span>
						<span class="invisible show_more_display">${movie.getDescription(descriptionCharacters)}</span>
					</c:if>
				</p>
				<c:if test="${!empty movie.getDirector()}">
					<p class="director">Režisér: <a href="${movie.getDirector().getProfileURL()}">${movie.getDirector().getMeno()}</a></p>
				</c:if>
				<c:if test="${!empty movie.getActors(5)}">
				<div class="actors_and_stuff"><h3>Spolupracujú a hrajú:</h3>
					<c:forEach var="actor" items="${movie.getActors(5)}" varStatus="loopCounter" >
						<div class="actor_microlayout">
							<a href="${actor.getPhotoURL(bigPhoto)}" class="lbox"><img src="${actor.getPhotoURL(tinyPhoto)}" alt="${actor.getMeno()}" /></a><br/>
							<a href="${actor.getProfileURL()}">${actor.getMeno()}</a><br/>
							${actor.getTyp().getNazov()}
						</div>
					</c:forEach>
				</div>
				</c:if>
			</div>
			<div class="comments">
				<h2>Komentáre:</h2>
				<c:choose>
					<c:when test="${empty movie.getCommentCollection()}">
						<p class="center">Zatiaľ neboli pridané žiadne komentáre.</p>
					</c:when>
					<c:otherwise>
						<c:forEach var="comment" items="${movie.getCommentCollection()}" varStatus="loopCounter" >
						
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
    </body>
</html>
