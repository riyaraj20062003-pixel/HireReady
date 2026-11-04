<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HireReady - Online Resume Builder &amp; Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

            <!-- Hero Section -->
            <section class="hero">
                <span class="hero-tag">Career Platform &amp; Resume Management</span>
                <h1 class="hero-title">Craft Standout Resumes with <span>HireReady</span></h1>
                <p class="hero-desc">
                    A streamlined, professional career portal to create, organize, manage, and filter high-impact resumes. Built for students, professionals, and recruiters.
                </p>
                <div class="hero-actions">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary btn-lg">Go to Dashboard</a>
                            <a href="${pageContext.request.contextPath}/resume/create" class="btn btn-secondary btn-lg">Create New Resume</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-lg">Get Started Free</a>
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-secondary btn-lg">Sign In</a>
                        </c:otherwise>
                    </c:choose>
                    <a href="${pageContext.request.contextPath}/filter" class="btn btn-secondary btn-lg">Eligibility Search</a>
                </div>
            </section>

            <!-- Feature Overview -->
            <section style="margin-top: 1rem;">
                <div style="text-align: center; margin-bottom: 2rem;">
                    <h2>Engineered for Simplicity and Reliability</h2>
                    <p style="color: var(--text-muted);">Everything you need to craft, update, and manage your job applications.</p>
                </div>

                <div class="features-grid">
                    <div class="feature-card">
                        <div class="feature-icon">1</div>
                        <h3 class="feature-title">Intuitive Resume Builder</h3>
                        <p class="feature-desc">
                            Easily input personal details, educational qualifications, categorized technical skills, professional experience, projects, and awards.
                        </p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon">2</div>
                        <h3 class="feature-title">Print &amp; PDF Ready</h3>
                        <p class="feature-desc">
                            Instant preview with a clean, recruiter-approved document format. One-click browser print layout ready to save as a clean PDF.
                        </p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon">3</div>
                        <h3 class="feature-title">Eligibility Filtering</h3>
                        <p class="feature-desc">
                            Filter candidate resumes directly in the database using criteria such as Minimum CGPA, Degree, specific Skills, and Graduation Year.
                        </p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon">4</div>
                        <h3 class="feature-title">Multi-Resume Management</h3>
                        <p class="feature-desc">
                            Maintain multiple resume profiles tailored for different job roles. Edit, update, or remove entries anytime with strict privacy controls.
                        </p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon">5</div>
                        <h3 class="feature-title">Data Security &amp; Isolation</h3>
                        <p class="feature-desc">
                            Secure session-based authentication, salted BCrypt password hashing, and user-level authorization to prevent cross-account tampering.
                        </p>
                    </div>
                </div>
            </section>

        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
