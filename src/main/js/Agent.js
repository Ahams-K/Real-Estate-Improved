const loadProperties = document.querySelector('#load-properties');
const assignedProperties = document.querySelector('#managed-properties');
loadProperties.addEventListener('click', async evt => {
    const agentId = loadProperties.getAttribute('data-agent-id');
    loadProperties.disabled = true;
    const result = await fetch(`/api/agents/${agentId}/properties`, {
        headers: {
            Accept: 'application/json'
        }
    });
    loadProperties.disabled = false;
    if (result.status === 200){
        const properties = await result.json();
        assignedProperties.innerHTML = '<ul>'
        + properties.map(property => `<li><a href="/properties/${property.propertyId}">${property.propertyName}</a></li>`).join('')
        + '</ul>';
    } else {
        alert('Failed to load properties');
    }
})