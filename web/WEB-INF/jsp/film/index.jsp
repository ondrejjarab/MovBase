<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/jsp/components/head.jsp" %>

<div class="list">
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
</div>
<form method="get" action="/movies" class="filter_form">
	<a href="/movies/new" class="show_button">Pridať nový film</a>
	<p class="center">
		<a href="/movies">&lt;&lt;</a>
		<c:if test="${actualpage-1>0}"><a href="${paging_url}page=${actualpage-1}">&lt;</a></c:if>

		<c:if test="${actualpage-2>0}"><a href="${paging_url}page=${actualpage-2}">${actualpage-2}</a></c:if>
		<c:if test="${actualpage-1>0}"><a href="${paging_url}page=${actualpage-1}">${actualpage-1}</a></c:if>
		${actualpage}
		<c:if test="${actualpage+1<=lastpage}"><a href="${paging_url}page=${actualpage+1}">${actualpage+1}</a></c:if>
		<c:if test="${actualpage+2<=lastpage}"><a href="${paging_url}page=${actualpage+2}">${actualpage+2}</a></c:if>

		<c:if test="${actualpage+1<=lastpage}"><a href="${paging_url}page=${actualpage+1}">&gt;</a></c:if>
		<a href="/movies?page=${lastpage}">&gt;&gt;</a>
	</p>
	<p class="center">
		Hľadať:
		<input type="text" name="search" value="${search}">
	</p>
	<p>
		Žáner:<br>
	<select name="genre">
		<option value="0">Všetky</option>
		<c:forEach var="one_genre" items="${genres}">
			<option value="${one_genre.getZanerId()}"<c:if test="${genre==one_genre.getZanerId()}"> selected="selected"</c:if>>${one_genre.getNazov()}</option>
		</c:forEach>
	</select></p>
	<p>
	Zoradiť:
	<select name="order">
		<option value="0">Abecedne A-Z</option>
		<option value="1"<c:if test="${order==1}"> selected="selected"</c:if>>Abecedne Z-A</option>
		<option value="2"<c:if test="${order==2}"> selected="selected"</c:if>>Od najstaršieho</option>
		<option value="3"<c:if test="${order==3}"> selected="selected"</c:if>>Od najnovšieho</option>
		<option value="4"<c:if test="${order==4}"> selected="selected"</c:if>>Od prvých pridaných</option>
		<option value="5"<c:if test="${order==5}"> selected="selected"</c:if>>Od posledne pridaných</option>
	</select></p><p>
	Položiek na stránku:
	<select name="items">
		<option value="10">10 filmov</option>
		<option value="20"<c:if test="${items==20}"> selected="selected"</c:if>>20 filmov</option>
		<option value="30"<c:if test="${items==30}"> selected="selected"</c:if>>30 filmov</option>
		<option value="40"<c:if test="${items==40}"> selected="selected"</c:if>>40 filmov</option>
		<option value="50"<c:if test="${items==50}"> selected="selected"</c:if>>50 filmov</option>
	</select></p>
	<p><button type="submit">Zobraziť</button></p>
</form>

<%@ include file="/WEB-INF/jsp/components/foot.jsp" %>