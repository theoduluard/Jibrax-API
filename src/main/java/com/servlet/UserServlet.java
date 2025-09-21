package com.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dao.implementation.UserDAO;
import com.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(name="adduser", urlPatterns={"/UserServlet"})
@MultipartConfig
public class UserServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(UserServlet.class.getName());

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("postgresql");

    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/html");

        User user = new User();
        user.setFirstname(request.getParameter("firstname"));
        user.setLastname(request.getParameter("lastname"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        user.setUsername(request.getParameter("username"));
        user.setLastLogin(LocalDateTime.now());
        user.setActive(true);

        Part filePart = request.getPart("image");
        if (filePart != null && filePart.getSize() > 0) {
            try (InputStream inputStream = filePart.getInputStream()) {
                byte[] imageBytes = inputStream.readAllBytes();
                user.setImage(imageBytes);
            }
        }

        UserDAO userDao = new UserDAO();
        userDao.setEntityManager(entityManager);
        userDao.save(user);

        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body>");
            out.println("<h1>Récapitulatif des informations du User ajouté</h1>");
            out.println("<ul>");
            out.println("<li>Nom: " + user.getLastname() + "</li>");
            out.println("<li>Prénom: " + user.getFirstname() + "</li>");
            out.println("<li>Username: " + user.getUsername() + "</li>");
            out.println("<li>Email: " + user.getEmail() + "</li>");
            out.println("<li>Password: " + user.getPassword() + "</li>");

            if (user.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(user.getImage());
                out.println("<li>Image:<br><img src='data:image/png;base64," + base64Image + "' width='500'/></li>");
            }

            out.println("</ul>");
            out.println("</body></html>");
        }
        catch (Exception e) {
            log.log(Level.WARNING, String.format("Error while writing data on web console: %s", e));
        }
    }
}
