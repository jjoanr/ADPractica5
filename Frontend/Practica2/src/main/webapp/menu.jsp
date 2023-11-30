<%-- 
    Document   : menu.jsp
    Created on : 21 sept 2023, 12:31:49
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
    <title>Menu</title>
</head>
<body>
    <% String message = (String) request.getAttribute("message"); %>

        <% if (message != null) { %>
        <div style="color: green;">
            <%= message %>
        </div>
    <% } %>
    <form>
        <h2 style="margin-left: 20px; font-size: 20px;"><% out.println("Welcome " + session.getAttribute("username") + ","); %> </h2>
        <h1 style="font-size: 25px; font-weight: bold; text-align: left; color: #333; margin-left: 20px">Choose an option:</h1>
        <button style="padding: 10px 20px; font-size: 16px; font-weight: bold; text-align: center; text-decoration: none; border: 2px solid #ccc; color: #fff; background-color: #4CAF50; cursor: pointer; margin-left: 20px" type="button" onclick="window.location.href='registrarImagen.jsp'">Register Image</button>
        <br><br>
        <button style="padding: 10px 20px; font-size: 16px; font-weight: bold; text-align: center; text-decoration: none; border: 2px solid #ccc; color: #fff; background-color: #4CAF50; cursor: pointer; margin-left: 20px" type="button" onclick="window.location.href='list.jsp'">List Images</button>
        <br><br>        <button style="padding: 10px 20px; font-size: 16px; font-weight: bold; text-align: center; text-decoration: none; border: 2px solid #ccc; color: #fff; background-color: #4CAF50; cursor: pointer; margin-left: 20px" type="button" onclick="window.location.href='buscarImagen.jsp'">Search Image</button>
        <br><br>
    </form>
    <form id="logoutForm" action="login" method="POST">
        <input type="hidden" name="action" value="logout">
        <button type="button" onclick="handleLogout()">Logout</button>
    </form>
    <script>
        function handleLogout() {
            document.getElementById('logoutForm').submit();
        }
    </script>       
</body>
</html>

