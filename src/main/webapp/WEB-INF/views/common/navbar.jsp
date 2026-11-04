<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="navbar no-print">
    <div class="container nav-container">
        <a href="${pageContext.request.contextPath}/" class="brand">
            <span>HireReady</span>
            <span class="brand-badge">Career System</span>
        </a>

        <ul class="nav-menu">
            <c:choose>
                <%-- Standard User Logged In --%>
                <c:when test="${not empty sessionScope.user}">
                    <li><a href="${pageContext.request.contextPath}/dashboard" class="nav-link">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/resume/create" class="nav-link">Create Resume</a></li>
                    <li><a href="${pageContext.request.contextPath}/resumes" class="nav-link">My Resumes</a></li>
                    <li><a href="${pageContext.request.contextPath}/job-eligibility" class="nav-link">Job Eligibility</a></li>
                    <li><a href="${pageContext.request.contextPath}/filter" class="nav-link">Search Filter</a></li>
                    <li>
                        <span class="badge badge-primary" style="margin-right: 0.5rem;">
                            <c:out value="${sessionScope.userName}" />
                        </span>
                    </li>
                    <li><a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary btn-sm">Logout</a></li>
                </c:when>

                <%-- Guest / Visitor --%>
                <c:otherwise>
                    <li><a href="${pageContext.request.contextPath}/" class="nav-link">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/job-eligibility" class="nav-link">Job Eligibility</a></li>
                    <li><a href="${pageContext.request.contextPath}/filter" class="nav-link">Search Filter</a></li>
                    <li><a href="${pageContext.request.contextPath}/login" class="nav-link">Login</a></li>
                    <li><a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-sm">Register</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>
