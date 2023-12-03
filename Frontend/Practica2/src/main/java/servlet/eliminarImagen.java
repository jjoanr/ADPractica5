package servlet;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author alumne
 */
@WebServlet(urlPatterns = {"/eliminarImagen"})
public class eliminarImagen extends HttpServlet {
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
        
        String filename = request.getParameter("filename");
        
        //URL del servicio REST
        String restServiceUrl = "http://localhost:3000/delete";
        
        //HHTP Connection
        URL url = new URL(restServiceUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        //Crear request
        connection.setRequestMethod("POST");
        String requestBody = "id=" + filename;
        
        //Enviar request
        connection.setDoOutput(true);
        try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
            out.writeBytes(requestBody);
            out.flush();
        }
        
        //Obtener respuesta del servicio REST
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {           
            request.setAttribute("message", "Image deleted successfully.");
            request.getRequestDispatcher("list.jsp").forward(request, response);
        } 
        else {
            request.setAttribute("errorMessage", "Image deletion failed.");
            request.getRequestDispatcher("list.jsp").forward(request, response);        
        }
        connection.disconnect();
    }
}
