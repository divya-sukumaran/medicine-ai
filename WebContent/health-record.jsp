<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Health Record | AI Health Assistant</title>
    <%@ include file="includes/head.jsp" %>
</head>
<body>

<%@ include file="includes/navbar.jsp" %>

<div class="container py-4" style="max-width: 960px;">
    <div class="mb-4">
        <h3 class="page-title"><i class="bi bi-clipboard2-pulse me-2" style="color:#2563EB;"></i>Personal Health Record</h3>
        <p class="page-subtitle mb-0">Vital information kept handy for you and for emergencies.</p>
    </div>

    <c:if test="${param.saved == '1'}">
        <div class="alert alert-success d-flex align-items-center gap-2">
            <i class="bi bi-check-circle"></i>Your health record has been saved successfully.
        </div>
    </c:if>

    <%-- Summary of the saved record, shown once a record exists --%>
    <c:if test="${not empty record}">
        <div class="row g-3 mb-4">
            <div class="col-lg-3 col-sm-6">
                <div class="record-chip">
                    <span class="chip-icon red"><i class="bi bi-droplet-fill"></i></span>
                    <div>
                        <p class="chip-label mb-0">Blood Group</p>
                        <p class="chip-value mb-0">${record.bloodGroup}</p>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 col-sm-6">
                <div class="record-chip">
                    <span class="chip-icon blue"><i class="bi bi-rulers"></i></span>
                    <div>
                        <p class="chip-label mb-0">Height</p>
                        <p class="chip-value mb-0">${record.height}</p>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 col-sm-6">
                <div class="record-chip">
                    <span class="chip-icon green"><i class="bi bi-speedometer2"></i></span>
                    <div>
                        <p class="chip-label mb-0">Weight</p>
                        <p class="chip-value mb-0">${record.weight}</p>
                    </div>
                </div>
            </div>
            <div class="col-lg-3 col-sm-6">
                <div class="record-chip">
                    <span class="chip-icon amber"><i class="bi bi-telephone-fill"></i></span>
                    <div>
                        <p class="chip-label mb-0">Emergency</p>
                        <p class="chip-value mb-0">${record.emergencyContact}</p>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

    <div class="card">
        <div class="card-body p-4">
            <h5 class="mb-3">
                <c:choose>
                    <c:when test="${empty record}">Fill in your health record</c:when>
                    <c:otherwise>Update your details</c:otherwise>
                </c:choose>
            </h5>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger d-flex align-items-center gap-2">
                    <i class="bi bi-exclamation-circle"></i><%= request.getAttribute("error") %>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/health-record" method="post" id="healthRecordForm" novalidate>
                <div class="row g-3">
                    <div class="col-md-4">
                        <label class="form-label" for="bloodGroup">Blood Group</label>
                        <select class="form-select" id="bloodGroup" name="bloodGroup" required>
                            <option value="" disabled ${empty record.bloodGroup ? 'selected' : ''}>Select</option>
                            <option value="A+" ${record.bloodGroup == 'A+' ? 'selected' : ''}>A+</option>
                            <option value="A-" ${record.bloodGroup == 'A-' ? 'selected' : ''}>A-</option>
                            <option value="B+" ${record.bloodGroup == 'B+' ? 'selected' : ''}>B+</option>
                            <option value="B-" ${record.bloodGroup == 'B-' ? 'selected' : ''}>B-</option>
                            <option value="AB+" ${record.bloodGroup == 'AB+' ? 'selected' : ''}>AB+</option>
                            <option value="AB-" ${record.bloodGroup == 'AB-' ? 'selected' : ''}>AB-</option>
                            <option value="O+" ${record.bloodGroup == 'O+' ? 'selected' : ''}>O+</option>
                            <option value="O-" ${record.bloodGroup == 'O-' ? 'selected' : ''}>O-</option>
                        </select>
                        <div class="invalid-feedback">Please select a blood group.</div>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label" for="height">Height</label>
                        <input type="text" class="form-control" id="height" name="height"
                               placeholder="e.g. 170 cm" value="${record.height}" required>
                        <div class="invalid-feedback">Please enter your height.</div>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label" for="weight">Weight</label>
                        <input type="text" class="form-control" id="weight" name="weight"
                               placeholder="e.g. 68 kg" value="${record.weight}" required>
                        <div class="invalid-feedback">Please enter your weight.</div>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="allergies">Allergies <span class="text-muted fw-normal">(optional)</span></label>
                        <input type="text" class="form-control" id="allergies" name="allergies"
                               placeholder="e.g. Dust, Pollen" value="${record.allergies}">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label" for="emergencyContact">Emergency Contact</label>
                        <input type="tel" class="form-control" id="emergencyContact" name="emergencyContact"
                               placeholder="10-digit mobile number" pattern="[0-9]{10}"
                               value="${record.emergencyContact}" required>
                        <div class="invalid-feedback">Please enter a valid 10-digit contact number.</div>
                    </div>
                    <div class="col-12">
                        <label class="form-label" for="medicalHistory">Medical History <span class="text-muted fw-normal">(optional)</span></label>
                        <textarea class="form-control" id="medicalHistory" name="medicalHistory" rows="4"
                                  placeholder="Past surgeries, ongoing conditions, medications...">${record.medicalHistory}</textarea>
                    </div>
                </div>

                <div class="mt-4">
                    <button type="submit" class="btn btn-primary px-4">
                        <i class="bi bi-check-lg me-1"></i>Save Health Record
                    </button>
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
