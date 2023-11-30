<%-- 
    Document   : buscarImagen
    Created on : 21 sept 2023, 12:33:09
    Author     : alumne
--%>

<%
    if(session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
    }
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search image</title>
    </head>
    <body>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>

        <% if (errorMessage != null) { %>
        <div style="color: red;">
            <%= errorMessage %>
        </div>
        <% } %>  
        <br>
        <label>Search for title, keywords, author or user.</label>
        <br><br>
        <form action="buscarImagen" method="POST"> 
            <input type="search" id="query" name="searchWord" placeholder="Search...">
            <button type="submit">Search</button>
        </form>
        <br><br>
        <a href="menu.jsp">Back to menu</a>
    </body>
</html>
