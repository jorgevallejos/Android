<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome to Spring Web MVC project</title>
    </head>
    <body>
        <ul>
            <li><a href="loginForm.htm">Login</a></li>
            <li><a href="registerForm.htm">Register</a></li>
            <li><a href="alarmForm.htm">Alarm Form</a></li>
            <li><a href="alarms.htm">Alarms</a></li>
            <li><a href="addUserToAlarm.htm?aID=409">Add user to alarm</a></li>
            <li><a href="users.htm">Users</a></li>
        </ul>
        <c:choose>
            <c:when test="${user != null}">
                <p>U bent ingelogd als <c:out value="${user.naam}"/>!</p>
            </c:when>
            <c:otherwise>
                <p>U bent niet ingelogd</p>
            </c:otherwise>
        </c:choose>
    </body>
</html>
