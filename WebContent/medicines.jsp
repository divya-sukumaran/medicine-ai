<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Medicines | AI Health Assistant</title>
    <%@ include file="includes/head.jsp" %>
</head>
<body>

<%@ include file="includes/navbar.jsp" %>

<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
        <div>
            <h3 class="page-title">My Medicines</h3>
            <p class="page-subtitle mb-0">All your medicine reminders in one place.</p>
        </div>
        <a href="${pageContext.request.contextPath}/add-medicine.jsp" class="btn btn-primary">
            <i class="bi bi-plus-lg me-1"></i>Add Medicine
        </a>
    </div>

    <div class="card">
        <div class="card-body">
            <c:choose>
                <c:when test="${empty medicines}">
                    <div class="empty-state">
                        <i class="bi bi-capsule"></i>
                        <p class="fw-semibold mb-1">No medicines yet</p>
                        <p class="mb-3">Add your first medicine to start getting organized.</p>
                        <a href="${pageContext.request.contextPath}/add-medicine.jsp" class="btn btn-primary btn-sm px-3">
                            <i class="bi bi-plus-lg me-1"></i>Add Medicine
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Medicine</th>
                                    <th>Dosage</th>
                                    <th>Time</th>
                                    <th>Start Date</th>
                                    <th>End Date</th>
                                    <th>Notes</th>
                                    <th class="text-end">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="medicine" items="${medicines}">
                                    <tr>
                                        <td class="fw-semibold">${medicine.medicineName}</td>
                                        <td><span class="badge-soft-blue">${medicine.dosage}</span></td>
                                        <td><span class="badge-soft-green"><i class="bi bi-clock me-1"></i>${medicine.time}</span></td>
                                        <td>${medicine.startDate}</td>
                                        <td>${medicine.endDate}</td>
                                        <td class="text-muted">${medicine.notes}</td>
                                        <td class="text-end">
                                            <a href="${pageContext.request.contextPath}/medicines?action=edit&id=${medicine.id}"
                                               class="btn btn-sm btn-outline-primary" title="Edit">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/medicines?action=delete&id=${medicine.id}"
                                               class="btn btn-sm btn-outline-danger" title="Delete"
                                               onclick="return confirm('Delete this medicine reminder?');">
                                                <i class="bi bi-trash"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<%@ include file="includes/footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
