-- ==========================================================
-- HireReady: Online Resume Builder & Resume Management System
-- Database Schema Script
-- ==========================================================

DROP DATABASE IF EXISTS hireready_db;
CREATE DATABASE hireready_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hireready_db;

-- ----------------------------------------------------------
-- 1. Table: users
-- ----------------------------------------------------------
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- 2. Table: admins
-- ----------------------------------------------------------
CREATE TABLE admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- 3. Table: resumes
-- ----------------------------------------------------------
CREATE TABLE resumes (
    resume_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    address VARCHAR(255),
    linkedin VARCHAR(255),
    github VARCHAR(255),
    summary TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_resumes_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- 4. Table: education
-- ----------------------------------------------------------
CREATE TABLE education (
    education_id INT AUTO_INCREMENT PRIMARY KEY,
    resume_id INT NOT NULL,
    degree VARCHAR(100) NOT NULL,
    institution VARCHAR(200) NOT NULL,
    passing_year INT NOT NULL,
    cgpa_or_percentage DECIMAL(5, 2) NOT NULL,
    CONSTRAINT fk_education_resume FOREIGN KEY (resume_id) REFERENCES resumes(resume_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- 5. Table: skills
-- ----------------------------------------------------------
CREATE TABLE skills (
    skill_id INT AUTO_INCREMENT PRIMARY KEY,
    resume_id INT NOT NULL,
    skill_category VARCHAR(100) NOT NULL, -- e.g., 'Programming Languages', 'Technical Skills', 'Tools/Technologies'
    skill_names TEXT NOT NULL,
    CONSTRAINT fk_skills_resume FOREIGN KEY (resume_id) REFERENCES resumes(resume_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- 6. Table: experience
-- ----------------------------------------------------------
CREATE TABLE experience (
    experience_id INT AUTO_INCREMENT PRIMARY KEY,
    resume_id INT NOT NULL,
    company VARCHAR(150) NOT NULL,
    job_role VARCHAR(100) NOT NULL,
    duration VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT fk_experience_resume FOREIGN KEY (resume_id) REFERENCES resumes(resume_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- 7. Table: projects
-- ----------------------------------------------------------
CREATE TABLE projects (
    project_id INT AUTO_INCREMENT PRIMARY KEY,
    resume_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    technologies VARCHAR(255),
    description TEXT,
    project_link VARCHAR(255),
    CONSTRAINT fk_projects_resume FOREIGN KEY (resume_id) REFERENCES resumes(resume_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- 8. Table: certifications
-- ----------------------------------------------------------
CREATE TABLE certifications (
    cert_id INT AUTO_INCREMENT PRIMARY KEY,
    resume_id INT NOT NULL,
    cert_name VARCHAR(200) NOT NULL,
    issuing_org VARCHAR(150) NOT NULL,
    issue_year INT,
    CONSTRAINT fk_certifications_resume FOREIGN KEY (resume_id) REFERENCES resumes(resume_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- 9. Table: achievements
-- ----------------------------------------------------------
CREATE TABLE achievements (
    achievement_id INT AUTO_INCREMENT PRIMARY KEY,
    resume_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    CONSTRAINT fk_achievements_resume FOREIGN KEY (resume_id) REFERENCES resumes(resume_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==========================================================
-- Sample Data Insertion
-- ==========================================================

-- Default Admin Account (Username: admin, Password: Admin@123)
-- BCrypt hash for Admin@123: $2a$10$7Z/Y9kXwQ2nSgU1hJ3r7Le1U8iYyNqI6B/lSj.u9kFmH5V4rP1D.C
INSERT INTO admins (username, password, full_name, email)
VALUES ('admin', '$2a$10$7Z/Y9kXwQ2nSgU1hJ3r7Le1U8iYyNqI6B/lSj.u9kFmH5V4rP1D.C', 'System Administrator', 'admin@hireready.com');

-- Sample Users (Password: User@123)
-- BCrypt hash for User@123: $2a$10$n7/78b8lU0wY2j6F9qP8Ae4rP4U8iYyNqI6B/lSj.u9kFmH5V4rP2
INSERT INTO users (full_name, email, password)
VALUES 
('Rahul Sharma', 'rahul.sharma@example.com', '$2a$10$n7/78b8lU0wY2j6F9qP8Ae4rP4U8iYyNqI6B/lSj.u9kFmH5V4rP2'),
('Priya Patel', 'priya.patel@example.com', '$2a$10$n7/78b8lU0wY2j6F9qP8Ae4rP4U8iYyNqI6B/lSj.u9kFmH5V4rP2');

-- Sample Resume 1 for Rahul Sharma (Software Engineer)
INSERT INTO resumes (user_id, title, full_name, email, phone, address, linkedin, github, summary)
VALUES (
    1, 
    'Full Stack Java Developer', 
    'Rahul Sharma', 
    'rahul.sharma@example.com', 
    '+91 9876543210', 
    'Bengaluru, Karnataka, India', 
    'https://linkedin.com/in/rahulsharma', 
    'https://github.com/rahulsharma', 
    'Enthusiastic and results-driven Computer Science graduate with hands-on expertise in Java, Spring, JSP/Servlets, and MySQL. Passionate about designing robust web applications, optimizing database performance, and building clean user experiences.'
);

-- Education for Resume 1
INSERT INTO education (resume_id, degree, institution, passing_year, cgpa_or_percentage)
VALUES 
(1, 'B.Tech in Computer Science and Engineering', 'National Institute of Technology', 2024, 8.75),
(1, 'Higher Secondary Certificate (Class XII)', 'Delhi Public School', 2020, 91.40);

-- Skills for Resume 1
INSERT INTO skills (resume_id, skill_category, skill_names)
VALUES 
(1, 'Programming Languages', 'Java, Python, C++, SQL, JavaScript'),
(1, 'Technical Skills', 'Jakarta Servlets, JSP, JDBC, RESTful APIs, OOP, Data Structures & Algorithms'),
(1, 'Tools/Technologies', 'MySQL, Git, GitHub, Maven, Apache Tomcat, VS Code, Eclipse');

-- Experience for Resume 1
INSERT INTO experience (resume_id, company, job_role, duration, description)
VALUES 
(1, 'Infosys Ltd.', 'Software Developer Intern', 'Jan 2024 - Jun 2024', 'Assisted in building scalable backend services using Java and MySQL. Created dynamic JSP reporting modules and optimized SQL queries, improving response times by 25%.');

-- Projects for Resume 1
INSERT INTO projects (resume_id, title, technologies, description, project_link)
VALUES 
(1, 'HireReady Career Portal', 'Java Servlets, JSP, MySQL, JDBC, HTML5, CSS3', 'Architected and built a complete online resume creation and eligibility filtering system supporting dynamic PDF generation and admin moderation.', 'https://github.com/rahulsharma/hireready'),
(1, 'Inventory Control Dashboard', 'Java, JDBC, MySQL, Swing', 'Developed a desktop inventory tracking application with automated low-stock alerts and transaction log exports.', 'https://github.com/rahulsharma/inventory-system');

-- Certifications for Resume 1
INSERT INTO certifications (resume_id, cert_name, issuing_org, issue_year)
VALUES 
(1, 'Oracle Certified Professional: Java SE 11 Developer', 'Oracle', 2023),
(1, 'Database Foundations Specialist', 'Coursera / Stanford Online', 2022);

-- Achievements for Resume 1
INSERT INTO achievements (resume_id, title, description)
VALUES 
(1, 'Smart India Hackathon 2023 Finalist', 'Selected among top 30 teams out of 1,200 submissions for developing an automated education portal.'),
(1, 'CodeChef 4-Star Coder', 'Ranked in top 5% globally in monthly competitive programming contests.');

-- Sample Resume 2 for Priya Patel (Frontend & Web Developer)
INSERT INTO resumes (user_id, title, full_name, email, phone, address, linkedin, github, summary)
VALUES (
    2, 
    'Frontend & Web Specialist', 
    'Priya Patel', 
    'priya.patel@example.com', 
    '+91 9123456780', 
    'Pune, Maharashtra, India', 
    'https://linkedin.com/in/priyapatel', 
    'https://github.com/priyapatel', 
    'Creative web developer with strong frontend development foundations, responsive layout engineering, and relational database management knowledge. Committed to writing clean, accessible, and high-performance web code.'
);

-- Education for Resume 2
INSERT INTO education (resume_id, degree, institution, passing_year, cgpa_or_percentage)
VALUES 
(2, 'B.E. in Information Technology', 'Pune Institute of Computer Technology', 2023, 7.90);

-- Skills for Resume 2
INSERT INTO skills (resume_id, skill_category, skill_names)
VALUES 
(2, 'Programming Languages', 'JavaScript, HTML5, CSS3, Java, Python'),
(2, 'Technical Skills', 'Responsive Web Design, JSP, Servlets, CSS Flexbox/Grid, UI/UX Prototyping'),
(2, 'Tools/Technologies', 'Git, Figma, MySQL, Apache Tomcat, Postman');

-- Experience for Resume 2
INSERT INTO experience (resume_id, company, job_role, duration, description)
VALUES 
(2, 'TechSpark Solutions', 'Junior Web Developer', 'Jul 2023 - Present', 'Developing responsive interfaces, converting Figma wireframes into semantic HTML/CSS templates, and integrating with Java backend endpoints.');

-- Projects for Resume 2
INSERT INTO projects (resume_id, title, technologies, description, project_link)
VALUES 
(2, 'E-Commerce Showcase Portal', 'Java Servlets, JSP, MySQL, CSS3', 'Created a multi-category product catalog with shopping cart, search filter, and checkout validation.', 'https://github.com/priyapatel/eshop');

-- Certifications for Resume 2
INSERT INTO certifications (resume_id, cert_name, issuing_org, issue_year)
VALUES 
(2, 'Responsive Web Design Certification', 'freeCodeCamp', 2022);

-- Achievements for Resume 2
INSERT INTO achievements (resume_id, title, description)
VALUES 
(2, '1st Place - College Web Design Competition', 'Designed the winning accessible UI for the annual university tech symposium.');
