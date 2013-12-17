<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/jsp/components/head.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
                        <h2 class="add_new_headline">Pridanie novej osobnosti</h2>
                <form:form method="POST" action="/people" modelAttribute="person" enctype="multipart/form-data">
                    <table class="add_table">
						<tr>
							<td>Profilová fotografia:</td>
							<td><input type="file" name="fotografia" /></td>
						</tr>
                        <tr>
                            <td><form:label path="meno">Meno a priezvisko</form:label></td>
                            <td><form:input path="meno" /></td>
                        </tr>
                        <tr>
                            <td><form:label path="popis">Popis</form:label></td>
                            <td><form:textarea path="popis" /></td>
                        </tr>
                        
                        <tr>
        <td><form:label name = "typ" path="typ">Povolanie: </form:label></td>
        <td><form:select name = "typ" path="typ" items="${types}" itemValue="profesiaId" itemLabel="nazov"/></td>
    </tr>
    
              <tr>
        <td><form:label name="narodnost" path="narodnost">Národnosť: </form:label></td>
        <td><form:select name="narodnost" path = "narodnost" items="${nations}" itemValue="krajinaId" itemLabel="nazov"/></td>
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