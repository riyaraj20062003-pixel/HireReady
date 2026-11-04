<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<footer class="footer no-print">
    <div class="container footer-content">
        <div>
            <strong>HireReady</strong> &mdash; Online Resume Builder &amp; Career Management System
            <div style="font-size: 0.8rem; color: var(--text-light); margin-top: 0.2rem;">
                A comprehensive Java Servlets, JSP &amp; MySQL Project
            </div>
        </div>
        <div>
            <span>&copy; <%= java.time.Year.now() %> HireReady. All rights reserved.</span>
        </div>
    </div>
</footer>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
