import { csrfToken, csrfHeader } from './util/csrf.js';
import { animate } from 'animejs';

const deleteAgentButton = document.querySelectorAll('.remove-agent-button');
deleteAgentButton.forEach(deleteAgentsButton => {
    deleteAgentsButton.addEventListener('click', async () => {
        deleteAgentsButton.disabled = true;
        const agentId = deleteAgentsButton.getAttribute('data-agent-id');
        const fetchUrl = `/api/agents/${encodeURIComponent(agentId)}`;
        const response = await fetch(fetchUrl, {
            method: 'DELETE',
            headers: {
                [csrfHeader]: csrfToken
            }
        });
        if (response.status === 204) {
            const agentElement = document.querySelector(`#agent-${agentId}`);
            if (!agentElement) {
                deleteAgentsButton.disabled = false;
                return;
            }
            animate(agentElement, {
                opacity: 0,
                duration: 1000,
                easing: 'easeOutQuad',
                onComplete: () => {
                    agentElement.remove();
                    deleteAgentsButton.disabled = false;
                }
            });
        } else {
            deleteAgentsButton.disabled = false;
            alert('something went wrong');
        }
    });
});