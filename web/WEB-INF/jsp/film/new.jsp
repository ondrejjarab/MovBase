<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/jsp/components/head.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
                        <h2 class="add_new_headline">Pridanie nového filmu</h2>
<form:form method="POST" action="/movies" modelAttribute="film">
   <table class="add_table">
    <tr>
        <td><form:label path="nazov">Názov:*</form:label></td>
        <td><form:input path="nazov" /></td>
    </tr>
    <tr>
        <td><form:label path="popis">Popis:</form:label></td>
        <td><form:textarea path="popis" /></td>
    </tr>
    <tr>
        <td><form:label path="originalnyNazov">Originálny názov:</form:label></td>
        <td><form:input path="originalnyNazov" /></td>
    </tr>
    <tr>
        <td><form:label path="rokVydania">Rok vydania:</form:label></td>
        <td><form:input path="rokVydania" /></td>
    </tr>
    <tr>
        <td><form:label path="minutaz">Minutáž:</form:label></td>
        <td><form:input path="minutaz" /></td>
    </tr>
    <tr>
        <td><form:label path="pocetCasti">Počet častí:</form:label></td>
        <td><form:input path="pocetCasti" /></td>
    </tr>
    <tr>
        <td><form:label path="typ">Typ:</form:label></td>
        <td><form:select path="typ" items="${types}"/></td>
    </tr>
    
    <tr>
		<td></td>
        <td>
            <button type="submit">Odoslať</button>
        </td>
    </tr>
</table>  
</form:form>
			
<%@ include file="/WEB-INF/jsp/components/foot.jsp" %>
