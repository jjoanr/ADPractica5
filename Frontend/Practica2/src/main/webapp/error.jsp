<%-- 
    Document   : error
    Created on : 21 sept 2023, 12:33:21
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error</title>
    </head>
    <body>
        <%
            String error = request.getParameter("error");
   
            if (error != null) {
        %>
        
        <label style="color: red"> <%out.println(error);%> </label>
        <br><br>
        <button style="padding: 10px 20px; font-size: 16px; font-weight: bold; text-align: center; text-decoration: none; border: 2px solid #ccc; color: #fff; background-color: #4CAF50; cursor: pointer; margin-left: 20px" type="button" onclick="window.location.href='login.jsp'">Return to login</button>
        
        <%
            }
        %>
    </body>
</html>
