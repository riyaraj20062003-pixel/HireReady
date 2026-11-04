package com.hireready.controller;

import com.hireready.dao.ResumeDAO;
import com.hireready.model.FilterCriteria;
import com.hireready.model.Resume;
import com.hireready.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Candidate Resume Search & Eligibility Filtering.
 * Supports filtering by Minimum CGPA, Degree, Skills, Graduation Year, and Keywords.
 */
@WebServlet(name = "ResumeFilterServlet", urlPatterns = {"/filter"})
public class ResumeFilterServlet extends HttpServlet {

    private final ResumeDAO resumeDAO = new ResumeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processFilter(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processFilter(request, response);
    }

    private void processFilter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String minCgpaStr = request.getParameter("minCgpa");
        String degree = request.getParameter("degree");
        String skill = request.getParameter("skill");
        String passingYearStr = request.getParameter("passingYear");
        String keyword = request.getParameter("keyword");

        FilterCriteria criteria = new FilterCriteria();

        if (!ValidationUtil.isNullOrEmpty(minCgpaStr)) {
            double cgpa = ValidationUtil.parseDouble(minCgpaStr, -1);
            if (cgpa >= 0) {
                criteria.setMinCgpa(cgpa);
            }
        }

        if (!ValidationUtil.isNullOrEmpty(degree)) {
            criteria.setDegree(degree.trim());
        }

        if (!ValidationUtil.isNullOrEmpty(skill)) {
            criteria.setSkill(skill.trim());
        }

        if (!ValidationUtil.isNullOrEmpty(passingYearStr)) {
            int year = ValidationUtil.parseInt(passingYearStr, 0);
            if (year > 1900 && year < 2100) {
                criteria.setPassingYear(year);
            }
        }

        if (!ValidationUtil.isNullOrEmpty(keyword)) {
            criteria.setKeyword(keyword.trim());
        }

        List<Resume> results = resumeDAO.filterResumes(criteria);

        request.setAttribute("criteria", criteria);
        request.setAttribute("results", results);
        request.setAttribute("resultCount", results.size());

        request.getRequestDispatcher("/filter.jsp").forward(request, response);
    }
}
