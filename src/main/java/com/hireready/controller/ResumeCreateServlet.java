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
 * Controller handling Resume creation.
 */
@WebServlet(name = "ResumeCreateServlet", urlPatterns = {"/resume/create"})
public class ResumeCreateServlet extends HttpServlet {

    private final ResumeDAO resumeDAO = new ResumeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("mode", "create");
        request.setAttribute("resume", new Resume());
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

        // Basic validation
        if (ValidationUtil.isNullOrEmpty(title) || 
            ValidationUtil.isNullOrEmpty(fullName) || 
            ValidationUtil.isNullOrEmpty(email) || 
            ValidationUtil.isNullOrEmpty(phone)) {
            
            request.setAttribute("errorMessage", "Resume Title, Full Name, Email, and Phone Number are required.");
            request.setAttribute("mode", "create");
            request.getRequestDispatcher("/resume-form.jsp").forward(request, response);
            return;
        }

        Resume resume = new Resume();
        resume.setUserId(user.getUserId());
        resume.setTitle(title != null ? title.trim() : "");
        resume.setFullName(fullName != null ? fullName.trim() : "");
        resume.setEmail(email != null ? email.trim() : "");
        resume.setPhone(phone != null ? phone.trim() : "");
        resume.setAddress(address != null ? address.trim() : "");
        resume.setLinkedin(linkedin != null ? linkedin.trim() : "");
        resume.setGithub(github != null ? github.trim() : "");
        resume.setSummary(summary != null ? summary.trim() : "");
        resume.setCareerObjective(careerObjective != null ? careerObjective.trim() : "");

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
            resume.setEducationList(eduList);
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
            resume.setSkillList(skillList);
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
            resume.setExperienceList(expList);
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
            resume.setProjectList(projectList);
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
            resume.setCertificationList(certList);
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
            resume.setAchievementList(achList);
        }

        // Save resume in MySQL
        int generatedId = resumeDAO.createResume(resume);

        if (generatedId > 0) {
            HttpSession curSession = request.getSession(true);
            curSession.setAttribute("successMessage", "Resume '" + resume.getTitle() + "' created successfully!");
            response.sendRedirect(request.getContextPath() + "/resume/view?id=" + generatedId);
        } else {
            request.setAttribute("errorMessage", "Failed to save resume. Please check your details and try again.");
            request.setAttribute("mode", "create");
            request.setAttribute("resume", resume);
            request.getRequestDispatcher("/resume-form.jsp").forward(request, response);
        }
    }
}
