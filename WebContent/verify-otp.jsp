<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session.getAttribute("reg_email") == null) {
        response.sendRedirect(request.getContextPath() + "/register.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Verify Email | AI Health Assistant</title>
    <%@ include file="includes/head.jsp" %>
</head>
<body>

<div class="auth-page d-flex align-items-center justify-content-center">
    <div class="card auth-card">
        <div class="card-body p-4 p-md-5">
            <div class="text-center mb-4">
                <span class="auth-brand-mark mb-3"><i class="bi bi-envelope-check-fill"></i></span>
                <h3 class="mt-3 mb-1">Verify your email</h3>
                <p class="text-muted mb-0">
                    Enter the code we sent to
                    <strong>${empty email ? sessionScope.reg_email : email}</strong>
                </p>
            </div>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger d-flex align-items-center gap-2">
                    <i class="bi bi-exclamation-circle"></i><%= request.getAttribute("error") %>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/register" method="post" id="verifyOtpForm" novalidate>
                <input type="hidden" name="step" value="otp">
                <div class="mb-4">
                    <label class="form-label" for="otp">Verification code</label>
                    <input type="text" class="form-control text-center" id="otp" name="otp"
                           placeholder="8-digit code" inputmode="numeric" pattern="[0-9]{8}"
                           maxlength="8" autocomplete="one-time-code" required autofocus>
                    <div class="invalid-feedback">Please enter the 8-digit code.</div>
                </div>
                <button type="submit" class="btn btn-primary w-100 py-2">
                    Verify <i class="bi bi-arrow-right ms-1"></i>
                </button>
            </form>

            <p class="text-center mt-4 mb-0 text-muted">
                Wrong email? <a href="register.jsp" class="fw-semibold text-decoration-none">Start over</a>
            </p>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
