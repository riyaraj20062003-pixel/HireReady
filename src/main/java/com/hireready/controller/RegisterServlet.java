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
 * Controller handling user registration requests.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Preserve input values on error
        request.setAttribute("fullName", fullName);
        request.setAttribute("email", email);

        // 1. Validate required fields
        if (ValidationUtil.isNullOrEmpty(fullName) || 
            ValidationUtil.isNullOrEmpty(email) || 
            ValidationUtil.isNullOrEmpty(password) || 
            ValidationUtil.isNullOrEmpty(confirmPassword)) {
            
            request.setAttribute("errorMessage", "All fields are required. Please fill in all fields.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 2. Validate email format
        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("errorMessage", "Please provide a valid email address (e.g., user@example.com).");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 3. Validate password length
        if (!ValidationUtil.isValidPassword(password)) {
            request.setAttribute("errorMessage", "Password must be at least 6 characters long.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 4. Validate password confirmation
        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Password and Confirm Password do not match.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 5. Prevent duplicate email registration
        if (userDAO.isEmailRegistered(email)) {
            request.setAttribute("errorMessage", "An account with this email address already exists. Please log in or use another email.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 6. Hash password securely with BCrypt
        String hashedPassword = PasswordUtil.hashPassword(password);
        User newUser = new User(fullName.trim(), email.trim().toLowerCase(), hashedPassword);

        int generatedId = userDAO.register(newUser);
        if (generatedId > 0) {
            HttpSession session = request.getSession(true);
            session.setAttribute("successMessage", "Account created successfully! You can now log in.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            request.setAttribute("errorMessage", "An unexpected database error occurred while creating your account. Please try again.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
