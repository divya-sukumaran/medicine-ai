<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Add Medicine | AI Health Assistant</title>
    <%@ include file="includes/head.jsp" %>
</head>
<body>

<%@ include file="includes/navbar.jsp" %>

<div class="container py-4" style="max-width: 860px;">
    <div class="mb-4">
        <h3 class="page-title"><i class="bi bi-capsule me-2" style="color:#2563EB;"></i>Add Medicine</h3>
        <p class="page-subtitle mb-0">Set up a new reminder with dosage and timing.</p>
    </div>

    <div class="card">
        <div class="card-body p-4">
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger d-flex align-items-center gap-2">
                    <i class="bi bi-exclamation-circle"></i><%= request.getAttribute("error") %>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/medicines" method="post" id="medicineForm" novalidate>
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label" for="medicineName">Medicine Name</label>
                        <input type="text" class="form-control" id="medicineName" name="medicineName"
                               placeholder="e.g. Paracetamol" required>
                        <div class="invalid-feedback">Please enter the medicine name.</div>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label" for="dosage">Dosage</label>
                        <input type="text" class="form-control" id="dosage" name="dosage" placeholder="e.g. 500mg" required>
                        <div class="invalid-feedback">Please enter the dosage.</div>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label" for="time">Time</label>
                        <input type="time" class="form-control" id="time" name="time" required>
                        <div class="invalid-feedback">Please select a time.</div>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="startDate">Start Date</label>
                        <input type="date" class="form-control" id="startDate" name="startDate" required>
                        <div class="invalid-feedback">Please select a start date.</div>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="endDate">End Date</label>
                        <input type="date" class="form-control" id="endDate" name="endDate" required>
                        <div class="invalid-feedback">Please select an end date.</div>
                    </div>
                    <div class="col-12">
                        <label class="form-label" for="notes">Notes <span class="text-muted fw-normal">(optional)</span></label>
                        <textarea class="form-control" id="notes" name="notes" rows="3"
                                  placeholder="e.g. Take after breakfast"></textarea>
                    </div>
                </div>

                <div class="mt-4 d-flex gap-2">
                    <button type="submit" class="btn btn-primary px-4">
                        <i class="bi bi-check-lg me-1"></i>Save Medicine
                    </button>
                    <a href="${pageContext.request.contextPath}/medicines" class="btn btn-outline-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="includes/footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
