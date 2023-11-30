<%-- 
    Document   : registrar.jsp
    Created on : 3 oct 2023, 9:29:32
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
    </head>
    <body>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>

        <% if (errorMessage != null) { %>
        <div style="color: red;">
            <%= errorMessage %>
        </div>
        <% } %>
        <form action="registrar" method="POST"> 
        <h2>Enter registration information</h2>
        <label>Username: </label><input type="text" name="username"/>
        <br> <br>
        <label>Password: </label><input type="password" name="password"/>
        <br><br>
        <label>Repeat password: </label><input type="password" name="password2"/>
        <br><br>
        <button type="button" onclick="window.location.href='login.jsp'">Return</button>
        <button type="submit">Register</button>
        </form>
    </body>
</html>
