<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Add / Remove alarms from user</h1>

        <h2>User Info</h2>
        <table>
            <tr>
                <td>ID: </td>
                <td>${user.id}</td>
            </tr>
            <tr>
                <td>Naam: </td>
                <td>${user.naam}</td>
            </tr>
            <tr>
                <td>Achternaam: </td>
                <td>${user.achternaam}</td>
            </tr>
            <tr>
                <td>E-mail: </td>
                <td>${user.email}</td>
            </tr>
        </table>

        <h2>Available Alarms:</h2>
        <table>
            <tr>
                <td>ID</td>
                <td>Title</td>
                <td>Description</td>
                <td>Date</td>
                <td>Repeated</td>
                <td>Unit</td>
                <td>Quantity</td>
                <td>End Date</td>
            </tr>
            <c:forEach items="${alarmsAvailable}" var="a">
                <tr>
                    <td>${a.id}</td>
                    <td>${a.title}</td>
                    <td>${a.info}</td>
                    <td>${a.eventDateTimeString}</td>
                    <td>${a.repeated}</td>
                    <c:choose>
                        <c:when test="${a.repeated == true}">
                            <td>${a.repeatunit}</td>
                            <td>${a.repeatQuantity}</td>
                            <td>${a.endRepeatDateTimeString}</td>
                        </c:when>
                        <c:otherwise>
                            <td>N/A</td>
                            <td>N/A</td>
                            <td>N/A</td>
                        </c:otherwise>
                    </c:choose>
                    <td><a href="addAlarmToUserAction.htm?uID=${user.id}&aID=${a.id}">Add</a></td>
                </tr>
            </c:forEach>
        </table>

        <h2>Linked Alarms</h2>

        <table>
            <tr>
                <td>ID</td>
                <td>Title</td>
                <td>Description</td>
                <td>Date</td>
                <td>Repeated</td>
                <td>Unit</td>
                <td>Quantity</td>
                <td>End Date</td>
            </tr>
            <c:forEach items="${alarmsLinked}" var="a">
                <tr>
                    <td>${a.id}</td>
                    <td>${a.title}</td>
                    <td>${a.info}</td>
                    <td>${a.eventDateTimeString}</td>
                    <td>${a.repeated}</td>
                    <c:choose>
                        <c:when test="${a.repeated == true}">
                            <td>${a.repeatunit}</td>
                            <td>${a.repeatQuantity}</td>
                            <td>${a.endRepeatDateTimeString}</td>
                        </c:when>
                        <c:otherwise>
                            <td>N/A</td>
                            <td>N/A</td>
                            <td>N/A</td>
                        </c:otherwise>
                    </c:choose>
                    <td><a href="removeAlarmFromUser.htm?uID=${user.id}&aID=${a.id}">Remove</a></td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
