<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ include file="/WEB-INF/jsp/components/head.jsp" %>

			<div class="left_col clear">
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
                                                    <p class="center">${comment.getKomentar()}</p>
						</c:forEach>
					</c:otherwise>
				</c:choose>
                                <form:form method="POST" action="/movie/${movie.getFilmId()}/comments" modelAttribute="comment">
                                    <form:label path="komentar">Tvoj komentár</form:label>
                                    <form:textarea path="komentar" rows="3" cols="99" />
                                    <input type="submit" value="Pridaj komentár"/>
                                </form:form>
			</div>
			
<%@ include file="/WEB-INF/jsp/components/foot.jsp" %>
