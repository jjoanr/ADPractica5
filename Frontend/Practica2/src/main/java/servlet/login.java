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
@WebServlet(urlPatterns = {"/login"})
public class login extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            //Obtener datos del formulario
            String username = request.getParameter("username");
            String password = request.getParameter("password");
           
            String action = request.getParameter("action");
            if (action != null && action.equals("logout")) {
                //Logout
                HttpSession session = request.getSession(); //Obtener sesion
                if (session != null) {
                    session.invalidate(); // Invalidar sesion
                    response.sendRedirect("login.jsp");
                    return;
                }
            }
            
            //URL del servicio REST
            String restServiceUrl = "http://localhost:3000/login";
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
                HttpSession session = request.getSession();
                session.setAttribute("username", username);    
                response.sendRedirect("menu.jsp");
            } 
            else {
                response.sendRedirect("error.jsp?error=Username or password incorrect.");                
            }
            connection.disconnect();
    }
}
