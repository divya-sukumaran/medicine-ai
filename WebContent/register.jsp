<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Sign Up | AI Health Assistant</title>
    <%@ include file="includes/head.jsp" %>
</head>
<body>

<div class="auth-page d-flex align-items-center justify-content-center">
    <div class="card auth-card">
        <div class="card-body p-4 p-md-5">
            <div class="text-center mb-4">
                <span class="auth-brand-mark mb-3"><i class="bi bi-heart-pulse-fill"></i></span>
                <h3 class="mt-3 mb-1">Create your account</h3>
                <p class="text-muted mb-0">Start managing your health in minutes</p>
            </div>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger d-flex align-items-center gap-2">
                    <i class="bi bi-exclamation-circle"></i><%= request.getAttribute("error") %>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/register" method="post" id="registerForm" novalidate>
                <input type="hidden" name="step" value="details">
                <div class="mb-3">
                    <label class="form-label" for="name">Full Name</label>
                    <input type="text" class="form-control" id="name" name="name"
                           placeholder="Your name" required>
                    <div class="invalid-feedback">Please enter your full name.</div>
                </div>
                <div class="mb-3">
                    <label class="form-label" for="email">Email address</label>
                    <input type="email" class="form-control" id="email" name="email"
                           placeholder="you@example.com" required>
                    <div class="invalid-feedback">Please enter a valid email address.</div>
                </div>
                <div class="mb-4">
                    <label class="form-label" for="phone">Phone Number</label>
                    <input type="tel" class="form-control" id="phone" name="phone"
                           placeholder="10-digit mobile number" pattern="[0-9]{10}" required>
                    <div class="invalid-feedback">Please enter a valid 10-digit phone number.</div>
                </div>
                <button type="submit" class="btn btn-primary w-100 py-2">
                    Send verification code <i class="bi bi-arrow-right ms-1"></i>
                </button>
            </form>

            <p class="text-center mt-4 mb-0 text-muted">
                Already have an account? <a href="login.jsp" class="fw-semibold text-decoration-none">Sign in</a>
            </p>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
