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

        <form:form commandName="registerUser" method="POST">
            <form:errors path="*" cssClass="error"/>

            <table>
                <tr>
                    <td>Naam: </td>
                    <td><form:input path="naam"></form:input></td>
                    </tr>
                    <tr>
                        <td>Achternaam: </td>
                        <td><form:input path="achternaam"></form:input></td>
                    </tr>
                    <tr>
                        <td>E-mail: </td>
                        <td><form:input path="email"></form:input></td>
                    </tr>
                    <tr>
                        <td>Paswoord: </td>
                        <td><form:input path="paswoord"></form:input></td>
                    </tr>
                    <tr>
                        <td>Herhaal Paswoord: </td>
                        <td><form:input path="paswoordRepeat"></form:input></td>
                    </tr>
                    <tr>
                        <td><input type="submit" value="Submit" /></td>
                    </tr>
                </table>

        </form:form>

    </body>
</html>
