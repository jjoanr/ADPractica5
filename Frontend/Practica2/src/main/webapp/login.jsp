<%-- 
    Document   : login.jsp
    Created on : 21 sept 2023, 12:30:33
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>

        <% if (errorMessage != null) { %>
        <div style="color: green;">
            <%= errorMessage %>
        </div>
        <% } %>
        <h1>AD - P3</h1>
        <form action="login" method="POST"> 
            <label>Username: </label><input type="text" name="username"/>
            <br> <br>
            <label>Password: </label><input type="password" name="password"/>
            <br><br>
            <button type="button" onclick="window.location.href='registrar.jsp'">Register</button>
            <button type="submit">Login</button>
        </form>
    </body>
</html>
