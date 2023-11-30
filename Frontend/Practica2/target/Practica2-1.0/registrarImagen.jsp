<%-- 
    Document   : regislabelatImagen
    Created on : 21 sept 2023, 12:32:13
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
        <title>Register Image</title>
    </head>
    <body>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>

        <% if (errorMessage != null) { %>
        <div style="color: red;">
            <%= errorMessage %>
        </div>
        <% } %>       
        
        <form method="post" action="registrarImagen" enctype="multipart/form-data">
            <h1>Image information</h1>
            <label>Title: </label><input type="text" name="title"/>
            <br> <br>
            <label>Description: </label> <input type="text" name="description"/>
             <br> <br>
             <label>Key words: </label> <input type="text" name="keywords" placeholder="keyword1,keyword2,..."/>
             <br> <br>
            <label>Author: </label> <input type="text" name="author"/>
             <br> <br>
             <label>Creation date:  </label> <input type="text" name="year" placeholder="Year" size="5"/>
                                    </label> <input type="text" name="month" placeholder="Month" size="5"/>
                                    </label> <input type="text" name="day" placeholder="Day" size="5"/>
             <br> <br>
            <input type="file" name="file"/>
             <br> <br>
            <input type="submit" value="Submit" style="margin-left: 270px"/>
        </form> 
        <a href="menu.jsp">Back to menu</a>
    </body>
</html>
