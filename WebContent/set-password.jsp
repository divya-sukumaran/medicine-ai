<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session.getAttribute("reg_access_token") == null) {
        response.sendRedirect(request.getContextPath() + "/register.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Set Password | AI Health Assistant</title>
    <%@ include file="includes/head.jsp" %>
</head>
<body>

<div class="auth-page d-flex align-items-center justify-content-center">
    <div class="card auth-card">
        <div class="card-body p-4 p-md-5">
            <div class="text-center mb-4">
                <span class="auth-brand-mark mb-3"><i class="bi bi-shield-lock-fill"></i></span>
                <h3 class="mt-3 mb-1">Set your password</h3>
                <p class="text-muted mb-0">Email verified &mdash; last step</p>
            </div>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger d-flex align-items-center gap-2">
                    <i class="bi bi-exclamation-circle"></i><%= request.getAttribute("error") %>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/register" method="post" id="setPasswordForm" novalidate>
                <input type="hidden" name="step" value="password">
                <div class="mb-4">
                    <label class="form-label" for="password">Password</label>
                    <input type="password" class="form-control" id="password" name="password"
                           placeholder="At least 6 characters" minlength="6" required autofocus>
                    <div class="invalid-feedback">Password must be at least 6 characters.</div>
                </div>
                <button type="submit" class="btn btn-primary w-100 py-2">
                    Create account <i class="bi bi-arrow-right ms-1"></i>
                </button>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
