package com.hireready.dao;

import com.hireready.model.*;
import com.hireready.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Resume and its related collections.
 * Uses atomic transactions for saving and updating resumes.
 */
public class ResumeDAO {

    /**
     * Creates a new resume and its child records inside a database transaction.
     * @param resume Complete resume object.
     * @return generated resumeId or -1 if failed.
     */
    public int createResume(Resume resume) {
        Connection conn = null;
        PreparedStatement psResume = null;
        ResultSet rsKey = null;

        String insertResumeSql = "INSERT INTO resumes (user_id, resume_title, full_name, email, phone, address, linkedin, github, summary, career_objective) " +
                                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            psResume = conn.prepareStatement(insertResumeSql, Statement.RETURN_GENERATED_KEYS);
            psResume.setInt(1, resume.getUserId());
            psResume.setString(2, resume.getTitle());
            psResume.setString(3, resume.getFullName());
            psResume.setString(4, resume.getEmail());
            psResume.setString(5, resume.getPhone());
            psResume.setString(6, resume.getAddress());
            psResume.setString(7, resume.getLinkedin());
            psResume.setString(8, resume.getGithub());
            psResume.setString(9, resume.getSummary());
            psResume.setString(10, resume.getCareerObjective());

            int affected = psResume.executeUpdate();
            if (affected == 0) {
                conn.rollback();
                return -1;
            }

            rsKey = psResume.getGeneratedKeys();
            int resumeId = -1;
            if (rsKey.next()) {
                resumeId = rsKey.getInt(1);
                resume.setResumeId(resumeId);
            } else {
                conn.rollback();
                return -1;
            }

            // Insert child records
            insertEducationList(conn, resume.getUserId(), resumeId, resume.getEducationList());
            insertSkillList(conn, resume.getUserId(), resumeId, resume.getSkillList());
            insertExperienceList(conn, resume.getUserId(), resume.getExperienceList());
            insertProjectList(conn, resume.getUserId(), resumeId, resume.getProjectList());
            insertCertificationList(conn, resume.getUserId(), resume.getCertificationList());
            insertAchievementList(conn, resume.getUserId(), resume.getAchievementList());

            conn.commit();
            return resumeId;
        } catch (SQLException e) {
            System.err.println("Error creating resume: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            return -1;
        } finally {
            DBConnection.close(rsKey, psResume, conn);
        }
    }

