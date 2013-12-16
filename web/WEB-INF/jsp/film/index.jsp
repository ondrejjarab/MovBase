<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/jsp/components/head.jsp" %>

            <c:forEach var="movie" items="${movies}">
				<div class="list_item">
					<img src="${movie.getPhotoURL(tinyPhoto)}" class="photo_tiny" alt="${movie.nazov}">
					<h2>${movie.nazov} (${movie.rokVydania})</h2>
					<c:forEach var="genre" items="${movie.getGenreCollection()}">
						${genre.getNazov()} 
					</c:forEach>
					<a class="show_button" href="${movie.getProfileURL()}">Zobraziť</a>
					<div class="clear"></div>
				</div>
                
            </c:forEach>

<%@ include file="/WEB-INF/jsp/components/foot.jsp" %>