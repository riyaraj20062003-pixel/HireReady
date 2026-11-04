package com.hireready.controller;

import com.hireready.dao.ResumeDAO;
import com.hireready.model.Resume;
import com.hireready.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for rendering the User Dashboard.
 * Displays user welcome, resume statistics, and the user's resumes.
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    private final ResumeDAO resumeDAO = new ResumeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Prevent browser caching to ensure fresh data is always displayed on dashboard
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        List<Resume> userResumes = resumeDAO.getResumesByUserId(user.getUserId());
        int totalResumes = resumeDAO.countResumesByUser(user.getUserId());

        request.setAttribute("resumes", userResumes);
        request.setAttribute("resumeCount", totalResumes);

        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}
