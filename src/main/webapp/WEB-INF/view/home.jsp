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
		<br />
		
		<input type="checkbox" name="isFree" value="false" /> Free
		<br />
		
        <!-- Hide when ifFree checkbox is checked -->
        Price Range : 
        <select name="priceRange">
            <option value="0" selected>All Prices </option>
            <option value="1">Max 49.99</option>
            <option value="2">50 - 99.99</option>
            <option value="3">Over 100</option>
        </select>
        
		<br />
		
		Source:
		<select name="source" multiple="multiple">
            <option value="All" selected> All Sources </option>
			<option value="Udemy">Udemy</option>
			<option value="Coursera">Coursera</option>
			<option value="Edx">Edx</option>
		</select>
		
		<br />
	</form:form>
	
	
</body>

</html>