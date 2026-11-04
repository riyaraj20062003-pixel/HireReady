package com.hireready.controller;

import com.hireready.dao.UserDAO;
import com.hireready.model.User;
import com.hireready.util.PasswordUtil;
import com.hireready.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller handling user login and session initialization.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // If user already logged in, redirect straight to dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        request.setAttribute("email", email);

        if (ValidationUtil.isNullOrEmpty(email) || ValidationUtil.isNullOrEmpty(password)) {
            request.setAttribute("errorMessage", "Please enter both your email and password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        User user = userDAO.findByEmail(email);

        if (user == null || !PasswordUtil.verifyPassword(password, user.getPassword())) {
            request.setAttribute("errorMessage", "Invalid email address or password. Please try again.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Prevent password from lingering in session
        user.setPassword(null);

        // Session-based authentication
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        session.setAttribute("userName", user.getFullName());
        session.setAttribute("userId", user.getUserId());

        // Redirect to user dashboard
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}
