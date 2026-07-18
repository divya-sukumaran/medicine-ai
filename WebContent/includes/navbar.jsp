<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Shared navigation bar included on every page after login --%>
<nav class="navbar navbar-expand-lg app-navbar">
    <div class="container">
        <a class="navbar-brand d-flex align-items-center gap-2" href="${pageContext.request.contextPath}/dashboard">
            <span class="brand-mark"><i class="bi bi-heart-pulse-fill"></i></span>
            <span>AI Health Assistant</span>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="mainNavbar">
            <ul class="navbar-nav mx-auto gap-lg-1">
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath == '/dashboard' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/dashboard">
                        <i class="bi bi-grid-1x2 me-1"></i>Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath == '/medicines' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/medicines">
                        <i class="bi bi-capsule me-1"></i>Medicines
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath == '/health-record' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/health-record">
                        <i class="bi bi-clipboard2-pulse me-1"></i>Health Record
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath == '/chatbot.jsp' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/chatbot.jsp">
                        <i class="bi bi-stars me-1"></i>AI Assistant
                    </a>
                </li>
            </ul>
            <div class="d-flex align-items-center gap-2">
                <span class="d-none d-lg-inline text-muted small fw-semibold">
                    <i class="bi bi-person-circle me-1"></i>${sessionScope.userName}
                </span>
                <a class="btn btn-logout" href="${pageContext.request.contextPath}/logout">
                    <i class="bi bi-box-arrow-right me-1"></i>Logout
                </a>
            </div>
        </div>
    </div>
</nav>
