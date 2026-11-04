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
 * Controller for listing all resumes belonging to the currently logged in user.
 */
@WebServlet(name = "ResumeListServlet", urlPatterns = {"/resumes"})
public class ResumeListServlet extends HttpServlet {

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

        List<Resume> resumes = resumeDAO.getResumesByUserId(user.getUserId());
        request.setAttribute("resumes", resumes);
        request.getRequestDispatcher("/resume-list.jsp").forward(request, response);
    }
}
