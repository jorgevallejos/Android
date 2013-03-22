<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add User To Alarm</title>
    </head>
    <body>
        
        <h1>Add User To Alarm</h1>
        
        <h2>Alarm Info</h2>
        <table>
            <tr>
                <td>ID: </td>
                <td>${alarm.id}</td>
            </tr>
            <tr>
                <td>Title: </td>
                <td>${alarm.title}</td>
            </tr>
            <tr>
                <td>Info: </td>
                <td>${alarm.info}</td>
            </tr>
            <tr>
                <td>Date: </td>
                <td>${alarm.eventDateTimeString}</td>
            </tr>
            <tr>
                <td>Repeated: </td>
                <td>${alarm.repeated}</td>
            </tr>
            <tr>
                <td>Repeat unit: </td>
                <td>${alarm.repeatunit}</td>
            </tr>
            <tr>
                <td>Repeat quantity: </td>
                <td>${alarm.repeatQuantity}</td>
            </tr>
            <tr>
                <td>Repeat End Date: </td>
                <td>${alarm.endRepeatDateTimeString}</td>
            </tr>
        </table>
        
        <h2>Available Users</h2>
        
        <table>
            <tr>
                <td>Naam</td>
                <td>Achternaam</td>
                <td>E-mail</td>
            </tr>
            <c:forEach items="${usersAvailable}" var="u">
                <tr>
                    <td>${u.naam}</td>
                    <td>${u.achternaam}</td>
                    <td>${u.email}</td>
                    <td><a href="addUserToAlarmAction.htm?uID=${u.id}&aID=${alarm.id}">Add</a></td>
                </tr>
            </c:forEach>
        </table>
        
        <h2>Linked Users</h2>
        <table>
            <tr>
                <td>Naam</td>
                <td>Achternaam</td>
                <td>E-mail</td>
            </tr>
            <c:forEach items="${usersLinked}" var="u">
                <tr>
                    <td>${u.naam}</td>
                    <td>${u.achternaam}</td>
                    <td>${u.email}</td>
                    <td><a href="removeUserFromAlarm.htm?uID=${u.id}&aID=${alarm.id}">Remove</a></td>
                </tr>
            </c:forEach>
        </table>
        
    </body>
</html>
