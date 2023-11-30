package servlet;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.time.LocalDateTime;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.PrintWriter;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

/**
 *
 * @author alumne
 */
@WebServlet(name = "registrarImagen", urlPatterns = {"/registrarImagen"}) 
@MultipartConfig() 
public class registrarImagen extends HttpServlet {

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
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession();
        if(session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
       
        //Obtenemos parametros del formulario
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String keywords = request.getParameter("keywords");
        String author = request.getParameter("author");
        String creationDate = request.getParameter("year") + "/" + 
                              request.getParameter("month") + "/" +
                              request.getParameter("day");
        
        // Validacion de que todos los campos tienen valor
        if (title == null || title.trim().isEmpty() || description == null || description.trim().isEmpty() ||
            keywords == null || keywords.trim().isEmpty() || author == null || author.trim().isEmpty() || 
            creationDate == null || creationDate.trim().isEmpty()) {
            
                request.setAttribute("errorMessage", "All fields are required.");
                request.getRequestDispatcher("registrarImagen.jsp").forward(request, response);
                return;
        }
        
        //Month entre 1 y 12
        if(Integer.parseInt(request.getParameter("month")) < 1 || Integer.parseInt(request.getParameter("month")) > 12) {
            request.setAttribute("errorMessage", "Month field incorrect.");
            request.getRequestDispatcher("registrarImagen.jsp").forward(request, response);
            return;
        }
        
        //Day entre 1 y 31
        if(Integer.parseInt(request.getParameter("month")) < 1 || Integer.parseInt(request.getParameter("month")) > 31) {
            request.setAttribute("errorMessage", "Day field incorrect.");
            request.getRequestDispatcher("registrarImagen.jsp").forward(request, response);
            return;
        }
        
        String creator = (String)request.getSession().getAttribute("username");
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y/M/d");
        String introductionDate = currentDate.format(formatter);
        
        //Obtener fichero
        Part fileP = request.getPart("file");
                
        String submittedFileName = fileP.getSubmittedFileName();        
        String[] parts = submittedFileName.split("\\.");
        String extension = parts.length > 1 ? "." + parts[parts.length - 1] : "";

        String fileName = title + "-" +  creator + extension;
        
        //Validacion de formato de imagen
        if(!extension.equals(".jpg") && !extension.equals(".jpeg") &&
            !extension.equals(".png") && !extension.equals(".gif") ) {
            
            request.setAttribute("errorMessaget", "Incorrect format. Formats addmited: jpg, jpeg, png and gif.");
            request.getRequestDispatcher("registrarImagen.jsp").forward(request, response);
            return;
        }

        // Crear JSON object con los datos
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("title", title.trim());
        jsonBody.addProperty("description", description.trim());
        jsonBody.addProperty("keywords", keywords.trim());
        jsonBody.addProperty("author", author.trim());
        jsonBody.addProperty("creator", creator.trim());
        jsonBody.addProperty("creationDate", creationDate.trim());
        jsonBody.addProperty("introductionDate", introductionDate.trim());
        jsonBody.addProperty("filename", fileName.trim());
        
        //Convertir json a string
        String jsonBodyString = jsonBody.toString();  
        
        //Enviar fichero al servicio REST
        final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
        StreamDataBodyPart filepart = new StreamDataBodyPart("file", fileP.getInputStream());
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart
                .field("jsonInput", jsonBodyString, MediaType.APPLICATION_JSON_TYPE)
                .bodyPart(filepart);

        WebTarget target = client.target("http://localhost:3000/register");
        Response resp = target.request().post(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA_TYPE));
        int status = resp.getStatus();

        
        formDataMultiPart.close();
        multipart.close();
        
        if (status == 200) {
            request.setAttribute("message", "Image uploaded successfully.");
            request.getRequestDispatcher("menu.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Image failed to upload.");
            request.getRequestDispatcher("registrarImagen.jsp").forward(request, response);
        }
    }
}   