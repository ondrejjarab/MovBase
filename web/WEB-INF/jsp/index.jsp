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
	<title>Movied.com - Not Just Another MOVIEDatabase</title>
  </head>
    <body>
		<div id="holder">
			<p class="center"><img src="/resources/img/logo.png" alt="logo" /></p>
			
			<div id="main_animation">
				<c:forEach var="movie" items="${movies}" varStatus="i" >
					<div class="animate 
						 <c:choose><c:when test="${i.count==1}">animate_active</c:when>
								   <c:otherwise>invisible</c:otherwise></c:choose>
								   " id="n${i.count}"><a href="${movie.getProfileURL()}"><img src="${movie.getPhotoURL(smallPhoto)}" alt="${movie.getNazov()}"></a></div>
				</c:forEach>
				<div id="animation_controller">
					<c:forEach var="movie" items="${movies}" varStatus="i" >
						<div class="controller_unit
							 <c:if test="${i.count==1}">animation_controller_active</c:if>
							 " id="c${i.count}"><strong>${movie.getNazov()}</strong><br>${movie.getRokVydania()}</div>
					</c:forEach>
				</div>
			</div>
		</div>
    </body>
</html>
