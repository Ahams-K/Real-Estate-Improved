import { csrfToken, csrfHeader } from './util/csrf.js';

document.addEventListener('DOMContentLoaded', () => {
    const updateForm = document.getElementById('update-agent-form');
    const updateButton = document.getElementById('update-agent-button');

    // Track original values
    const originalValues = {
        contactInfo: document.getElementById('contact-info').value,
        licenceNumber: document.getElementById('licence-number').value,
        email: document.getElementById('email').value
    };

    // Enable/disable update button based on changes
    const checkChanges = () => {
        const hasChanges =
            document.getElementById('contact-info').value !== originalValues.contactInfo ||
            document.getElementById('licence-number').value !== originalValues.licenceNumber ||
            document.getElementById('email').value !== originalValues.email;

        updateButton.disabled = !hasChanges;
    };

    // Add input listeners
    ['contact-info', 'licence-number', 'email'].forEach(id => {
        document.getElementById(id).addEventListener('input', checkChanges);
    });

    updateForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const agentId = updateForm.getAttribute('data-agent-id');
        updateButton.disabled = true;
        updateButton.textContent = 'Updating...';

        try {
            const response = await fetch(`/api/agents/${agentId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({
                    contactInfo: document.getElementById('contact-info').value,
                    licenceNumber: document.getElementById('licence-number').value,
                    email: document.getElementById('email').value
                })
            });

            if (response.ok) {
                // Update successful - refresh the page to show changes
                window.location.href = `/agents`;
            } else {
                const error = await response.json();
                alert(`Error: ${error.message || 'Failed to update agent'}`);
                updateButton.textContent = 'Update Agent';
                updateButton.disabled = false;
            }
        } catch (error) {
            alert(`Network error: ${error.message}`);
            updateButton.textContent = 'Update Agent';
            updateButton.disabled = false;
        }
    });

    // Initial check
    checkChanges();
});