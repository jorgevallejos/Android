<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/AlarmManager.css">
        <link rel="stylesheet" type="text/css" href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css">
        <script type="text/javascript" src="http://code.jquery.com/jquery-1.9.0.min.js"></script>
        <script type="text/javascript" src="http://code.jquery.com/ui/1.10.0/jquery-ui.min.js"></script>
        <script type="text/javascript" src="js/jquery-ui-timepicker-addon.js"></script>
        <script type="text/javascript" src="js/jquery-ui-sliderAccess.js"></script>
        <script type="text/javascript" src="js/functions.js"></script>
        <title>Create Alarm</title>
    </head>
    <body onload="createDatepickers();
            createNumberPicker();">

        <form:form commandName="editAlarm" method="POST">
            <form:hidden path="id"></form:hidden>
            <form:errors path="*" cssClass="error"/>

            <table>
                <tr>
                    <td>Title: </td>
                    <td><form:input path="title"></form:input> </td>
                    </tr>
                    <tr>
                        <td>Description: </td>
                        <td><form:input path="info"></form:input> </td>
                    </tr>
                    <tr>
                        <td>Date: </td>
                        <td><form:input path="eventDateTimeString" id="eventDate" readonly="true"></form:input></td>
                    </tr>
                    <tr>
                        <td>Repeated: </td>
                        <td><form:checkbox path="repeated" id="repeated"></form:checkbox> </td>
                    </tr>
                    <tr>
                        <td>Repeat unit: </td>
                        <td>
                        <form:select path="repeatunit">
                            <form:option value="MINUTE" label="Minute"></form:option>
                            <form:option value="HOUR" label="Hour"></form:option>
                            <form:option value="DAY" label="Day"></form:option>
                            <form:option value="WEEK" label="Week"></form:option>
                            <form:option value="MONTH" label="Month"></form:option>
                            <form:option value="YEAR" label="Year"></form:option>
                        </form:select>
                    </td>
                </tr>
                <tr>
                    <td>Repeat Quantity: </td>
                    <td><form:input path="repeatQuantity" id="repeatQuantity" readonly="true"></form:input></td>
                    </tr>
                    <tr>
                        <td>Repeat End Date: </td>
                        <td><form:input path="endRepeatDateTimeString" id="endDate" readonly="true"></form:input></td>
                    </tr>
                    <tr>
                        <td><input type="submit" value="submit" /></td>
                    </tr>
                </table>

        </form:form>
    </body>
</html>
