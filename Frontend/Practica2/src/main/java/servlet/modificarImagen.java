package servlet;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import com.google.gson.JsonObject;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author alumne
 */
@WebServlet(urlPatterns = {"/modificarImagen"})
public class modificarImagen extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        if(session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        //Obtener datos actualizados de la imagen
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String keywords = request.getParameter("keywords");
        String author = request.getParameter("author");
        String creator = (String)request.getSession().getAttribute("username");
        
        //Valores originales
        String oldtitle = request.getParameter("oldtitle");
        String olddescription = request.getParameter("olddescription");
        String oldkeywords = request.getParameter("oldkeywords");
        String oldauthor = request.getParameter("oldauthor");
        String filename = request.getParameter("filename");
        //Boton restore presionado, restaurar valores originales
        String restoreButton = request.getParameter("restore");
        if ("Restore Previous Values".equals(restoreButton)) {
            // Construir la URL de redirección
            String redirectURL = String.format("modificarImagen.jsp?title=" + oldtitle + "&description=" + olddescription 
                                              + "&keywords=" + oldkeywords + "&author=" + oldauthor + "&filename=" + filename);

            // Redirigir al usuario
            response.sendRedirect(redirectURL);
            return;
        }
        
        //Obtener nombre del fichero
        String[] parts = filename.split("\\.");
        String extension = parts.length > 1 ? "." + parts[parts.length - 1] : "";

        //Campos tienen valor
        if (title == null || description == null || keywords == null || author == null) {
            request.setAttribute("errorMessage", "Every field must have a value.");
            request.getRequestDispatcher("modificarImagen.jsp").forward(request, response);
            return;
        }
        
        if(title.equals(oldtitle) && description.equals(olddescription) && keywords.equals(oldkeywords) && author.equals(oldauthor)) {
            request.setAttribute("errorMessage", "No changes in the attributes were made.");
            request.getRequestDispatcher("modificarImagen.jsp").forward(request, response);
            return;
        }
        
        // Construir un objeto JSON con los datos actualizados
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("title", title.trim());
        jsonBody.addProperty("oldtitle", oldtitle.trim());
        jsonBody.addProperty("description", description.trim());
        jsonBody.addProperty("keywords", keywords.trim());
        jsonBody.addProperty("author", author.trim());
        jsonBody.addProperty("creator", creator.trim());
        jsonBody.addProperty("filename", filename.trim());
        jsonBody.addProperty("extension", extension.trim());
        
        //Convertir json a string
        String jsonBodyString = jsonBody.toString();  
        
        //URL del servicio REST
        String restServiceUrl = "http://localhost:3000/modify";
        //HHTP Connection
        URL url = new URL(restServiceUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //Crear request
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        // Enviar request
        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBodyString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        //Obtener respuesta del servicio REST
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {   
            request.setAttribute("message", "Image attributes updated successfully.");
            request.getRequestDispatcher("list.jsp").forward(request, response);
            connection.disconnect();
        } 
        else if (responseCode == 409) {
            request.setAttribute("errorMessage", "Title already used.");
            request.getRequestDispatcher("modificarImagen.jsp").forward(request, response);  
            connection.disconnect();
        }
        else {
            request.setAttribute("errorMessage", "Internal error modifying the image.");
            request.getRequestDispatcher("modificarImagen.jsp").forward(request, response);  
            connection.disconnect();
        }
    }
}

