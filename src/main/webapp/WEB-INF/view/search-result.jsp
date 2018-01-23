<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>

</head>

<body>

	<h3>Search results are listed here:</h3>
	
	<c:forEach items="${list}" var="course">
	<div>
		Source : <c:out value="${course.source}"/>
		Title : <c:out value="${course.title}"/>
		Price : <c:out value="${course.price}"/>
		Author : <c:out value="${course.author}"/>
		Length : <c:out value="${course.length}"/>
		</div>
	</c:forEach>
	


</body>

</html>