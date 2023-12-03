<%-- 
    Document   : list
    Created on : 21 sept 2023, 12:32:58
    Author     : alumne
--%>

<%@ page import="java.util.List" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="com.google.gson.JsonArray" %>
<%@ page import="com.google.gson.JsonObject" %>
<%@ page import="com.google.gson.JsonParser" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.io.IOException" %>

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
        <title>List of images:</title>
    </head>
    <body>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>

        <% if (errorMessage != null) { %>
        <div style="color: red;">
            <%= errorMessage %>
        </div>
        <% } %>   
                
        <% String message = (String) request.getAttribute("message"); %>

        <% if (message != null) { %>
        <div style="color: green;">
            <%= message %>
        </div>
        <% } %> 
        <h1> Image List: </h1>
        <table border="1">
        <thead>
            <tr>
                <th>Title</th>
                <th>User</th>
                <th>Author</th>
                <th>Description</th>
                <th>Key words</th>
                <th>Capture Date</th>
                <th>Storage Date</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <%
                String restServiceUrl = "http://localhost:3000/list";
                //Conexion
                URL url = new URL(restServiceUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                
                //Leemos el json en la respuesta
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonResponse.append(line);
                }

                JsonParser parser = new JsonParser();
                JsonArray imagesArray = (JsonArray) parser.parse(jsonResponse.toString());
                // Iterar a traves de cada JSONObject en el JSONArray
                for (Object obj : imagesArray) {
                    if (obj instanceof JsonObject) {
                        JsonObject image = (JsonObject) obj;
                        String base64Image = image.has("imageData") ? image.get("imageData").getAsString() : "";
            %>  
                        <tr>
                            <td><%= image.get("title").getAsString() %></td>
                            <td><%= image.get("creator").getAsString() %></td>
                            <td><%= image.get("author").getAsString() %></td>
                            <td><%= image.get("description").getAsString() %></td>
                            <td><%= image.get("keywords").getAsString() %></td>
                            <td><%= image.get("creationDate").getAsString() %></td>
                            <td><%= image.get("introductionDate").getAsString() %></td>
                            <td>
                                <a href='data:image/jpeg;base64,<%= image.get("imageData").getAsString() %>'
                                target="_blank">View</a>
            <%
                        //Nombre con el que esta guardada la imagen
                        String idImage = image.get("filename").getAsString();
                        String creator = image.get("creator").getAsString();
                        // Creamos links para modificar y borrar imagenes, en caso de que las imagenes que sean del usuario loggeado
                        if (session.getAttribute("username").equals(creator)) {
            %>
                            |<a href="modificarImagen.jsp?title=<%= image.get("title").getAsString() %>&description=<%= image.get("description").getAsString() %>
                                     &keywords=<%= image.get("keywords").getAsString() %>&author=<%= image.get("author").getAsString() %>
                                     &filename=<%= image.get("filename").getAsString() %>">Modify</a> |
                            <a href="eliminarImagen.jsp?filename=<%= image.get("filename").getAsString() %>">Delete</a>
            <%
                        }
                        out.println("</td>");
                        out.println("</tr>");
                    }
                }
                connection.disconnect();
            %>
        </tbody>
        </table>
        <br>
        <a href="menu.jsp">Back to menu</a>
    </body>
</html>
