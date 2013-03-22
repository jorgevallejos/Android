<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="../jspf/css.jspf" %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Remote Alarm Manager | Something went wrong...</title>
    </head>
    <body>
        <%@include file="../jspf/navbar.jspf" %>
        <div id="container" class="well" style="width: 500px;">
            <legend>Oops!</legend>
            <p>Something somewhere went wrong. My apologies. Please try again later.</p>
        </div>

        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </body>
</html>
