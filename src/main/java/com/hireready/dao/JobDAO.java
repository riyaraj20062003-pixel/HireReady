package com.hireready.dao;

import com.hireready.model.Job;
import com.hireready.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Job listings and requirements.
 */
public class JobDAO {

    /**
     * Retrieves all jobs ordered by job_id ascending.
     */
    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs ORDER BY job_id ASC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Job job = mapJob(rs);
                loadJobRequirements(conn, job);
                jobs.add(job);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all jobs: " + e.getMessage());
        } finally {
            DBConnection.close(rs, ps, conn);
        }
        return jobs;
    }

    /**
     * Retrieves a single job by its ID with requirements.
     */
    public Job getJobById(int jobId) {
        String sql = "SELECT * FROM jobs WHERE job_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, jobId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Job job = mapJob(rs);
                loadJobRequirements(conn, job);
                return job;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching job by id: " + e.getMessage());
        } finally {
            DBConnection.close(rs, ps, conn);
        }
        return null;
    }

    private Job mapJob(ResultSet rs) throws SQLException {
        Job job = new Job();
        job.setJobId(rs.getInt("job_id"));
        job.setCompanyName(rs.getString("company_name"));
        job.setJobTitle(rs.getString("job_title"));
        job.setJobDescription(rs.getString("job_description"));
        
        double cgpa = rs.getDouble("minimum_cgpa");
        if (!rs.wasNull()) {
            job.setMinimumCgpa(cgpa);
        }
        
        int exp = rs.getInt("minimum_experience");
        if (!rs.wasNull()) {
            job.setMinimumExperience(exp);
        }
        
        job.setMinimumDegree(rs.getString("minimum_degree"));
        
        int year = rs.getInt("graduation_year");
        if (!rs.wasNull()) {
            job.setGraduationYear(year);
        }
        
        job.setLocation(rs.getString("location"));
        job.setJobType(rs.getString("job_type"));
        job.setApplicationDeadline(rs.getDate("application_deadline"));
        job.setCreatedAt(rs.getTimestamp("created_at"));
        return job;
    }

    private void loadJobRequirements(Connection conn, Job job) throws SQLException {
        String sql = "SELECT skill_name FROM job_requirements WHERE job_id = ? ORDER BY requirement_id ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, job.getJobId());
            try (ResultSet rs = ps.executeQuery()) {
                List<String> skills = new ArrayList<>();
                while (rs.next()) {
                    skills.add(rs.getString("skill_name"));
                }
                job.setRequiredSkills(skills);
            }
        }
    }
}
