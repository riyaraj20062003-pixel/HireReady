<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 Page Not Found - HireReady</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

    <main class="main-content">
        <div class="container" style="text-align: center; padding: 4rem 1rem;">
            <div style="font-size: 5rem; font-weight: 800; color: var(--primary); line-height: 1;">404</div>
            <h1 style="margin: 1rem 0 0.5rem;">Page Not Found</h1>
            <p style="color: var(--text-muted); max-width: 500px; margin: 0 auto 2rem;">
                The page or resume you requested could not be found. It may have been removed, renamed, or is temporarily unavailable.
            </p>
            <div style="display: flex; gap: 1rem; justify-content: center;">
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Return Home</a>
                <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">Go to Dashboard</a>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
