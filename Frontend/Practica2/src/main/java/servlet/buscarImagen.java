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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author alumne
 */
@WebServlet(urlPatterns = {"/buscarImagen"})
public class buscarImagen extends HttpServlet {

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
        
        String searchWord = request.getParameter("searchWord");
        
        //URL del servicio REST
        String restServiceUrl = "http://localhost:3000/search";
        
        //HHTP Connection
        URL url = new URL(restServiceUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // Crear request
        connection.setRequestMethod("POST");
        String requestBody = "searchWord=" + searchWord;
               
        // Enviar request
        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        // GObtener respuesta del servicio REST
        int responseCode = connection.getResponseCode();
        
        System.out.println(responseCode);
        
        if(responseCode == 200) {
            // Leer el JSON en la respuesta
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResponse.append(line);
            }

            //Almacenar json en cuerpo solicitud
            request.setAttribute("searchResultsJson", jsonResponse.toString());
            request.getRequestDispatcher("mostrarResultados.jsp").forward(request, response);
        }
        else if(responseCode == 404) {
            request.setAttribute("errorMessage", "No results were found.");
            request.getRequestDispatcher("buscarImagen.jsp").forward(request, response);
        }
        else {
            request.setAttribute("errorMessage", "Internal error.");
            request.getRequestDispatcher("buscarImagen.jsp").forward(request, response);
        }
        connection.disconnect();
    }
}
