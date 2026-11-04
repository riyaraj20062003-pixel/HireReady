<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account - HireReady</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/navbar.jsp" />

    <main class="main-content">
        <div class="container">
            <div class="auth-wrapper">
                <div class="auth-card">
                    <div class="auth-header">
                        <h1>Create an Account</h1>
                        <p>Join HireReady to start building professional resumes</p>
                    </div>

                    <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

                    <form action="${pageContext.request.contextPath}/register" method="POST" autocomplete="off">
                        <div class="form-group">
                            <label for="fullName" class="form-label">Full Name <span class="required">*</span></label>
                            <input type="text" id="fullName" name="fullName" class="form-control" 
                                   value="<c:out value='${fullName}' />" placeholder="e.g. Rahul Sharma" required autofocus>
                        </div>

                        <div class="form-group">
                            <label for="email" class="form-label">Email Address <span class="required">*</span></label>
                            <input type="email" id="email" name="email" class="form-control" 
                                   value="<c:out value='${email}' />" placeholder="e.g. rahul.sharma@example.com" required>
                            <div class="form-text">We'll never share your email with third parties.</div>
                        </div>

                        <div class="form-group">
                            <label for="password" class="form-label">Password <span class="required">*</span></label>
                            <input type="password" id="password" name="password" class="form-control" 
                                   placeholder="At least 6 characters" minlength="6" required>
                        </div>

                        <div class="form-group">
                            <label for="confirmPassword" class="form-label">Confirm Password <span class="required">*</span></label>
                            <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" 
                                   placeholder="Re-enter your password" minlength="6" required>
                        </div>

                        <button type="submit" class="btn btn-primary btn-lg" style="width: 100%; margin-top: 0.5rem;">
                            Register Account
                        </button>
                    </form>

                    <div style="text-align: center; margin-top: 1.5rem; font-size: 0.9rem; color: var(--text-muted);">
                        Already have an account? 
                        <a href="${pageContext.request.contextPath}/login">Log in here</a>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
