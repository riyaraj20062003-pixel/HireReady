package com.hireready.controller;

import com.hireready.dao.JobDAO;
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
import java.util.*;

/**
 * Controller handling Job Eligibility Filtering.
 * Evaluates candidate resumes against a selected Job's Minimum CGPA,
 * Required Skills, and Graduation Year.
 */
@WebServlet(name = "JobEligibilityServlet", urlPatterns = {"/job-eligibility"})
public class JobEligibilityServlet extends HttpServlet {

    private final JobDAO jobDAO = new JobDAO();
    private final ResumeDAO resumeDAO = new ResumeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processEligibility(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processEligibility(request, response);
    }

    private void processEligibility(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Optional authentication check: guest or logged-in user/admin can use the feature
        List<Job> allJobs = jobDAO.getAllJobs();
        request.setAttribute("jobs", allJobs);

        String jobIdParam = request.getParameter("jobId");
        int selectedJobId = ValidationUtil.parseInt(jobIdParam, 0);

        // Default to first job if none chosen, or keep 0
        if (selectedJobId == 0 && !allJobs.isEmpty() && jobIdParam != null) {
            selectedJobId = allJobs.get(0).getJobId();
        }

        Job selectedJob = null;
        if (selectedJobId > 0) {
            selectedJob = jobDAO.getJobById(selectedJobId);
        }

        request.setAttribute("selectedJob", selectedJob);

        if (selectedJob != null) {
            List<Resume> allResumes = resumeDAO.getAllResumesWithDetails();
            List<ResumeEligibility> evaluatedList = new ArrayList<>();
            int eligibleCount = 0;

            for (Resume resume : allResumes) {
                ResumeEligibility eval = evaluateResumeAgainstJob(resume, selectedJob);
                if (eval.isOverallEligible()) {
                    eligibleCount++;
                }
                evaluatedList.add(eval);
            }

            request.setAttribute("evaluations", evaluatedList);
            request.setAttribute("eligibleCount", eligibleCount);
            request.setAttribute("totalEvaluated", evaluatedList.size());
        }

        request.getRequestDispatcher("/job-eligibility.jsp").forward(request, response);
    }

    /**
     * Evaluates a single resume strictly using its own child collections (education, skills).
     */
    private ResumeEligibility evaluateResumeAgainstJob(Resume resume, Job job) {
        ResumeEligibility eval = new ResumeEligibility();
        eval.setResume(resume);

        // 1. CGPA & Graduation Year evaluation from resume's education list
        double highestCgpa = 0.0;
        int candidateGradYear = 0;
        boolean hasEducation = false;

        if (resume.getEducationList() != null && !resume.getEducationList().isEmpty()) {
            hasEducation = true;
            for (Education edu : resume.getEducationList()) {
                if (edu.getCgpaOrPercentage() > highestCgpa) {
                    highestCgpa = edu.getCgpaOrPercentage();
                }
                if (edu.getPassingYear() > candidateGradYear) {
                    candidateGradYear = edu.getPassingYear();
                }
            }
        }

        eval.setCandidateCgpa(highestCgpa);
        eval.setCandidateGraduationYear(candidateGradYear);

        // Criterion 1: Minimum CGPA
        boolean cgpaPassed = true;
        if (job.getMinimumCgpa() != null && job.getMinimumCgpa() > 0) {
            if (!hasEducation || highestCgpa < job.getMinimumCgpa()) {
                cgpaPassed = false;
            }
        }
        eval.setCgpaEligible(cgpaPassed);

        // Criterion 2: Graduation Year requirement (if defined for the job)
        boolean gradYearPassed = true;
        if (job.getGraduationYear() != null && job.getGraduationYear() > 0) {
            if (!hasEducation || candidateGradYear != job.getGraduationYear().intValue()) {
                gradYearPassed = false;
            }
        }
        eval.setGraduationYearEligible(gradYearPassed);

        // Criterion 3: Required Skills (all required job skills must be matched)
        Set<String> candidateSkillTokens = extractCandidateSkillTokens(resume);
        List<String> matchingSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();
        boolean skillsPassed = true;

        if (job.getRequiredSkills() != null && !job.getRequiredSkills().isEmpty()) {
            for (String requiredSkill : job.getRequiredSkills()) {
                String reqNormalized = requiredSkill.trim().toLowerCase();
                boolean matched = false;
                for (String candidateToken : candidateSkillTokens) {
                    if (candidateToken.equalsIgnoreCase(reqNormalized) 
                            || candidateToken.contains(reqNormalized) 
                            || reqNormalized.contains(candidateToken)) {
                        matched = true;
                        break;
                    }
                }
                if (matched) {
                    matchingSkills.add(requiredSkill);
                } else {
                    missingSkills.add(requiredSkill);
                    skillsPassed = false;
                }
            }
        }
        eval.setMatchingSkills(matchingSkills);
        eval.setMissingSkills(missingSkills);
        eval.setSkillsEligible(skillsPassed);

        // Overall eligibility: MUST satisfy all active criteria
        boolean overall = cgpaPassed && gradYearPassed && skillsPassed;
        eval.setOverallEligible(overall);

        return eval;
    }

    /**
     * Extracts comma-separated skill names and individual tokens from resume skills.
     */
    private Set<String> extractCandidateSkillTokens(Resume resume) {
        Set<String> tokens = new HashSet<>();
        if (resume.getSkillList() != null) {
            for (Skill sk : resume.getSkillList()) {
                if (sk.getSkillNames() != null) {
                    String[] parts = sk.getSkillNames().split("[,;/]");
                    for (String p : parts) {
                        String trimmed = p.trim().toLowerCase();
                        if (!trimmed.isEmpty()) {
                            tokens.add(trimmed);
                        }
                    }
                }
            }
        }
        return tokens;
    }
}
