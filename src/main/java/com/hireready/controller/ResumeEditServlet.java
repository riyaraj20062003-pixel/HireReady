package com.hireready.controller;

import com.hireready.dao.ResumeDAO;
import com.hireready.model.*;
import com.hireready.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller handling Resume editing and updating.
 * Strictly verifies ownership so one user cannot edit another's resume.
 */
@WebServlet(name = "ResumeEditServlet", urlPatterns = {"/resume/edit"})
public class ResumeEditServlet extends HttpServlet {

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

        int resumeId = ValidationUtil.parseInt(request.getParameter("id"), 0);
        if (resumeId <= 0) {
            session.setAttribute("errorMessage", "Invalid resume ID provided.");
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // Enforce ownership: user can ONLY access their own resume
        Resume resume = resumeDAO.getResumeByIdAndUser(resumeId, user.getUserId());
        if (resume == null) {
            session.setAttribute("errorMessage", "Resume not found or you do not have permission to access it.");
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.setAttribute("mode", "edit");
        request.setAttribute("resume", resume);
        request.getRequestDispatcher("/resume-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int resumeId = ValidationUtil.parseInt(request.getParameter("resumeId"), 0);
        if (resumeId <= 0) {
            session.setAttribute("errorMessage", "Invalid resume ID provided.");
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // Verify that resume exists and belongs to current user
        Resume existing = resumeDAO.getResumeByIdAndUser(resumeId, user.getUserId());
        if (existing == null) {
            session.setAttribute("errorMessage", "Resume not found or you do not have permission to modify it.");
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // 1. Extract header fields
        String title = request.getParameter("title");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String linkedin = request.getParameter("linkedin");
        String github = request.getParameter("github");
        String summary = request.getParameter("summary");
        String careerObjective = request.getParameter("careerObjective");

        if (ValidationUtil.isNullOrEmpty(title) || 
            ValidationUtil.isNullOrEmpty(fullName) || 
            ValidationUtil.isNullOrEmpty(email) || 
            ValidationUtil.isNullOrEmpty(phone)) {
            
            request.setAttribute("errorMessage", "Resume Title, Full Name, Email, and Phone Number are required.");
            request.setAttribute("mode", "edit");
            existing.setTitle(title);
            existing.setFullName(fullName);
            existing.setEmail(email);
            existing.setPhone(phone);
            request.setAttribute("resume", existing);
            request.getRequestDispatcher("/resume-form.jsp").forward(request, response);
            return;
        }

        existing.setTitle(title != null ? title.trim() : "");
        existing.setFullName(fullName != null ? fullName.trim() : "");
        existing.setEmail(email != null ? email.trim() : "");
        existing.setPhone(phone != null ? phone.trim() : "");
        existing.setAddress(address != null ? address.trim() : "");
        existing.setLinkedin(linkedin != null ? linkedin.trim() : "");
        existing.setGithub(github != null ? github.trim() : "");
        existing.setSummary(summary != null ? summary.trim() : "");
        existing.setCareerObjective(careerObjective != null ? careerObjective.trim() : "");

        // 2. Extract Education items
        String[] degrees = request.getParameterValues("degree[]");
        String[] institutions = request.getParameterValues("institution[]");
        String[] passingYears = request.getParameterValues("passingYear[]");
        String[] cgpas = request.getParameterValues("cgpa[]");

        if (degrees != null) {
            List<Education> eduList = new ArrayList<>();
            for (int i = 0; i < degrees.length; i++) {
                if (!ValidationUtil.isNullOrEmpty(degrees[i])) {
                    String inst = (institutions != null && i < institutions.length) ? institutions[i] : "";
                    int yr = (passingYears != null && i < passingYears.length) ? ValidationUtil.parseInt(passingYears[i], 0) : 0;
                    double cg = (cgpas != null && i < cgpas.length) ? ValidationUtil.parseDouble(cgpas[i], 0.0) : 0.0;
                    eduList.add(new Education(degrees[i].trim(), inst.trim(), yr, cg));
                }
            }
            existing.setEducationList(eduList);
        }

        // 3. Extract Skills items
        String[] skillCategories = request.getParameterValues("skillCategory[]");
        String[] skillNames = request.getParameterValues("skillNames[]");

        if (skillNames != null) {
            List<Skill> skillList = new ArrayList<>();
            for (int i = 0; i < skillNames.length; i++) {
                if (!ValidationUtil.isNullOrEmpty(skillNames[i])) {
                    String cat = (skillCategories != null && i < skillCategories.length && !ValidationUtil.isNullOrEmpty(skillCategories[i])) 
                                 ? skillCategories[i].trim() : "Technical Skills";
                    skillList.add(new Skill(cat, skillNames[i].trim()));
                }
            }
            existing.setSkillList(skillList);
        }

        // 4. Extract Experience items
        String[] companies = request.getParameterValues("company[]");
        String[] jobRoles = request.getParameterValues("jobRole[]");
        String[] durations = request.getParameterValues("duration[]");
        String[] expDescriptions = request.getParameterValues("expDescription[]");

        if (companies != null) {
            List<Experience> expList = new ArrayList<>();
            for (int i = 0; i < companies.length; i++) {
                if (!ValidationUtil.isNullOrEmpty(companies[i])) {
                    String role = (jobRoles != null && i < jobRoles.length) ? jobRoles[i].trim() : "";
                    String dur = (durations != null && i < durations.length) ? durations[i].trim() : "";
                    String desc = (expDescriptions != null && i < expDescriptions.length) ? expDescriptions[i].trim() : "";
                    expList.add(new Experience(companies[i].trim(), role, dur, desc));
                }
            }
            existing.setExperienceList(expList);
        }

        // 5. Extract Project items
        String[] projectTitles = request.getParameterValues("projectTitle[]");
        String[] technologies = request.getParameterValues("technologies[]");
        String[] projectDescriptions = request.getParameterValues("projectDescription[]");
        String[] projectLinks = request.getParameterValues("projectLink[]");

        if (projectTitles != null) {
            List<Project> projectList = new ArrayList<>();
            for (int i = 0; i < projectTitles.length; i++) {
                if (!ValidationUtil.isNullOrEmpty(projectTitles[i])) {
                    String tech = (technologies != null && i < technologies.length) ? technologies[i].trim() : "";
                    String desc = (projectDescriptions != null && i < projectDescriptions.length) ? projectDescriptions[i].trim() : "";
                    String link = (projectLinks != null && i < projectLinks.length) ? projectLinks[i].trim() : "";
                    projectList.add(new Project(projectTitles[i].trim(), tech, desc, link));
                }
            }
            existing.setProjectList(projectList);
        }

        // 6. Extract Certification items
        String[] certNames = request.getParameterValues("certName[]");
        String[] issuingOrgs = request.getParameterValues("issuingOrg[]");
        String[] issueYears = request.getParameterValues("issueYear[]");

        if (certNames != null) {
            List<Certification> certList = new ArrayList<>();
            for (int i = 0; i < certNames.length; i++) {
                if (!ValidationUtil.isNullOrEmpty(certNames[i])) {
                    String org = (issuingOrgs != null && i < issuingOrgs.length) ? issuingOrgs[i].trim() : "";
                    int yr = (issueYears != null && i < issueYears.length) ? ValidationUtil.parseInt(issueYears[i], 0) : 0;
                    certList.add(new Certification(certNames[i].trim(), org, yr));
                }
            }
            existing.setCertificationList(certList);
        }

        // 7. Extract Achievement items
        String[] achievementTitles = request.getParameterValues("achievementTitle[]");
        String[] achievementDescriptions = request.getParameterValues("achievementDescription[]");

        if (achievementTitles != null) {
            List<Achievement> achList = new ArrayList<>();
            for (int i = 0; i < achievementTitles.length; i++) {
                if (!ValidationUtil.isNullOrEmpty(achievementTitles[i])) {
                    String desc = (achievementDescriptions != null && i < achievementDescriptions.length) ? achievementDescriptions[i].trim() : "";
                    achList.add(new Achievement(achievementTitles[i].trim(), desc));
                }
            }
            existing.setAchievementList(achList);
        }

        // Perform transactional update
        boolean updated = resumeDAO.updateResume(existing);

        if (updated) {
            session.setAttribute("successMessage", "Resume '" + existing.getTitle() + "' updated successfully!");
            response.sendRedirect(request.getContextPath() + "/resume/view?id=" + resumeId);
        } else {
            request.setAttribute("errorMessage", "Failed to update resume. Please try again.");
            request.setAttribute("mode", "edit");
            request.setAttribute("resume", existing);
            request.getRequestDispatcher("/resume-form.jsp").forward(request, response);
        }
    }
}
