package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import com.dao.implementation.UserDAO;
import com.domain.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name="adduser",
    urlPatterns={"/AddUser"})
public class UserManager extends HttpServlet {
  public void doPost(HttpServletRequest request,
                     HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html");

    PrintWriter out = response.getWriter();
    User user = new User();
    user.setFirstname(request.getParameter("firstname"));
    user.setLastname(request.getParameter("lastname"));
    user.setEmail(request.getParameter("email"));
    user.setPassword(request.getParameter("password"));
    user.setImage(request.getParameter("image").getBytes());
    user.setUsername(request.getParameter("username"));
    user.setActive(true);
    LocalDateTime now = LocalDateTime.now();
    user.setLastLogin(now);
    user.setCreatedAt(now);
    user.setUpdatedAt(now);
    UserDAO userDao = new UserDAO();
    userDao.save(user);



    out.println("<HTML>\n<BODY>\n" +
                    "<H1>Recapitulatif des informations du User ajouter</H1>\n" +
                    "<UL>\n" +
                    " <LI>Nom: "
                    + request.getParameter("lastname") + "\n" +
                    " <LI>Prenom: "
                    + request.getParameter("firstname") + "\n" +
                    " <LI>Mot De Passe: "
                    + request.getParameter("password") + "\n" +
                    "</UL>\n" +
                    "</BODY></HTML>");
  }
}

