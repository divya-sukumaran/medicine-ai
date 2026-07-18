<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Dashboard | AI Health Assistant</title>
    <%@ include file="includes/head.jsp" %>
</head>
<body>

<%@ include file="includes/navbar.jsp" %>

<div class="container py-4">
    <div class="mb-4">
        <h3 class="page-title">Hello, ${sessionScope.userName} 👋</h3>
        <p class="page-subtitle mb-0">Here is a quick overview of your health today.</p>
    </div>

    <div class="row g-3">
        <div class="col-lg-3 col-sm-6">
            <div class="card stat-card card-hover h-100">
                <div class="card-body">
                    <span class="stat-icon blue"><i class="bi bi-capsule"></i></span>
                    <div>
                        <p class="stat-label">Total Medicines</p>
                        <h2 class="stat-value">${totalMedicines}</h2>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-3 col-sm-6">
            <div class="card stat-card card-hover h-100">
                <div class="card-body">
                    <span class="stat-icon green"><i class="bi bi-calendar2-check"></i></span>
                    <div>
                        <p class="stat-label">Today's Medicines</p>
                        <h2 class="stat-value">${todaysMedicines}</h2>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-3 col-sm-6">
            <div class="card stat-card card-hover h-100">
                <div class="card-body">
                    <span class="stat-icon violet"><i class="bi bi-clipboard2-pulse"></i></span>
                    <div>
                        <p class="stat-label">Health Record</p>
                        <c:choose>
                            <c:when test="${healthRecordComplete}">
                                <h2 class="stat-value" style="color:#10B981;">Complete</h2>
                            </c:when>
                            <c:otherwise>
                                <h2 class="stat-value" style="color:#D97706;">Pending</h2>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-3 col-sm-6">
            <a href="${pageContext.request.contextPath}/chatbot.jsp" class="text-decoration-none">
                <div class="card stat-card card-hover h-100">
                    <div class="card-body">
                        <span class="stat-icon cyan"><i class="bi bi-stars"></i></span>
                        <div>
                            <p class="stat-label">AI Assistant</p>
                            <h2 class="stat-value" style="color:#0891B2;">Ask now <i class="bi bi-arrow-right-short"></i></h2>
                        </div>
                    </div>
                </div>
            </a>
        </div>
    </div>

    <div class="row g-3 mt-2">
        <div class="col-md-6">
            <div class="card card-hover h-100">
                <div class="card-body p-4">
                    <span class="stat-icon blue mb-3"><i class="bi bi-alarm"></i></span>
                    <h5 class="mt-3">Medicine Reminder</h5>
                    <p class="text-muted">Add, edit, and track your daily medicines with dosage and timing.</p>
                    <a href="${pageContext.request.contextPath}/medicines" class="btn btn-primary btn-sm px-3">
                        Manage Medicines <i class="bi bi-arrow-right ms-1"></i>
                    </a>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card card-hover h-100">
                <div class="card-body p-4">
                    <span class="stat-icon green mb-3"><i class="bi bi-clipboard2-heart"></i></span>
                    <h5 class="mt-3">Personal Health Record</h5>
                    <p class="text-muted">Keep your blood group, vitals, allergies, and emergency contact up to date.</p>
                    <a href="${pageContext.request.contextPath}/health-record" class="btn btn-primary btn-sm px-3">
                        View Health Record <i class="bi bi-arrow-right ms-1"></i>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="includes/footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
