<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <c:choose>
            <c:when test="${mode eq 'edit'}">Edit Resume - <c:out value="${resume.title}" /></c:when>
            <c:otherwise>Create New Resume - HireReady</c:otherwise>
        </c:choose>
    </title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

    <main class="main-content">
        <div class="container" style="max-width: 900px;">
            <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                <div>
                    <h1>
                        <c:choose>
                            <c:when test="${mode eq 'edit'}">Edit Resume</c:when>
                            <c:otherwise>Build New Resume</c:otherwise>
                        </c:choose>
                    </h1>
                    <p style="color: var(--text-muted); margin-bottom: 0;">
                        Fill in your career details. You can add multiple education degrees, skills, experiences, and projects.
                    </p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary btn-sm">
                        &larr; Back to Dashboard
                    </a>
                </div>
            </div>

            <form action="${pageContext.request.contextPath}/resume/${mode eq 'edit' ? 'edit' : 'create'}" 
                  method="POST" id="resumeForm">

                <c:if test="${mode eq 'edit'}">
                    <input type="hidden" name="resumeId" value="${resume.resumeId}">
                </c:if>

                <!-- SECTION 1: Resume Profile Title -->
                <div class="form-section">
                    <div class="form-section-title">
                        <span>1. Resume Profile Identifier</span>
                    </div>
                    <div class="form-group">
                        <label for="title" class="form-label">Resume Title <span class="required">*</span></label>
                        <input type="text" id="title" name="title" class="form-control" 
                               value="<c:out value='${resume.title}' />" 
                               placeholder="e.g. Software Engineer - Java & Spring Specialist" required>
                        <div class="form-text">A descriptive title to help you organize multiple resumes on your dashboard.</div>
                    </div>
                </div>

                <!-- SECTION 2: Personal Information -->
                <div class="form-section">
                    <div class="form-section-title">
                        <span>2. Personal &amp; Contact Information</span>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="fullName" class="form-label">Full Name <span class="required">*</span></label>
                            <input type="text" id="fullName" name="fullName" class="form-control" 
                                   value="<c:out value='${not empty resume.fullName ? resume.fullName : sessionScope.userName}' />" 
                                   placeholder="e.g. Rahul Sharma" required>
                        </div>
                        <div class="form-group">
                            <label for="email" class="form-label">Email Address <span class="required">*</span></label>
                            <input type="email" id="email" name="email" class="form-control" 
                                   value="<c:out value='${not empty resume.email ? resume.email : sessionScope.user.email}' />" 
                                   placeholder="e.g. rahul@example.com" required>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="phone" class="form-label">Phone Number <span class="required">*</span></label>
                            <input type="tel" id="phone" name="phone" class="form-control" 
                                   value="<c:out value='${resume.phone}' />" 
                                   placeholder="e.g. +91 9876543210" required>
                        </div>
                        <div class="form-group">
                            <label for="address" class="form-label">Location / City, Country</label>
                            <input type="text" id="address" name="address" class="form-control" 
                                   value="<c:out value='${resume.address}' />" 
                                   placeholder="e.g. Bengaluru, India">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="linkedin" class="form-label">LinkedIn Profile URL</label>
                            <input type="url" id="linkedin" name="linkedin" class="form-control" 
                                   value="<c:out value='${resume.linkedin}' />" 
                                   placeholder="https://linkedin.com/in/username">
                        </div>
                        <div class="form-group">
                            <label for="github" class="form-label">GitHub Profile / Portfolio URL</label>
                            <input type="url" id="github" name="github" class="form-control" 
                                   value="<c:out value='${resume.github}' />" 
                                   placeholder="https://github.com/username">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="summary" class="form-label">Professional Summary / Career Objective</label>
                        <textarea id="summary" name="summary" class="form-control" rows="4" 
                                  placeholder="Write a concise 3-4 sentence overview of your technical background, core competencies, and career goals..."><c:out value="${resume.summary}" /></textarea>
                    </div>
                </div>

                <!-- SECTION 3: Education (Dynamic Repeaters) -->
                <div class="form-section">
                    <div class="form-section-title">
                        <span>3. Education Details</span>
                        <button type="button" class="btn btn-secondary btn-sm" onclick="addEducationRow()">
                            + Add Education
                        </button>
                    </div>

                    <div id="educationContainer">
                        <c:choose>
                            <c:when test="${not empty resume.educationList}">
                                <c:forEach var="edu" items="${resume.educationList}">
                                    <div class="repeater-item">
                                        <div class="repeater-header">
                                            <span class="repeater-title">Education Entry</span>
                                            <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Remove</button>
                                        </div>
                                        <div class="form-row">
                                            <div class="form-group">
                                                <label class="form-label">Degree / Qualification <span class="required">*</span></label>
                                                <input type="text" name="degree[]" class="form-control" 
                                                       value="<c:out value='${edu.degree}' />" placeholder="e.g. B.Tech in CSE" required>
                                            </div>
                                            <div class="form-group">
                                                <label class="form-label">School / Institution</label>
                                                <input type="text" name="institution[]" class="form-control" 
                                                       value="<c:out value='${edu.institution}' />" placeholder="e.g. National Institute of Tech">
                                            </div>
                                        </div>
                                        <div class="form-row">
                                            <div class="form-group">
                                                <label class="form-label">Passing Year</label>
                                                <input type="number" name="passingYear[]" class="form-control" 
                                                       value="<c:out value='${edu.passingYear}' />" placeholder="e.g. 2024" min="1950" max="2099">
                                            </div>
                                            <div class="form-group">
                                                <label class="form-label">CGPA / Percentage</label>
                                                <input type="number" step="0.01" name="cgpa[]" class="form-control" 
                                                       value="<c:out value='${edu.cgpaOrPercentage}' />" placeholder="e.g. 8.5">
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="repeater-item">
                                    <div class="repeater-header">
                                        <span class="repeater-title">Education Entry</span>
                                        <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Remove</button>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label class="form-label">Degree / Qualification <span class="required">*</span></label>
                                            <input type="text" name="degree[]" class="form-control" placeholder="e.g. B.Tech in Computer Science and Engineering" required>
                                        </div>
                                        <div class="form-group">
                                            <label class="form-label">School / Institution</label>
                                            <input type="text" name="institution[]" class="form-control" placeholder="e.g. ABC Institute of Technology">
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label class="form-label">Passing Year</label>
                                            <input type="number" name="passingYear[]" class="form-control" placeholder="e.g. 2024" min="1950" max="2099">
                                        </div>
                                        <div class="form-group">
                                            <label class="form-label">CGPA / Percentage</label>
                                            <input type="number" step="0.01" name="cgpa[]" class="form-control" placeholder="e.g. 8.50 or 85.5">
                                        </div>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- SECTION 4: Skills (Dynamic Repeaters) -->
                <div class="form-section">
                    <div class="form-section-title">
                        <span>4. Skills</span>
                        <button type="button" class="btn btn-secondary btn-sm" onclick="addSkillRow()">
                            + Add Skill Category
                        </button>
                    </div>

                    <div id="skillsContainer">
                        <c:choose>
                            <c:when test="${not empty resume.skillList}">
                                <c:forEach var="sk" items="${resume.skillList}">
                                    <div class="repeater-item">
                                        <div class="repeater-header">
                                            <span class="repeater-title">Skill Category</span>
                                            <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Remove</button>
                                        </div>
                                        <div class="form-row">
                                            <div class="form-group">
                                                <label class="form-label">Category Name</label>
                                                <input type="text" name="skillCategory[]" class="form-control" 
                                                       value="<c:out value='${sk.skillCategory}' />" placeholder="e.g. Programming Languages">
                                            </div>
                                            <div class="form-group">
                                                <label class="form-label">Skills (Comma-separated) <span class="required">*</span></label>
                                                <input type="text" name="skillNames[]" class="form-control" 
                                                       value="<c:out value='${sk.skillNames}' />" placeholder="e.g. Java, Python, SQL" required>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="repeater-item">
                                    <div class="repeater-header">
                                        <span class="repeater-title">Skill Category</span>
                                        <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Remove</button>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label class="form-label">Category Name</label>
                                            <input type="text" name="skillCategory[]" class="form-control" placeholder="e.g. Programming Languages" value="">
                                        </div>
                                        <div class="form-group">
                                            <label class="form-label">Skills (Comma-separated) <span class="required">*</span></label>
                                            <input type="text" name="skillNames[]" class="form-control" placeholder="e.g. Java, Python, C++, SQL" required>
                                        </div>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- SECTION 5: Work Experience -->
                <div class="form-section">
                    <div class="form-section-title">
                        <span>5. Work Experience / Internships</span>
                        <button type="button" class="btn btn-secondary btn-sm" onclick="addExperienceRow()">
                            + Add Experience
                        </button>
                    </div>

                    <div id="experienceContainer">
                        <c:forEach var="exp" items="${resume.experienceList}">
                            <div class="repeater-item">
                                <div class="repeater-header">
                                    <span class="repeater-title">Experience Entry</span>
                                    <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Remove</button>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label class="form-label">Company / Organization <span class="required">*</span></label>
                                        <input type="text" name="company[]" class="form-control" 
                                               value="<c:out value='${exp.company}' />" placeholder="e.g. TechCorp Solutions" required>
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">Job Role / Title</label>
                                        <input type="text" name="jobRole[]" class="form-control" 
                                               value="<c:out value='${exp.jobRole}' />" placeholder="e.g. Java Developer Intern">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="form-label">Duration</label>
                                    <input type="text" name="duration[]" class="form-control" 
                                           value="<c:out value='${exp.duration}' />" placeholder="e.g. Jan 2024 - Jun 2024">
                                </div>
                                <div class="form-group">
                                    <label class="form-label">Responsibilities &amp; Achievements</label>
                                    <textarea name="expDescription[]" class="form-control" rows="3" 
                                              placeholder="Key duties and contributions..."><c:out value="${exp.description}" /></textarea>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div style="font-size: 0.82rem; color: var(--text-light);">
                        Click &quot;+ Add Experience&quot; above if you have internship or work experience.
                    </div>
                </div>

                <!-- SECTION 6: Projects -->
                <div class="form-section">
                    <div class="form-section-title">
                        <span>6. Projects</span>
                        <button type="button" class="btn btn-secondary btn-sm" onclick="addProjectRow()">
                            + Add Project
                        </button>
                    </div>

                    <div id="projectsContainer">
                        <c:choose>
                            <c:when test="${not empty resume.projectList}">
                                <c:forEach var="proj" items="${resume.projectList}">
                                    <div class="repeater-item">
                                        <div class="repeater-header">
                                            <span class="repeater-title">Project Entry</span>
                                            <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Remove</button>
                                        </div>
                                        <div class="form-row">
                                            <div class="form-group">
                                                <label class="form-label">Project Title <span class="required">*</span></label>
                                                <input type="text" name="projectTitle[]" class="form-control" 
                                                       value="<c:out value='${proj.title}' />" placeholder="e.g. HireReady Web Portal" required>
                                            </div>
                                            <div class="form-group">
                                                <label class="form-label">Technologies Used</label>
                                                <input type="text" name="technologies[]" class="form-control" 
                                                       value="<c:out value='${proj.technologies}' />" placeholder="e.g. Java, JSP, Servlets, MySQL">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="form-label">Project Link (GitHub or Live URL)</label>
                                            <input type="url" name="projectLink[]" class="form-control" 
                                                   value="<c:out value='${proj.projectLink}' />" placeholder="https://github.com/...">
                                        </div>
                                        <div class="form-group">
                                            <label class="form-label">Project Description</label>
                                            <textarea name="projectDescription[]" class="form-control" rows="3" 
                                                      placeholder="Features implemented and problems solved..."><c:out value="${proj.description}" /></textarea>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="repeater-item">
                                    <div class="repeater-header">
                                        <span class="repeater-title">Project Entry</span>
                                        <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Remove</button>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label class="form-label">Project Title <span class="required">*</span></label>
                                            <input type="text" name="projectTitle[]" class="form-control" placeholder="e.g. Online Resume Builder" required>
                                        </div>
                                        <div class="form-group">
                                            <label class="form-label">Technologies Used</label>
                                            <input type="text" name="technologies[]" class="form-control" placeholder="e.g. Java, JSP, Servlets, MySQL">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">Project Link (GitHub / Live URL)</label>
                                        <input type="url" name="projectLink[]" class="form-control" placeholder="https://github.com/username/project">
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">Project Description</label>
                                        <textarea name="projectDescription[]" class="form-control" rows="3" placeholder="Describe the purpose, features, and your contribution..."></textarea>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- SECTION 7: Certifications -->
                <div class="form-section">
                    <div class="form-section-title">
                        <span>7. Certifications</span>
                        <button type="button" class="btn btn-secondary btn-sm" onclick="addCertificationRow()">
                            + Add Certification
                        </button>
                    </div>

                    <div id="certificationsContainer">
                        <c:forEach var="cert" items="${resume.certificationList}">
                            <div class="repeater-item">
                                <div class="repeater-header">
                                    <span class="repeater-title">Certification Entry</span>
                                    <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Remove</button>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label class="form-label">Certification Name <span class="required">*</span></label>
                                        <input type="text" name="certName[]" class="form-control" 
                                               value="<c:out value='${cert.certName}' />" placeholder="e.g. Oracle Certified Associate Java" required>
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">Issuing Organization</label>
                                        <input type="text" name="issuingOrg[]" class="form-control" 
                                               value="<c:out value='${cert.issuingOrg}' />" placeholder="e.g. Oracle, Coursera">
                                    </div>
                                    <div class="form-group">
                                        <label class="form-label">Year</label>
                                        <input type="number" name="issueYear[]" class="form-control" 
                                               value="<c:out value='${cert.issueYear}' />" placeholder="e.g. 2023" min="1950" max="2099">
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <!-- SECTION 8: Achievements -->
                <div class="form-section">
                    <div class="form-section-title">
                        <span>8. Key Achievements &amp; Honors</span>
                        <button type="button" class="btn btn-secondary btn-sm" onclick="addAchievementRow()">
                            + Add Achievement
                        </button>
                    </div>

                    <div id="achievementsContainer">
                        <c:forEach var="ach" items="${resume.achievementList}">
                            <div class="repeater-item">
                                <div class="repeater-header">
                                    <span class="repeater-title">Achievement Entry</span>
                                    <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Remove</button>
                                </div>
                                <div class="form-group">
                                    <label class="form-label">Achievement Title <span class="required">*</span></label>
                                    <input type="text" name="achievementTitle[]" class="form-control" 
                                           value="<c:out value='${ach.title}' />" placeholder="e.g. Finalist in Hackathon" required>
                                </div>
                                <div class="form-group">
                                    <label class="form-label">Description</label>
                                    <textarea name="achievementDescription[]" class="form-control" rows="2" 
                                              placeholder="Brief context and impact..."><c:out value="${ach.description}" /></textarea>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <!-- Form Action Buttons -->
                <div style="display: flex; gap: 1rem; margin-top: 2rem; margin-bottom: 3rem;">
                    <button type="submit" class="btn btn-primary btn-lg" style="flex: 2;">
                        <c:choose>
                            <c:when test="${mode eq 'edit'}">Update Resume</c:when>
                            <c:otherwise>Save &amp; Generate Resume</c:otherwise>
                        </c:choose>
                    </button>
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary btn-lg" style="flex: 1;">
                        Cancel
                    </a>
                </div>

            </form>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