    /**
     * Updates an existing resume and re-inserts its child collections inside a transaction.
     * Enforces that the resume belongs to the given user (unless userId is 0 for admin).
     */
    public boolean updateResume(Resume resume) {
        Connection conn = null;
        PreparedStatement psResume = null;

        String updateResumeSql = "UPDATE resumes SET resume_title=?, full_name=?, email=?, phone=?, address=?, linkedin=?, github=?, summary=?, career_objective=?, updated_at=CURRENT_TIMESTAMP " +
                                 "WHERE resume_id=? AND (user_id=? OR ?=0)";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            psResume = conn.prepareStatement(updateResumeSql);
            psResume.setString(1, resume.getTitle());
            psResume.setString(2, resume.getFullName());
            psResume.setString(3, resume.getEmail());
            psResume.setString(4, resume.getPhone());
            psResume.setString(5, resume.getAddress());
            psResume.setString(6, resume.getLinkedin());
            psResume.setString(7, resume.getGithub());
            psResume.setString(8, resume.getSummary());
            psResume.setString(9, resume.getCareerObjective());
            psResume.setInt(10, resume.getResumeId());
            psResume.setInt(11, resume.getUserId());
            psResume.setInt(12, resume.getUserId()); // Allows 0 for admin

            int rows = psResume.executeUpdate();
            if (rows == 0) {
                conn.rollback();
                return false;
            }

            int resumeId = resume.getResumeId();

            // Clear old child records and re-insert updated ones
            deleteChildRecords(conn, resumeId);

            insertEducationList(conn, resume.getUserId(), resumeId, resume.getEducationList());
            insertSkillList(conn, resume.getUserId(), resumeId, resume.getSkillList());
            insertExperienceList(conn, resume.getUserId(), resume.getExperienceList());
            insertProjectList(conn, resume.getUserId(), resumeId, resume.getProjectList());
            insertCertificationList(conn, resume.getUserId(), resume.getCertificationList());
            insertAchievementList(conn, resume.getUserId(), resume.getAchievementList());

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating resume: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            DBConnection.close(psResume, conn);
        }
    }

    /**
     * Deletes a resume while strictly enforcing user ownership.
     */
    public boolean deleteResume(int resumeId, int userId) {
        String sql = "DELETE FROM resumes WHERE resume_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, resumeId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting resume: " + e.getMessage());
            return false;
        } finally {
            DBConnection.close(ps, conn);
        }
    }


    /**
     * Retrieves a resume by ID with all its child records.
     */
    public Resume getResumeById(int resumeId) {
        String sql = "SELECT * FROM resumes WHERE resume_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, resumeId);
            rs = ps.executeQuery();

            if (rs.next()) {
                Resume r = mapResumeHeader(rs);
                loadChildCollections(conn, r);
                return r;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching resume by id: " + e.getMessage());
        } finally {
            DBConnection.close(rs, ps, conn);
        }
        return null;
    }

    /**
     * Retrieves a resume by ID only if it belongs to the given user ID.
     * Prevents URL ID manipulation security issue.
     */
    public Resume getResumeByIdAndUser(int resumeId, int userId) {
        String sql = "SELECT * FROM resumes WHERE resume_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, resumeId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                Resume r = mapResumeHeader(rs);
                loadChildCollections(conn, r);
                return r;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching resume by id and user: " + e.getMessage());
        } finally {
            DBConnection.close(rs, ps, conn);
        }
        return null;
    }

    /**
     * Returns a list of all resumes belonging to a user (header information only for list/dashboard view).
     */
    public List<Resume> getResumesByUserId(int userId) {
        List<Resume> list = new ArrayList<>();
        String sql = "SELECT * FROM resumes WHERE user_id = ? ORDER BY updated_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResumeHeader(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching resumes by user: " + e.getMessage());
        } finally {
            DBConnection.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * Retrieves all resumes across all users (for Admin dashboard).
     */
    public List<Resume> getAllResumes() {
        List<Resume> list = new ArrayList<>();
        String sql = "SELECT * FROM resumes ORDER BY updated_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResumeHeader(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all resumes: " + e.getMessage());
        } finally {
            DBConnection.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * Retrieves all resumes with their education and skills loaded (for Job Eligibility filtering).
     */
    public List<Resume> getAllResumesWithDetails() {
        List<Resume> list = new ArrayList<>();
        String sql = "SELECT * FROM resumes ORDER BY updated_at DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Resume r = mapResumeHeader(rs);
                loadFilterSummaryCollections(conn, r);
                list.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching resumes with details: " + e.getMessage());
        } finally {
            DBConnection.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * Filters resumes using dynamic parameterized SQL query based on eligibility criteria.
     */
    public List<Resume> filterResumes(FilterCriteria criteria) {
        List<Resume> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT DISTINCT r.* FROM resumes r ");
        List<Object> params = new ArrayList<>();

        if (criteria != null && !criteria.isEmpty()) {
            sql.append("WHERE 1=1 ");

            if (criteria.getMinCgpa() != null) {
                sql.append("AND r.resume_id IN (SELECT DISTINCT e.resume_id FROM education e WHERE e.cgpa >= ?) ");
                params.add(criteria.getMinCgpa());
            }

            if (criteria.getDegree() != null && !criteria.getDegree().trim().isEmpty()) {
                sql.append("AND r.resume_id IN (SELECT DISTINCT e.resume_id FROM education e WHERE LOWER(e.degree) LIKE ?) ");
                params.add("%" + criteria.getDegree().trim().toLowerCase() + "%");
            }

            if (criteria.getSkill() != null && !criteria.getSkill().trim().isEmpty()) {
                sql.append("AND r.resume_id IN (SELECT DISTINCT s.resume_id FROM skills s WHERE LOWER(s.skill_name) LIKE ? OR LOWER(s.skill_level) LIKE ?) ");
                String skillPattern = "%" + criteria.getSkill().trim().toLowerCase() + "%";
                params.add(skillPattern);
                params.add(skillPattern);
            }

            if (criteria.getPassingYear() != null && criteria.getPassingYear() > 0) {
                sql.append("AND r.resume_id IN (SELECT DISTINCT e.resume_id FROM education e WHERE e.end_year = ?) ");
                params.add(criteria.getPassingYear());
            }

            if (criteria.getKeyword() != null && !criteria.getKeyword().trim().isEmpty()) {
                sql.append("AND (LOWER(r.resume_title) LIKE ? OR LOWER(r.summary) LIKE ?) ");
                String kwPattern = "%" + criteria.getKeyword().trim().toLowerCase() + "%";
                params.add(kwPattern);
                params.add(kwPattern);
            }
        }

        sql.append("ORDER BY r.updated_at DESC");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                Resume r = mapResumeHeader(rs);
                // Also load education and skills for summary display in filter results
                loadFilterSummaryCollections(conn, r);
                list.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error executing resume filter: " + e.getMessage());
        } finally {
            DBConnection.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * Counts total resumes in system.
     */
    public int countResumes() {
        String sql = "SELECT COUNT(*) FROM resumes";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting resumes: " + e.getMessage());
        } finally {
            DBConnection.close(rs, ps, conn);
        }
        return 0;
    }

    /**
     * Counts total resumes for a specific user.
     */
    public int countResumesByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM resumes WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting user resumes: " + e.getMessage());
        } finally {
            DBConnection.close(rs, ps, conn);
        }
        return 0;
    }

    // ==========================================================
    // Internal Helper Methods
    // ==========================================================

    private Resume mapResumeHeader(ResultSet rs) throws SQLException {
        Resume r = new Resume();
        r.setResumeId(rs.getInt("resume_id"));
        r.setUserId(rs.getInt("user_id"));
        r.setTitle(rs.getString("resume_title"));
        r.setSummary(rs.getString("summary"));
        r.setCareerObjective(rs.getString("career_objective"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        r.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Map contact and personal details safely
        String fn = rs.getString("full_name");
        r.setFullName(fn != null ? fn : "");

        String em = rs.getString("email");
        r.setEmail(em != null ? em : "");

        String ph = rs.getString("phone");
        r.setPhone(ph != null ? ph : "");

        String addr = rs.getString("address");
        r.setAddress(addr != null ? addr : "");

        String li = rs.getString("linkedin");
        r.setLinkedin(li != null ? li : "");

        String gh = rs.getString("github");
        r.setGithub(gh != null ? gh : "");
        
        return r;
    }

    private void loadChildCollections(Connection conn, Resume resume) throws SQLException {
        // 1. Education
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM education WHERE resume_id = ? ORDER BY end_year DESC")) {
            ps.setInt(1, resume.getResumeId());
            try (ResultSet rs = ps.executeQuery()) {
                List<Education> eduList = new ArrayList<>();
                while (rs.next()) {
                    eduList.add(new Education(
                        rs.getInt("education_id"),
                        rs.getInt("user_id"), // still passing user_id to constructor just in case
                        rs.getString("degree"),
                        rs.getString("institution"),
                        rs.getInt("end_year"),
                        rs.getDouble("cgpa")
                    ));
                }
                resume.setEducationList(eduList);
            }
        }

        // 2. Skills
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM skills WHERE resume_id = ? ORDER BY skill_id ASC")) {
            ps.setInt(1, resume.getResumeId());
            try (ResultSet rs = ps.executeQuery()) {
                List<Skill> skillList = new ArrayList<>();
                while (rs.next()) {
                    skillList.add(new Skill(
                        rs.getInt("skill_id"),
                        rs.getInt("user_id"),
                        rs.getString("skill_level"),
                        rs.getString("skill_name")
                    ));
                }
                resume.setSkillList(skillList);
            }
        }

        // 3. Experience (Table doesn't exist)
        
        // 4. Projects
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM projects WHERE resume_id = ? ORDER BY project_id ASC")) {
            ps.setInt(1, resume.getResumeId());
            try (ResultSet rs = ps.executeQuery()) {
                List<Project> projList = new ArrayList<>();
                while (rs.next()) {
                    projList.add(new Project(
                        rs.getInt("project_id"),
                        rs.getInt("user_id"),
                        rs.getString("project_name"),
                        rs.getString("technologies"),
                        rs.getString("description"),
                        rs.getString("project_url")
                    ));
                }
                resume.setProjectList(projList);
            }
        }

        // 5. Certifications (Table doesn't exist)
        
        // 6. Achievements (Table doesn't exist)
    }

    private void loadFilterSummaryCollections(Connection conn, Resume resume) throws SQLException {
        // Load Education
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM education WHERE resume_id = ? ORDER BY end_year DESC")) {
            ps.setInt(1, resume.getResumeId());
            try (ResultSet rs = ps.executeQuery()) {
                List<Education> eduList = new ArrayList<>();
                while (rs.next()) {
                    eduList.add(new Education(
                        rs.getInt("education_id"),
                        rs.getInt("user_id"),
                        rs.getString("degree"),
                        rs.getString("institution"),
                        rs.getInt("end_year"),
                        rs.getDouble("cgpa")
                    ));
                }
                resume.setEducationList(eduList);
            }
        }

        // Load Skills
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM skills WHERE resume_id = ? ORDER BY skill_id ASC")) {
            ps.setInt(1, resume.getResumeId());
            try (ResultSet rs = ps.executeQuery()) {
                List<Skill> skillList = new ArrayList<>();
                while (rs.next()) {
                    skillList.add(new Skill(
                        rs.getInt("skill_id"),
                        rs.getInt("user_id"),
                        rs.getString("skill_level"),
                        rs.getString("skill_name")
                    ));
                }
                resume.setSkillList(skillList);
            }
        }
    }

    private void deleteChildRecords(Connection conn, int resumeId) throws SQLException {
        String[] tables = {"education", "skills", "projects"};
        for (String table : tables) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM " + table + " WHERE resume_id = ?")) {
                ps.setInt(1, resumeId);
                ps.executeUpdate();
            }
        }
    }

    private void insertEducationList(Connection conn, int userId, int resumeId, List<Education> list) throws SQLException {
        if (list == null || list.isEmpty()) return;
        String sql = "INSERT INTO education (user_id, resume_id, degree, institution, end_year, cgpa) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Education e : list) {
                if (e.getDegree() != null && !e.getDegree().trim().isEmpty()) {
                    ps.setInt(1, userId);
                    ps.setInt(2, resumeId);
                    ps.setString(3, e.getDegree().trim());
                    ps.setString(4, e.getInstitution() != null ? e.getInstitution().trim() : "");
                    ps.setInt(5, e.getPassingYear());
                    ps.setDouble(6, e.getCgpaOrPercentage());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }

    private void insertSkillList(Connection conn, int userId, int resumeId, List<Skill> list) throws SQLException {
        if (list == null || list.isEmpty()) return;
        String sql = "INSERT INTO skills (user_id, resume_id, skill_level, skill_name) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Skill s : list) {
                if (s.getSkillNames() != null && !s.getSkillNames().trim().isEmpty()) {
                    ps.setInt(1, userId);
                    ps.setInt(2, resumeId);
                    ps.setString(3, s.getSkillCategory() != null ? s.getSkillCategory().trim() : "Technical Skills");
                    ps.setString(4, s.getSkillNames().trim());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }

    private void insertExperienceList(Connection conn, int userId, List<Experience> list) throws SQLException {
        // Experience table doesn't exist in hire_ready database. No-op.
    }

    private void insertProjectList(Connection conn, int userId, int resumeId, List<Project> list) throws SQLException {
        if (list == null || list.isEmpty()) return;
        String sql = "INSERT INTO projects (user_id, resume_id, project_name, technologies, description, project_url) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Project p : list) {
                if (p.getTitle() != null && !p.getTitle().trim().isEmpty()) {
                    ps.setInt(1, userId);
                    ps.setInt(2, resumeId);
                    ps.setString(3, p.getTitle().trim());
                    ps.setString(4, p.getTechnologies() != null ? p.getTechnologies().trim() : "");
                    ps.setString(5, p.getDescription() != null ? p.getDescription().trim() : "");
                    ps.setString(6, p.getProjectLink() != null ? p.getProjectLink().trim() : "");
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }

    private void insertCertificationList(Connection conn, int userId, List<Certification> list) throws SQLException {
        // Certifications table doesn't exist in hire_ready database. No-op.
    }

    private void insertAchievementList(Connection conn, int userId, List<Achievement> list) throws SQLException {
        // Achievements table doesn't exist in hire_ready database. No-op.
    }
}
