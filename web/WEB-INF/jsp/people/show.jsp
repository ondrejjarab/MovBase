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
	<title>Movied.com - ${actor.getMeno()}</title>
  </head>
    <body>
		<div id="holder">
			<p class="center"><img src="/resources/img/logo.png" alt="logo" /></p>
			<div class="left_col">
				<a href="${actor.getPhotoURL(bigPhoto)}" class="lbox"><img src="${actor.getPhotoURL(smallPhoto)}" class="movie_photo" alt="${actor.getMeno()}" /></a>
			</div>
			<div class="right_col">
				<h2>${actor.getMeno()}</h2>
				<p class="subtitle">${actor.getTyp().getNazov()}</p>
				<p class="description">
					${actor.getDescription(0,descriptionCharacters)}
					<c:if test="${actor.getPopis().length()>descriptionCharacters}">
						<span>... <a href="#" class="show_more">zobraziť všetko</a></span>
						<span class="invisible show_more_display">${actor.getDescription(descriptionCharacters)}</span>
					</c:if>
				</p>
				<c:if test="${!empty actor.getMovies(5)}">
				<div class="actors_and_stuff"><h3>Filmografia:</h3>
					<c:forEach var="movie" items="${actor.getMovies(5)}" varStatus="loopCounter" >
						<div class="actor_microlayout">
							<a href="${movie.getPhotoURL(bigPhoto)}" class="lbox"><img src="${movie.getPhotoURL(tinyPhoto)}" alt="${movie.getNazov()}" /></a><br/>
							<a href="${movie.getProfileURL()}">${movie.getNazov()}</a><br/>
							${movie.getRokVydania()}
						</div>
					</c:forEach>
				</div>
				</c:if>
			</div>
		</div>
    </body>
</html>
