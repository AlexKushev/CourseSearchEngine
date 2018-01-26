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
                    Provider : <c:out value="${course.provider}"/>
                    Title : <c:out value="${course.title}"/>
                    Price : <c:out value="${course.price}"/>
                    Instructor : <c:out value="${course.instructor}"/>
                    Length : <c:out value="${course.length}"/>
                    Rating : <c:out value="${course.rating}"/>
                    Description : <c:out value="${course.description }"/>
                    Language : <c:out value="${course.language}"/>
                    <!-- Add button to click and redirect to url -->
                    url : <c:out value="${course.pageUrl}"/>
                    Image : <c:out value="${course.imageUrl}"/>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div>Your search "<c:out value="${searchQuery}" />" did not match any courses.</div>
        </c:otherwise>
    </c:choose>
	

	


</body>

</html>