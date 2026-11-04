<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Job Eligibility Filtering - HireReady</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

            <div style="margin-bottom: 2rem;">
                <h1>Job Eligibility Filtering</h1>
                <p style="color: var(--text-muted); margin-bottom: 0;">
                    Select a job opening to evaluate all candidate resumes against required criteria including Minimum CGPA, Mandatory Skills, and Graduation Year.
                </p>
            </div>

            <!-- Job Selection & Criteria Panel -->
            <div class="card" style="margin-bottom: 2rem;">
                <div class="card-header">
                    <h2>Select Job Opening</h2>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/job-eligibility" method="GET">
                        <div class="form-row">
                            <div class="form-group" style="flex: 2;">
                                <label for="jobId" class="form-label">Available Job Positions <span class="required">*</span></label>
                                <select id="jobId" name="jobId" class="form-control" onchange="this.form.submit()">
                                    <option value="">-- Choose a Job to View Criteria --</option>
                                    <c:forEach var="j" items="${jobs}">
                                        <option value="${j.jobId}" ${selectedJob.jobId eq j.jobId ? 'selected' : ''}>
                                            <c:out value="${j.jobTitle}" /> - <c:out value="${j.companyName}" /> (<c:out value="${j.location}" />)
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group" style="flex: 1; display: flex; align-items: flex-end;">
                                <button type="submit" class="btn btn-primary" style="width: 100%;">
                                    Filter Eligible Resumes
                                </button>
                            </div>
                        </div>
                    </form>

                    <!-- Criteria Summary Box -->
                    <c:if test="${not empty selectedJob}">
                        <div style="margin-top: 1.5rem; padding: 1.25rem; background-color: var(--bg-main, #f8fafc); border-radius: 8px; border: 1px solid var(--border-light, #e2e8f0);">
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.75rem;">
                                <h3 style="margin: 0; font-size: 1.15rem; color: var(--text-main);">
                                    <c:out value="${selectedJob.jobTitle}" />
                                    <span style="font-size: 0.85rem; font-weight: normal; color: var(--text-muted);">
                                        at <c:out value="${selectedJob.companyName}" />
                                    </span>
                                </h3>
                                <span class="badge badge-primary"><c:out value="${selectedJob.jobType}" /></span>
                            </div>

                            <p style="font-size: 0.9rem; color: var(--text-muted); margin-bottom: 1rem;">
                                <c:out value="${selectedJob.jobDescription}" />
                            </p>

                            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem;">
                                <div>
                                    <strong style="font-size: 0.85rem; color: var(--text-muted); display: block;">1. Minimum CGPA Requirement</strong>
                                    <span style="font-size: 1.05rem; font-weight: 600; color: #1e293b;">
                                        <c:choose>
                                            <c:when test="${not empty selectedJob.minimumCgpa}">
                                                &ge; <c:out value="${selectedJob.minimumCgpa}" />
                                            </c:when>
                                            <c:otherwise>No minimum CGPA required</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>

                                <div>
                                    <strong style="font-size: 0.85rem; color: var(--text-muted); display: block;">2. Graduation Year Requirement</strong>
                                    <span style="font-size: 1.05rem; font-weight: 600; color: #1e293b;">
                                        <c:choose>
                                            <c:when test="${not empty selectedJob.graduationYear and selectedJob.graduationYear gt 0}">
                                                Batch of <c:out value="${selectedJob.graduationYear}" />
                                            </c:when>
                                            <c:otherwise>Any passing year eligible</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>

                                <div>
                                    <strong style="font-size: 0.85rem; color: var(--text-muted); display: block;">3. Mandatory Required Skills</strong>
                                    <div style="margin-top: 0.25rem;">
                                        <c:choose>
                                            <c:when test="${not empty selectedJob.requiredSkills}">
                                                <c:forEach var="reqSkill" items="${selectedJob.requiredSkills}">
                                                    <span class="badge badge-neutral" style="margin-right: 4px; margin-bottom: 4px;">
                                                        <c:out value="${reqSkill}" />
                                                    </span>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: var(--text-muted);">None specified</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>

            <!-- Evaluation Results Table -->
            <c:if test="${not empty selectedJob}">
                <div class="card">
                    <div class="card-header">
                        <h2>Eligibility Evaluation Results</h2>
                        <div>
                            <span class="badge badge-primary" style="font-size: 0.9rem;">
                                <c:out value="${eligibleCount}" /> of <c:out value="${totalEvaluated}" /> Resumes Eligible
                            </span>
                        </div>
                    </div>
                    <div class="card-body" style="padding: 0;">
                        <c:choose>
                            <c:when test="${not empty evaluations}">
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Candidate Name</th>
                                                <th>Resume Title</th>
                                                <th>CGPA</th>
                                                <th>Graduation Year</th>
                                                <th>Matching Skills</th>
                                                <th>Eligibility Status</th>
                                                <th style="text-align: right;">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="item" items="${evaluations}" varStatus="status">
                                                <tr style="${item.overallEligible ? 'background-color: #f0fdf4;' : ''}">
                                                    <td><c:out value="${status.count}" /></td>
                                                    <td>
                                                        <strong><c:out value="${item.resume.fullName}" /></strong>
                                                        <div style="font-size: 0.8rem; color: var(--text-muted);">
                                                            <c:out value="${item.resume.email}" />
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <c:out value="${item.resume.title}" />
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${item.candidateCgpa gt 0}">
                                                                <span style="${item.cgpaEligible ? 'color: #16a34a; font-weight: 600;' : 'color: #dc2626;'}">
                                                                    <c:out value="${item.candidateCgpa}" />
                                                                </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span style="color: var(--text-light); font-style: italic;">Not provided</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${item.candidateGraduationYear gt 0}">
                                                                <span style="${item.graduationYearEligible ? 'color: #16a34a; font-weight: 600;' : 'color: #dc2626;'}">
                                                                    <c:out value="${item.candidateGraduationYear}" />
                                                                </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span style="color: var(--text-light); font-style: italic;">Not provided</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td style="max-width: 200px;">
                                                        <c:choose>
                                                            <c:when test="${not empty item.matchingSkills}">
                                                                <c:forEach var="ms" items="${item.matchingSkills}">
                                                                    <span class="badge badge-success" style="background-color: #dcfce7; color: #166534; margin-right: 2px;">
                                                                        &check; <c:out value="${ms}" />
                                                                    </span>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span style="font-size: 0.82rem; color: var(--text-light);">No matching skills</span>
                                                            </c:otherwise>
                                                        </c:choose>

                                                        <c:if test="${not empty item.missingSkills}">
                                                            <div style="margin-top: 4px;">
                                                                <c:forEach var="mis" items="${item.missingSkills}">
                                                                    <span class="badge badge-danger" style="background-color: #fee2e2; color: #991b1b; margin-right: 2px; font-size: 0.75rem;">
                                                                        &cross; <c:out value="${mis}" />
                                                                    </span>
                                                                </c:forEach>
                                                            </div>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${item.overallEligible}">
                                                                <span class="badge" style="background-color: #16a34a; color: #ffffff; padding: 0.35rem 0.65rem; font-weight: 600;">
                                                                    &check; ELIGIBLE
                                                                </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge" style="background-color: #ef4444; color: #ffffff; padding: 0.35rem 0.65rem; font-weight: 600;">
                                                                    NOT ELIGIBLE
                                                                </span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td style="text-align: right; white-space: nowrap;">
                                                        <a href="${pageContext.request.contextPath}/resume/view?id=${item.resume.resumeId}" 
                                                           class="btn btn-secondary btn-sm" target="_blank">
                                                            View Resume
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
                                    <h3 style="color: var(--text-muted); margin-bottom: 0.5rem;">No resumes found in system</h3>
                                    <p style="color: var(--text-light); margin-bottom: 0;">
                                        There are currently no resumes stored to evaluate.
                                    </p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>

        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
