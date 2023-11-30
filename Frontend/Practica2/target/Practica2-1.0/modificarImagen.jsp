<%-- 
    Document   : modificarImagen
    Created on : 18 oct 2023, 18:36:49
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
        <title>Modify Image</title>
    </head>
    <body>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>

        <% if (errorMessage != null) { %>
        <div style="color: red;">
            <%= errorMessage %>
        </div>
        <% } %>   
        <form method="post" action="modificarImagen">
            <h1>Modify Image</h1>
            <!-- Para que el servlet pueda obtener los valores originales de la imagen -->
            <input type="hidden" name="oldtitle" value="<%= request.getParameter("title").trim() %>" />
            <input type="hidden" name="olddescription" value="<%= request.getParameter("description").trim() %>" />
            <input type="hidden" name="oldkeywords" value="<%= request.getParameter("keywords").trim() %>" />
            <input type="hidden" name="oldauthor" value="<%= request.getParameter("author").trim() %>" />
            <input type="hidden" name="filename" value="<%= request.getParameter("filename").trim() %>" />

            <label>Title:</label>
            <input type="text" name="title" value="<%= request.getParameter("title").trim() %>" required/><br>
            <br>
            <label>Description:</label> 
            <input type="text" name="description" value="<%= request.getParameter("description").trim() %>" required/><br>
            <br>
            <label>Keywords:</label> 
            <input type="text" name="keywords" value="<%= request.getParameter("keywords").trim() %>" required/><br>
            <br>
            <label>Author:</label> 
            <input type="text" name="author" value="<%= request.getParameter("author").trim() %>" required/><br>
            <br>
            <input type="submit" name="restore" value="Restore Previous Values"/>
            <input type="submit" value="Modify" style="margin-left: 50p"/>
            <br><br>
            <a href="list.jsp">Back to list</a>
        </form>
    </body>
</html>
