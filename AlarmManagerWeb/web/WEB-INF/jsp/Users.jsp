<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Users</title>
    </head>
    <body>
        <table>

            <tr>
                <td>ID</td>
                <td>Naam</td>
                <td>Achternaam</td>
                <td>E-Mail</td>
            </tr>

            <c:forEach items="${users}" var="u">
                <tr>
                    <td>${u.id}</td>
                    <td>${u.naam}</td>
                    <td>${u.achternaam}</td>
                    <td>${u.email}</td>
                    <td><a href="editUserAlarms.htm?uID=${u.id}">Edit Alarms</a></td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
