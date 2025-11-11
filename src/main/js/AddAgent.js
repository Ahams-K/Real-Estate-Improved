const agentForm = document.querySelector('#add-agent-agentForm');
agentForm.addEventListener('submit', async e => {
    e.preventDefault();
    const agentName = document.querySelector('#agentName').value;
    const contactInfo = document.querySelector('#contactInfo').value;
    const licenceNumber = document.querySelector('#licenceNumber').value;
    const email = document.querySelector('#email').value;


    const jsonBody = JSON.stringify({
        agentName,
        contactInfo,
        licenceNumber,
        email
    });
    const response = await fetch('/api/agents', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: jsonBody
    });
    if (response.status === 201) {
        const agent = await response.json();
        alert(`Congrats, your agent got created. It has ID #${agent.agentId}`);
        window.location = `/agents/${agent.agentId}`;
    } else {
        alert('Something went wrong');
    }
});