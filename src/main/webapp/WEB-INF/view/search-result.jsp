<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>

<head>

</head>

<body>

	<h3>Search results are listed here:</h3>
    
    <c:choose>
        <c:when test="${fn:length(list) gt 0}">
            <c:forEach items="${list}" var="course">
                <div>
                    Source : <c:out value="${course.source}"/>
                    Title : <c:out value="${course.title}"/>
                    Price : <c:out value="${course.price}"/>
                    Author : <c:out value="${course.author}"/>
                    Length : <c:out value="${course.length}"/>
                    Rating : <c:out value="${course.rating}"/>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div>Your search "<c:out value="${searchQuery}" />" did not match any courses.</div>
        </c:otherwise>
    </c:choose>
	

	


</body>

</html>