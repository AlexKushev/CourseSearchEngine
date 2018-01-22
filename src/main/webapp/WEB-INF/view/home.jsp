<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<html>
<head>
	<title>Course Searcher</title>
</head>

<body>
	<h2>Simple Course Searcher</h2>
	
	<form:form action="${pageContext.request.contextPath}/search"  method="GET">
	
		Search: <input type="text" name="searchQuery" />
		
		<input type="submit" value="Search" />
	
	</form:form>
	
	
</body>

</html>