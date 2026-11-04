<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Eligibility Filter - HireReady</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

            <div style="margin-bottom: 2rem;">
                <h1>Candidate Eligibility Search &amp; Filtering</h1>
                <p style="color: var(--text-muted); margin-bottom: 0;">
                    Filter candidate resumes stored in the database according to recruitment and eligibility criteria.
                </p>
            </div>

            <!-- Filter Criteria Card -->
            <div class="card">
                <div class="card-header">
                    <h2>Filter Criteria</h2>
                    <c:if test="${not empty criteria and not criteria.empty}">
                        <span class="badge badge-primary"><c:out value="${resultCount}" /> Matching Resumes Found</span>
                    </c:if>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/filter" method="GET">
                        <div class="form-row">
                            <div class="form-group">
                                <label for="minCgpa" class="form-label">Minimum CGPA / Score</label>
                                <input type="number" step="0.1" min="0" max="100" id="minCgpa" name="minCgpa" 
                                       class="form-control" placeholder="e.g. 7.5" value="<c:out value='${criteria.minCgpa}' />">
                                <div class="form-text">Finds candidates with CGPA &gt;= this score.</div>
                            </div>

                            <div class="form-group">
                                <label for="degree" class="form-label">Degree / Field of Study</label>
                                <input type="text" id="degree" name="degree" class="form-control" 
                                       placeholder="e.g. Computer Science or B.Tech" value="<c:out value='${criteria.degree}' />">
                                <div class="form-text">Matches degree name in education history.</div>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="skill" class="form-label">Required Skill</label>
                                <input type="text" id="skill" name="skill" class="form-control" 
                                       placeholder="e.g. Java, Python, or MySQL" value="<c:out value='${criteria.skill}' />">
                                <div class="form-text">Matches technical skills or programming languages.</div>
                            </div>

                            <div class="form-group">
                                <label for="passingYear" class="form-label">Graduation Year</label>
                                <input type="number" id="passingYear" name="passingYear" class="form-control" 
                                       placeholder="e.g. 2024" min="1970" max="2099" value="<c:out value='${criteria.passingYear}' />">
                                <div class="form-text">Filters by passing or graduation year.</div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="keyword" class="form-label">Keyword Search (Company / Role / Summary)</label>
                            <input type="text" id="keyword" name="keyword" class="form-control" 
                                   placeholder="e.g. Full Stack, Intern, Infosys, Backend" value="<c:out value='${criteria.keyword}' />">
                        </div>

                        <div style="display: flex; gap: 0.75rem; align-items: center; margin-top: 1rem;">
                            <button type="submit" class="btn btn-primary">
                                Apply Filter
                            </button>
                            <a href="${pageContext.request.contextPath}/filter" class="btn btn-secondary">
                                Reset Filters
                            </a>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Filter Results -->
            <div class="card">
                <div class="card-header">
                    <h2>Search Results</h2>
                    <span class="badge badge-neutral"><c:out value="${resultCount}" default="0" /> Results</span>
                </div>
                <div class="card-body" style="padding: 0;">
                    <c:choose>
                        <c:when test="${not empty results}">
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Candidate Name</th>
                                            <th>Resume Title</th>
                                            <th>Education / CGPA</th>
                                            <th>Key Skills</th>
                                            <th style="text-align: right;">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="res" items="${results}" varStatus="status">
                                            <tr>
                                                <td><c:out value="${status.count}" /></td>
                                                <td>
                                                    <strong><c:out value="${res.fullName}" /></strong>
                                                    <div style="font-size: 0.82rem; color: var(--text-muted);"><c:out value="${res.email}" /></div>
                                                </td>
                                                <td>
                                                    <c:out value="${res.title}" />
                                                    <div style="font-size: 0.8rem; color: var(--text-light);"><c:out value="${res.address}" /></div>
                                                </td>
                                                <td>
                                                    <c:forEach var="edu" items="${res.educationList}" varStatus="eduStat">
                                                        <c:if test="${eduStat.first}">
                                                            <div><strong><c:out value="${edu.degree}" /></strong></div>
                                                            <div style="font-size: 0.82rem; color: var(--text-muted);">
                                                                CGPA: <c:out value="${edu.cgpaOrPercentage}" /> (<c:out value="${edu.passingYear}" />)
                                                            </div>
                                                        </c:if>
                                                    </c:forEach>
                                                </td>
                                                <td style="max-width: 250px;">
                                                    <c:forEach var="sk" items="${res.skillList}">
                                                        <div style="font-size: 0.82rem; margin-bottom: 2px;">
                                                            <span style="font-weight: 600;"><c:out value="${sk.skillCategory}" />:</span> 
                                                            <c:out value="${sk.skillNames}" />
                                                        </div>
                                                    </c:forEach>
                                                </td>
                                                <td style="text-align: right; white-space: nowrap;">
                                                    <a href="${pageContext.request.contextPath}/resume/view?id=${res.resumeId}" 
                                                       class="btn btn-secondary btn-sm" target="_blank">
                                                        View Full Resume
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div style="text-align: center; padding: 3rem 1.5rem;">
                                <h3 style="color: var(--text-muted); margin-bottom: 0.5rem;">No matching resumes</h3>
                                <p style="color: var(--text-light); margin-bottom: 0;">
                                    Try adjusting your filter criteria or click &quot;Reset Filters&quot; to view all resumes.
                                </p>
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
