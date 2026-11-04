package com.hireready.controller;

import com.hireready.dao.ResumeDAO;
import com.hireready.model.User;
import com.hireready.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller handling Resume deletion.
 * Strictly verifies ownership before deleting, preventing unauthorized deletion.
 */
@WebServlet(name = "ResumeDeleteServlet", urlPatterns = {"/resume/delete"})
public class ResumeDeleteServlet extends HttpServlet {

    private final ResumeDAO resumeDAO = new ResumeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        performDelete(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        performDelete(request, response);
    }

    private void performDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int resumeId = ValidationUtil.parseInt(request.getParameter("id"), 0);
        if (resumeId <= 0) {
            session.setAttribute("errorMessage", "Invalid resume ID.");
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // Only delete if this resume belongs to the current user
        boolean deleted = resumeDAO.deleteResume(resumeId, user.getUserId());

        if (deleted) {
            session.setAttribute("successMessage", "Resume deleted successfully.");
        } else {
            session.setAttribute("errorMessage", "Unable to delete resume. You may not have permission to delete this record.");
        }

        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}
