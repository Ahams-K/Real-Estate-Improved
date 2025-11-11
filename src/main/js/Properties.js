import { csrfToken, csrfHeader } from './util/csrf.js'

const loadAgents = document.querySelector('#load-agents');
const assignedAgents = document.querySelector('#assigned-agents');
loadAgents.addEventListener('click', async evt => {
    const propertyId = loadAgents.getAttribute('data-property-id');
    loadAgents.disabled = true;
    const result = await fetch(`/api/properties/${propertyId}/agents`, {
        headers: {
            Accept: 'application/json',
            [csrfHeader]: csrfToken
        }
    });
    loadAgents.disabled = false;
    if (result.status === 200){
        const agents = await result.json();
        assignedAgents.innerHTML = '<ul>'
            + agents.map(agent => `<li><a href="/agents/${agent.agentId}">${agent.agentName}</a></li>`).join('')
            + '</ul>';
    } else {
        alert('Failed to load Agents');
    }
});


const editPropertiesButton = document.querySelector('#edit-property');
const propertyPriceInput = document.querySelector('#property-price');
editPropertiesButton?.addEventListener('click', async evt => {
    const propertyId = editPropertiesButton.getAttribute('data-property-id');
    const result = await fetch(`/api/properties/${propertyId}`, {
        method:"PATCH",
        headers: {
            "Content-Type": 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({
        price: propertyPriceInput.value
        })
    });
    if (result.status === 200){
      editPropertiesButton.disabled = true;
    } else {
        alert('Something went wrong!!!!');
    }
});
propertyPriceInput?.addEventListener("input", () => editPropertiesButton.disabled =  false);