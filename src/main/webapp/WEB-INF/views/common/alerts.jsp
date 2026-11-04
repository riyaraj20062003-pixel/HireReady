<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- Display request-level or flash session-level alert messages --%>
<c:if test="${not empty errorMessage or not empty sessionScope.errorMessage}">
    <div class="alert alert-danger" role="alert">
        <span>
            <strong>Error:</strong> 
            <c:out value="${not empty errorMessage ? errorMessage : sessionScope.errorMessage}" />
        </span>
        <button type="button" class="btn btn-sm btn-secondary" onclick="this.parentElement.remove()" style="padding: 2px 8px; font-size: 12px;">&times;</button>
    </div>
    <% session.removeAttribute("errorMessage"); %>
</c:if>

<c:if test="${not empty successMessage or not empty sessionScope.successMessage}">
    <div class="alert alert-success" role="alert">
        <span>
            <strong>Success:</strong> 
            <c:out value="${not empty successMessage ? successMessage : sessionScope.successMessage}" />
        </span>
        <button type="button" class="btn btn-sm btn-secondary" onclick="this.parentElement.remove()" style="padding: 2px 8px; font-size: 12px;">&times;</button>
    </div>
    <% session.removeAttribute("successMessage"); %>
</c:if>
