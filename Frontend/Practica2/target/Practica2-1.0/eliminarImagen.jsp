<%-- 
    Document   : eliminarImagen
    Created on : 21 sept 2023, 12:32:48
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
    <title>Delete Image</title>
</head>
<body>
    <h1>Delete Image:</h1>
    <p>Are you sure you want to delete this image?</p>
    <form action="eliminarImagen" method="post">
        <input type="hidden" name="filename" value="<%= request.getParameter("filename").trim() %>" />
        <a href="list.jsp">Cancel</a>
        <button type="submit" style="margin-left: 30px">Yes, Delete</button>
    </form>
</body>
</html>


