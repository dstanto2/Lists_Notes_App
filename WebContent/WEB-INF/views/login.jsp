<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
	<head>
		<title>Login!</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/experiment.css" />" />
		<link rel="icon" type="image/x-icon" href="<c:url value="/resources/images/favicon.ico" />" />
	</head>
	<body>
		<div>
			<form method="POST" action="${contextPath}/login" style="float: right;">
				<input id="username" name="username" placeholder="Username" type="text" style="height: 30px; box-sizing: border-box; margin: 0px 1px 0px 1px; width: 150px;"/>
				<input id="password" name="password" placeholder="Password" type="password" style="height: 30px; box-sizing: border-box; margin: 0px 1px 0px 1px; width: 150px;"/>
				<button type="submit" style="height: 30px; box-sizing: border-box; margin: 0px 1px 0px 1px; width: 75px;">Log In</button>
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			</form>
		</div>
		<div style="clear: both; text-align: center">
			<div>
				<h1 style="display: inline-block; width: 25%; margin: 10px 0px 10px 0px;">Lists App</h1>
			</div>
			<div>
				<p style="display: inline-block; width: 25%; margin: 10px 0px 10px 0px;">Store and retrieve what you want to remember</p>
			</div>
			<div>
				<form method="POST" action="${contextPath}/signup" style="display: inline-block; width: 25%">
					<input id="username" name="username" placeholder="Username" type="text" style="width: 100%; height: 30px; box-sizing: border-box; margin: 5px 0px 5px 0px;"/>
					<input id="password" name="password" placeholder="Password" type="password" style="width: 100%; height: 30px; box-sizing: border-box; margin: 5px 0px 5px 0px;"/>
					<input id="confirmPassword" name="confirmPassword" placeholder="Confirm Password" type="password" style="width: 100%; height: 30px; box-sizing: border-box; margin: 5px 0px 5px 0px;"/>
					<button type="submit" style="width: 100%; height: 30px; margin: 5px 0px 5px 0px;">Sign Up</button>
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
			</div>
			<c:if test="${not empty errMsg}">
				<span style="display: block;"><c:out value="${errMsg}"/></span>
			</c:if>
		</div>
	</body>

</html>