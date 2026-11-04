package com.hireready.controller;

import com.hireready.dao.ResumeDAO;
import com.hireready.model.Resume;
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
 * Controller for viewing a full professional resume preview.
 * Allows the owner user to view the resume.
 */
@WebServlet(name = "ResumeViewServlet", urlPatterns = {"/resume/view"})
public class ResumeViewServlet extends HttpServlet {

    private final ResumeDAO resumeDAO = new ResumeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Prevent browser caching to ensure fresh data is always displayed after an edit
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        int resumeId = ValidationUtil.parseInt(request.getParameter("id"), 0);
        if (resumeId <= 0) {
            if (session != null) {
                session.setAttribute("errorMessage", "Invalid resume ID.");
            }
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // Regular user can view their own resume, or if viewed from eligibility filter
        Resume resume = resumeDAO.getResumeByIdAndUser(resumeId, user.getUserId());
        if (resume == null) {
            // Also allow reading by ID for resume review from the candidate search/eligibility filter
            resume = resumeDAO.getResumeById(resumeId);
        }

        if (resume == null) {
            if (session != null) {
                session.setAttribute("errorMessage", "Resume not found or you do not have permission to view it.");
            }
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.setAttribute("resume", resume);
        request.getRequestDispatcher("/resume-view.jsp").forward(request, response);
    }
}
