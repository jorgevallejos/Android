<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="../jspf/css.jspf" %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Users</title>
    </head>
    <body>
        <%@include file="../jspf/navbar.jspf" %>
        <div id="container" class="well">
            <ul class="breadcrumb">
                <li><a href="home.htm">Home</a><span class="divider">/</span></li>
                <li class="active">Users</li>
            </ul>
            <legend>Users Overview</legend>
            <table class="table-striped table-bordered table-hover overview" style="width: 940px;">
                <tr>
                    <th>ID</th>
                    <th>Naam</th>
                    <th>Achternaam</th>
                    <th>E-Mail</th>
                    <th></th>
                </tr>

                <c:forEach items="${users}" var="u">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.naam}</td>
                        <td>${u.achternaam}</td>
                        <td>${u.email}</td>
                        <td><a href="editUserAlarms.htm?uID=${u.id}"><i class="icon-time"></i></a></td>
                    </tr>
                </c:forEach>
            </table>
            <div style="height: 15px"></div>
            <a href="registerForm.htm"><button class="btn"><i class="icon-plus"></i>Add</button></a>
            <div style="height: 15px"></div>

            <h1 style="font-size: 14pt; margin-bottom: -5px;">Symbols</h1>
            <table class="table table-condensed">
                <tr>
                    <td><i class="icon-time"></i> </td>
                    <td>Click here to add alarms to or remove alarms from this user.</td>
                </tr>
            </table>
        </div>
    </body>
</html>
