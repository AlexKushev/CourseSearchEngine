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
		<br />
		
		<input type="checkbox" name="isFree" value="false" /> Free
		<br />
		
		Max Price: <input type="text" name="maxPrice"/>
		<br />
		
		Source:
		<select name="source">
			<option value="All" selected>All</option>
			<option value="Udemy">Udemy</option>
			<option value="Coursera">Coursera</option>
			<option value="Edx">Edx</option>
		</select>
		
		<br />
		<input type="submit" value="Search" />
	
	</form:form>
	
	
</body>

</html>