<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/jsp/components/head.jsp" %>
			
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

<%@ include file="/WEB-INF/jsp/components/foot.jsp" %>