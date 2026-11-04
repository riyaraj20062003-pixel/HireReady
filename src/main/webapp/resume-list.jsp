<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Resumes - HireReady</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                <div>
                    <h1>My Resumes</h1>
                    <p style="color: var(--text-muted); margin-bottom: 0;">
                        Manage, edit, or print any of your saved resume profiles.
                    </p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/resume/create" class="btn btn-primary">
                        + Create New Resume
                    </a>
                </div>
            </div>

            <div class="card">
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
                                            <th>Created Date</th>
                                            <th>Last Modified</th>
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
                                                <td><c:out value="${r.createdAt}" /></td>
                                                <td><c:out value="${r.updatedAt}" /></td>
                                                <td style="text-align: right; white-space: nowrap;">
                                                    <a href="${pageContext.request.contextPath}/resume/view?id=${r.resumeId}" 
                                                       class="btn btn-secondary btn-sm">Preview</a>
                                                    <a href="${pageContext.request.contextPath}/resume/edit?id=${r.resumeId}" 
                                                       class="btn btn-secondary btn-sm">Edit</a>
                                                    <a href="${pageContext.request.contextPath}/resume/delete?id=${r.resumeId}" 
                                                       class="btn btn-danger btn-sm"
                                                       onclick="return confirmAction('Are you sure you want to delete this resume?');">Delete</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div style="text-align: center; padding: 3rem 1.5rem;">
                                <h3 style="color: var(--text-muted); margin-bottom: 0.5rem;">No resumes found</h3>
                                <p style="color: var(--text-light); margin-bottom: 1.5rem;">
                                    You have not created any resumes yet.
                                </p>
                                <a href="${pageContext.request.contextPath}/resume/create" class="btn btn-primary">
                                    Create a Resume
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
