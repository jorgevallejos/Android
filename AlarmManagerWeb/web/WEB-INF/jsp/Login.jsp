<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form:form method="POST" commandName="login">
            <table>
                <tr>
                    <td>Username: </td>
                    <td><form:input path="name" /></td>
                </tr>
                <tr>
                    <td>Password: </td>
                    <td><form:password  path="pass" /></td>
                </tr>
                <tr>
                    <td>
                        <input type="submit" name="Add" value="Submit"></input>
                    </td>
                </tr>
            </table>
        </form:form>
    </body>
</html>
