<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Alarms</title>
    </head>
    <body>
        <a href="alarmForm.htm">ADD</a>
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
                <td>Edit</td>
                <td>Delete</td>
            </tr>

            <c:forEach items="${alarms}" var="a">
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
                    <td><a href="alarmForm.htm?id=${a.id}">edit</a></td>
                    <td><a href="editAlarmUsers.htm?aID=${a.id}">edit users</a></td>
                    <td><a href="deleteAlarm.htm?id=${a.id}">delete</a></td>
                </tr>
            </c:forEach>

        </table>

    </body>
</html>
