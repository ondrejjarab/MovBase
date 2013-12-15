<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/jsp/components/head.jsp" %>

        <p> 
            ahoj ${movies}             </p>

            <c:forEach var="i" items="${movies}">
            <p>${i.nazov}</p>
                
            </c:forEach>
        
    </body>
</html>

<%@ include file="/WEB-INF/jsp/components/foot.jsp" %>