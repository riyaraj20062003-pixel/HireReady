/**
 * HireReady - Dynamic Client-side Helpers
 * Manages repeatable form sections (Education, Skills, Experience, Projects, Certs, Achievements)
 * and confirmation prompts.
 */

document.addEventListener('DOMContentLoaded', function () {
    // Auto-dismiss flash alerts after 6 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(function () {
                if (alert.parentNode) {
                    alert.parentNode.removeChild(alert);
                }
            }, 500);
        }, 6000);
    });
});

/**
 * Confirms dangerous actions like deleting a resume or user.
 */
function confirmAction(message) {
    return confirm(message || "Are you sure you want to proceed with this action?");
}

/**
 * Triggers standard browser print dialog for printing or exporting resume to PDF.
 */
function printResume() {
    window.print();
}

/**
 * Removes a dynamic repeater row.
 */
function removeRow(button) {
    const item = button.closest('.repeater-item');
    if (item) {
        const container = item.parentElement;
        // Keep at least one empty row or allow removal
        item.remove();
    }
}

/**
 * Adds an Education entry row
 */
function addEducationRow() {
    const container = document.getElementById('educationContainer');
    const div = document.createElement('div');
    div.className = 'repeater-item';
    div.innerHTML = `
        <div class="repeater-header">
            <span class="repeater-title">Education Entry</span>
            <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Remove</button>
        </div>
        <div class="form-row">
            <div class="form-group">
                <label class="form-label">Degree / Qualification <span class="required">*</span></label>
                <input type="text" name="degree[]" class="form-control" placeholder="e.g. B.Tech in Computer Science" required>
            </div>
            <div class="form-group">
                <label class="form-label">School / Institution</label>
                <input type="text" name="institution[]" class="form-control" placeholder="e.g. Stanford University / ABC College">
            </div>
        </div>
        <div class="form-row">
            <div class="form-group">
                <label class="form-label">Passing Year</label>
                <input type="number" name="passingYear[]" class="form-control" placeholder="e.g. 2024" min="1950" max="2099">
            </div>
            <div class="form-group">
                <label class="form-label">CGPA / Percentage</label>
                <input type="number" step="0.01" name="cgpa[]" class="form-control" placeholder="e.g. 8.50 or 85.5">
            </div>
        </div>
    `;
    container.appendChild(div);
}

/**
 * Adds a Skill Category row
 */
function addSkillRow() {
    const container = document.getElementById('skillsContainer');
    const div = document.createElement('div');
    div.className = 'repeater-item';
    div.innerHTML = `
        <div class="repeater-header">
            <span class="repeater-title">Skill Category</span>
            <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Remove</button>
        </div>
        <div class="form-row">
            <div class="form-group">
                <label class="form-label">Category Name</label>
                <input type="text" name="skillCategory[]" class="form-control" placeholder="e.g. Programming Languages, Frameworks, Tools">
            </div>
            <div class="form-group">
                <label class="form-label">Skills (Comma-separated) <span class="required">*</span></label>
                <input type="text" name="skillNames[]" class="form-control" placeholder="e.g. Java, Python, MySQL, Git, Docker" required>
            </div>
        </div>
    `;
    container.appendChild(div);
}

/**
 * Adds an Experience entry row
 */
function addExperienceRow() {
    const container = document.getElementById('experienceContainer');
    const div = document.createElement('div');
    div.className = 'repeater-item';
    div.innerHTML = `
        <div class="repeater-header">
            <span class="repeater-title">Experience Entry</span>
            <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Remove</button>
        </div>
        <div class="form-row">
            <div class="form-group">
                <label class="form-label">Company / Organization <span class="required">*</span></label>
                <input type="text" name="company[]" class="form-control" placeholder="e.g. Google, Infosys, Tech Corp" required>
            </div>
            <div class="form-group">
                <label class="form-label">Job Role / Title</label>
                <input type="text" name="jobRole[]" class="form-control" placeholder="e.g. Software Engineer Intern">
            </div>
        </div>
        <div class="form-group">
            <label class="form-label">Duration</label>
            <input type="text" name="duration[]" class="form-control" placeholder="e.g. Jan 2024 - Present or 6 Months">
        </div>
        <div class="form-group">
            <label class="form-label">Job Responsibilities / Description</label>
            <textarea name="expDescription[]" class="form-control" rows="2" placeholder="Key responsibilities and achievements..."></textarea>
        </div>
    `;
    container.appendChild(div);
}

/**
 * Adds a Project entry row
 */
function addProjectRow() {
    const container = document.getElementById('projectsContainer');
    const div = document.createElement('div');
    div.className = 'repeater-item';
    div.innerHTML = `
        <div class="repeater-header">
            <span class="repeater-title">Project Entry</span>
            <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Remove</button>
        </div>
        <div class="form-row">
            <div class="form-group">
                <label class="form-label">Project Title <span class="required">*</span></label>
                <input type="text" name="projectTitle[]" class="form-control" placeholder="e.g. Online Resume Builder" required>
            </div>
            <div class="form-group">
                <label class="form-label">Technologies Used</label>
                <input type="text" name="technologies[]" class="form-control" placeholder="e.g. Java, JSP, Servlets, MySQL">
            </div>
        </div>
        <div class="form-group">
            <label class="form-label">Project Link (GitHub / Live Demo)</label>
            <input type="url" name="projectLink[]" class="form-control" placeholder="https://github.com/username/project">
        </div>
        <div class="form-group">
            <label class="form-label">Project Description</label>
            <textarea name="projectDescription[]" class="form-control" rows="2" placeholder="Short description of the project features..."></textarea>
        </div>
    `;
    container.appendChild(div);
}

/**
 * Adds a Certification entry row
 */
function addCertificationRow() {
    const container = document.getElementById('certificationsContainer');
    const div = document.createElement('div');
    div.className = 'repeater-item';
    div.innerHTML = `
        <div class="repeater-header">
            <span class="repeater-title">Certification Entry</span>
            <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Remove</button>
        </div>
        <div class="form-row">
            <div class="form-group">
                <label class="form-label">Certification Name <span class="required">*</span></label>
                <input type="text" name="certName[]" class="form-control" placeholder="e.g. AWS Certified Cloud Practitioner" required>
            </div>
            <div class="form-group">
                <label class="form-label">Issuing Organization</label>
                <input type="text" name="issuingOrg[]" class="form-control" placeholder="e.g. Amazon Web Services, Oracle">
            </div>
            <div class="form-group">
                <label class="form-label">Year</label>
                <input type="number" name="issueYear[]" class="form-control" placeholder="e.g. 2023" min="1950" max="2099">
            </div>
        </div>
    `;
    container.appendChild(div);
}

/**
 * Adds an Achievement entry row
 */
function addAchievementRow() {
    const container = document.getElementById('achievementsContainer');
    const div = document.createElement('div');
    div.className = 'repeater-item';
    div.innerHTML = `
        <div class="repeater-header">
            <span class="repeater-title">Achievement Entry</span>
            <button type="button" class="btn btn-danger btn-sm" onclick="removeRow(this)">Remove</button>
        </div>
        <div class="form-group">
            <label class="form-label">Achievement Title <span class="required">*</span></label>
            <input type="text" name="achievementTitle[]" class="form-control" placeholder="e.g. 1st Place at National Hackathon" required>
        </div>
        <div class="form-group">
            <label class="form-label">Description</label>
            <textarea name="achievementDescription[]" class="form-control" rows="2" placeholder="Brief details about the recognition or award..."></textarea>
        </div>
    `;
    container.appendChild(div);
}
