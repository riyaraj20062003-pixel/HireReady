<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${resume.fullName}" /> - Resume</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

    <main class="main-content">
        <div class="container resume-preview-container">
            <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

            <!-- Action Bar (Hidden in Print Mode) -->
            <div class="resume-action-bar no-print">
                <div>
                    <strong style="color: var(--text-main); font-size: 1.05rem;">
                        <c:out value="${resume.title}" />
                    </strong>
                    <div style="font-size: 0.8rem; color: var(--text-muted);">
                        Last updated: <c:out value="${resume.updatedAt}" />
                    </div>
                </div>
                <div style="display: flex; gap: 0.5rem; align-items: center;">
                    <button type="button" class="btn btn-primary btn-sm" onclick="printResume()">
                        Print / Save as PDF
                    </button>
                    
                    <c:if test="${not empty sessionScope.user and sessionScope.user.userId eq resume.userId}">
                        <a href="${pageContext.request.contextPath}/resume/edit?id=${resume.resumeId}" class="btn btn-secondary btn-sm">
                            Edit
                        </a>
                    </c:if>

                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary btn-sm">
                        Dashboard
                    </a>
                </div>
            </div>

            <!-- Authentic Professional Resume Paper Document -->
            <div class="resume-paper" id="resumeDocument">

                <!-- 1. Header & Contact -->
                <header class="resume-header">
                    <h1 class="resume-name"><c:out value="${resume.fullName}" /></h1>
                    <c:if test="${not empty resume.title}">
                        <div class="resume-target-title"><c:out value="${resume.title}" /></div>
                    </c:if>

                    <div class="resume-contact-info">
                        <c:if test="${not empty resume.email}">
                            <span>Email: <a href="mailto:${resume.email}"><c:out value="${resume.email}" /></a></span>
                        </c:if>
                        <c:if test="${not empty resume.phone}">
                            <span>Phone: <c:out value="${resume.phone}" /></span>
                        </c:if>
                        <c:if test="${not empty resume.address}">
                            <span>Location: <c:out value="${resume.address}" /></span>
                        </c:if>
                        <c:if test="${not empty resume.linkedin}">
                            <span>LinkedIn: <a href="<c:out value='${resume.linkedin}' />" target="_blank" rel="noopener">Profile</a></span>
                        </c:if>
                        <c:if test="${not empty resume.github}">
                            <span>GitHub: <a href="<c:out value='${resume.github}' />" target="_blank" rel="noopener">Portfolio</a></span>
                        </c:if>
                    </div>
                </header>

                <!-- 2. Professional Summary -->
                <c:if test="${not empty resume.summary}">
                    <section class="resume-section">
                        <h2 class="resume-section-heading">Professional Summary</h2>
                        <p class="resume-item-desc" style="text-align: justify; line-height: 1.55;">
                            <c:out value="${resume.summary}" />
                        </p>
                    </section>
                </c:if>

                <!-- 3. Education -->
                <c:if test="${not empty resume.educationList}">
                    <section class="resume-section">
                        <h2 class="resume-section-heading">Education</h2>
                        <c:forEach var="edu" items="${resume.educationList}">
                            <div class="resume-item">
                                <div class="resume-item-top">
                                    <span class="resume-item-title"><c:out value="${edu.degree}" /></span>
                                    <span class="resume-item-date">
                                        <c:if test="${edu.passingYear gt 0}">
                                            Graduated: <c:out value="${edu.passingYear}" />
                                        </c:if>
                                    </span>
                                </div>
                                <div style="display: flex; justify-content: space-between; font-size: 0.9rem; color: #475569;">
                                    <span><c:out value="${edu.institution}" /></span>
                                    <c:if test="${edu.cgpaOrPercentage gt 0}">
                                        <strong>CGPA/Score: <c:out value="${edu.cgpaOrPercentage}" /></strong>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </section>
                </c:if>

                <!-- 4. Technical Skills -->
                <c:if test="${not empty resume.skillList}">
                    <section class="resume-section">
                        <h2 class="resume-section-heading">Technical Skills</h2>
                        <div class="resume-skills-grid">
                            <c:forEach var="sk" items="${resume.skillList}">
                                <div class="resume-skill-cat"><c:out value="${sk.skillCategory}" />:</div>
                                <div class="resume-skill-vals"><c:out value="${sk.skillNames}" /></div>
                            </c:forEach>
                        </div>
                    </section>
                </c:if>

                <!-- 5. Work Experience -->
                <c:if test="${not empty resume.experienceList}">
                    <section class="resume-section">
                        <h2 class="resume-section-heading">Experience &amp; Internships</h2>
                        <c:forEach var="exp" items="${resume.experienceList}">
                            <div class="resume-item">
                                <div class="resume-item-top">
                                    <span class="resume-item-title"><c:out value="${exp.jobRole}" /></span>
                                    <span class="resume-item-date"><c:out value="${exp.duration}" /></span>
                                </div>
                                <div class="resume-item-sub"><c:out value="${exp.company}" /></div>
                                <c:if test="${not empty exp.description}">
                                    <p class="resume-item-desc" style="white-space: pre-line;">
                                        <c:out value="${exp.description}" />
                                    </p>
                                </c:if>
                            </div>
                        </c:forEach>
                    </section>
                </c:if>

                <!-- 6. Projects -->
                <c:if test="${not empty resume.projectList}">
                    <section class="resume-section">
                        <h2 class="resume-section-heading">Academic &amp; Personal Projects</h2>
                        <c:forEach var="proj" items="${resume.projectList}">
                            <div class="resume-item">
                                <div class="resume-item-top">
                                    <span class="resume-item-title">
                                        <c:out value="${proj.title}" />
                                        <c:if test="${not empty proj.technologies}">
                                            <span style="font-weight: normal; font-size: 0.85rem; color: #475569;">
                                                | <em><c:out value="${proj.technologies}" /></em>
                                            </span>
                                        </c:if>
                                    </span>
                                    <c:if test="${not empty proj.projectLink}">
                                        <a href="<c:out value='${proj.projectLink}' />" target="_blank" rel="noopener" class="resume-item-date">
                                            Link &rarr;
                                        </a>
                                    </c:if>
                                </div>
                                <c:if test="${not empty proj.description}">
                                    <p class="resume-item-desc" style="white-space: pre-line;">
                                        <c:out value="${proj.description}" />
                                    </p>
                                </c:if>
                            </div>
                        </c:forEach>
                    </section>
                </c:if>

                <!-- 7. Certifications -->
                <c:if test="${not empty resume.certificationList}">
                    <section class="resume-section">
                        <h2 class="resume-section-heading">Certifications</h2>
                        <c:forEach var="cert" items="${resume.certificationList}">
                            <div class="resume-item">
                                <div class="resume-item-top">
                                    <span class="resume-item-title"><c:out value="${cert.certName}" /></span>
                                    <span class="resume-item-date">
                                        <c:if test="${cert.issueYear gt 0}"><c:out value="${cert.issueYear}" /></c:if>
                                    </span>
                                </div>
                                <div style="font-size: 0.88rem; color: #475569;">
                                    Issued by: <c:out value="${cert.issuingOrg}" />
                                </div>
                            </div>
                        </c:forEach>
                    </section>
                </c:if>

                <!-- 8. Achievements -->
                <c:if test="${not empty resume.achievementList}">
                    <section class="resume-section">
                        <h2 class="resume-section-heading">Key Achievements</h2>
                        <c:forEach var="ach" items="${resume.achievementList}">
                            <div class="resume-item">
                                <div class="resume-item-title" style="font-size: 0.95rem;">&bull; <c:out value="${ach.title}" /></div>
                                <c:if test="${not empty ach.description}">
                                    <p class="resume-item-desc" style="margin-left: 1rem;">
                                        <c:out value="${ach.description}" />
                                    </p>
                                </c:if>
                            </div>
                        </c:forEach>
                    </section>
                </c:if>

            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
