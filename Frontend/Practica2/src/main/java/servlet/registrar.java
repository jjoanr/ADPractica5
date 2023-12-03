/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author alumne
 */
@WebServlet(name = "registrar", urlPatterns = {"/registrar"})
public class registrar extends HttpServlet {

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
        //Leer parametros formulario
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        
        //Checks formulario
        if(!password.equals(password2)) {
            request.setAttribute("errorMessage", "Passwords don't match.");
            request.getRequestDispatcher("registrar.jsp").forward(request, response);
            return;
        }

        //URL del servicio REST
        String restServiceUrl = "http://localhost:3000/registerUser";
        //HHTP Connection
        URL url = new URL(restServiceUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //Crear request
        connection.setRequestMethod("POST");
        String requestBody = "username=" + username + "&password=" + password;

        //Enviar request
        connection.setDoOutput(true);
        try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
            out.writeBytes(requestBody);
            out.flush();
        }

        //Obtener respuesta del servicio REST
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {   
            request.setAttribute("errorMessage", "You registered successfully.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } 
        else {
            request.setAttribute("errorMessage", "User already exists.");
            request.getRequestDispatcher("registrar.jsp").forward(request, response);  
        }
        connection.disconnect();
    }
}
