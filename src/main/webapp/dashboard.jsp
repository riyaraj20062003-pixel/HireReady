<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Dashboard - HireReady</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

            <!-- Welcome Header -->
            <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem; margin-bottom: 2rem;">
                <div>
                    <h1>Welcome, <c:out value="${sessionScope.userName}" />!</h1>
                    <p style="color: var(--text-muted); margin-bottom: 0;">
                        Manage your professional resumes, create tailored job profiles, or export to PDF.
                    </p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/resume/create" class="btn btn-primary btn-lg">
                        + Create New Resume
                    </a>
                </div>
            </div>

            <!-- Stats Grid -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-num"><c:out value="${resumeCount}" default="0" /></div>
                    <div class="stat-label">Total Resumes Created</div>
                </div>
                <div class="stat-card">
                    <div class="stat-num" style="font-size: 1.3rem; line-height: 1.5; color: var(--text-main); font-weight: 600;">
                        <c:out value="${sessionScope.user.email}" />
                    </div>
                    <div class="stat-label">Logged In Account</div>
                </div>
                <div class="stat-card">
                    <div style="margin-top: 0.25rem;">
                        <a href="${pageContext.request.contextPath}/filter" class="btn btn-secondary btn-sm" style="width: 100%;">
                            Open Eligibility Search
                        </a>
                    </div>
                    <div class="stat-label" style="margin-top: 0.5rem;">Explore Candidate Filter</div>
                </div>
            </div>

            <!-- My Resumes Table Card -->
            <div class="card">
                <div class="card-header">
                    <h2>My Resumes</h2>
                    <span class="badge badge-primary"><c:out value="${resumeCount}" default="0" /> Resumes</span>
                </div>
                <div class="card-body" style="padding: 0;">
                    <c:choose>
                        <c:when test="${not empty resumes}">
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Resume Title</th>
                                            <th>Contact Name</th>
                                            <th>Contact Email</th>
                                            <th>Last Updated</th>
                                            <th style="text-align: right;">Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="r" items="${resumes}" varStatus="status">
                                            <tr>
                                                <td><c:out value="${status.count}" /></td>
                                                <td>
                                                    <strong>
                                                        <a href="${pageContext.request.contextPath}/resume/view?id=${r.resumeId}">
                                                            <c:out value="${r.title}" />
                                                        </a>
                                                    </strong>
                                                </td>
                                                <td><c:out value="${r.fullName}" /></td>
                                                <td><c:out value="${r.email}" /></td>
                                                <td>
                                                    <span style="font-size: 0.85rem; color: var(--text-muted);">
                                                        <c:out value="${r.updatedAt}" />
                                                    </span>
                                                </td>
                                                <td style="text-align: right; white-space: nowrap;">
                                                    <a href="${pageContext.request.contextPath}/resume/view?id=${r.resumeId}" 
                                                       class="btn btn-secondary btn-sm" title="Preview & Print">
                                                        Preview
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/resume/edit?id=${r.resumeId}" 
                                                       class="btn btn-secondary btn-sm" title="Edit Resume">
                                                        Edit
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/resume/delete?id=${r.resumeId}" 
                                                       class="btn btn-danger btn-sm" 
                                                       onclick="return confirmAction('Are you sure you want to permanently delete the resume &quot;${r.title}&quot;?');"
                                                       title="Delete Resume">
                                                        Delete
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div style="text-align: center; padding: 3.5rem 1.5rem;">
                                <h3 style="color: var(--text-muted); margin-bottom: 0.5rem;">No resumes created yet</h3>
                                <p style="color: var(--text-light); max-width: 450px; margin: 0 auto 1.5rem;">
                                    Start building your first professional resume with education, skills, experience, and projects.
                                </p>
                                <a href="${pageContext.request.contextPath}/resume/create" class="btn btn-primary">
                                    + Create Your First Resume
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
