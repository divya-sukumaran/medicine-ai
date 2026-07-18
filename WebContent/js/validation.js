/**
 * Client-side (frontend) validation for the AI Health Assistant forms.
 * Uses Bootstrap 5's built-in validation styles together with the
 * browser's native form constraints (required, pattern, minlength, etc).
 * This does NOT replace server-side validation -- it only gives the
 * user faster feedback before the form is submitted.
 */
(function () {
    "use strict";

    function attachValidation(formId) {
        var form = document.getElementById(formId);
        if (!form) {
            return;
        }

        form.addEventListener("submit", function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add("was-validated");
        });
    }

    document.addEventListener("DOMContentLoaded", function () {
        attachValidation("loginForm");
        attachValidation("registerForm");
        attachValidation("verifyOtpForm");
        attachValidation("setPasswordForm");
        attachValidation("medicineForm");
        attachValidation("healthRecordForm");
    });
})();
