import { csrfToken, csrfHeader } from './util/csrf.js';
import { animate } from 'animejs';

const deletePropertyButton = document.querySelectorAll('.remove-property-button');
deletePropertyButton.forEach(deletePropertiesButton => {
    deletePropertiesButton.addEventListener('click', async () => {
        deletePropertiesButton.disabled = true;
        const propertyId = deletePropertiesButton.getAttribute('data-property-id');
        const fetchUrl = `/api/properties/${encodeURIComponent(propertyId)}`;
        const response = await fetch(fetchUrl, {
            method: 'DELETE',
            headers: {
                [csrfHeader]: csrfToken
            }
        });
        if (response.status === 204) {
            const propertyElement = document.querySelector(`#property-${propertyId}`);
            if (!propertyElement) {
                deletePropertiesButton.disabled = false;
                return;
            }
            animate(propertyElement, {
                opacity: 0,
                duration: 1000,
                easing: 'easeOutQuad',
                onComplete: () => {
                    propertyElement.remove();
                    deletePropertiesButton.disabled = false;
                }
            });
        } else {
            deletePropertiesButton.disabled = false;
            alert('something went wrong');
        }
    });
});