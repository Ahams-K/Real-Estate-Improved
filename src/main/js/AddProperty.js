import { csrfToken, csrfHeader } from './util/csrf.js';
import Joi from 'joi';

// Get current date in YYYY-MM-DD format
const today = new Date().toISOString().split('T')[0];

const formSchema = Joi.object({
    propertyName: Joi.string().min(5).max(50).required().messages({
        'string.min': 'Property name must be at least 5 characters long',
        'string.max': 'Property name cannot exceed 50 characters',
        'any.required': 'Property name is required',
    }),
    address: Joi.string().min(10).max(100).required().messages({
        'string.min': 'Address must be at least 10 characters long',
        'string.max': 'Address cannot exceed 100 characters',
        'any.required': 'Address is required',
    }),
    price: Joi.number().positive().required().messages({
        'number.positive': 'Price must be a positive number',
        'any.required': 'Price is required',
        'number.base': 'Price must be a number',
    }),
    type: Joi.string().required().messages({
        'any.required': 'Type is required',
    }),
    size: Joi.number().positive().required().messages({
        'number.positive': 'Size must be a positive number',
        'any.required': 'Size is required',
        'number.base': 'Size must be a number',
    }),
    status: Joi.string().required().messages({
        'any.required': 'Status is required',
    }),
    numberOfRooms: Joi.number().integer().min(1).required().messages({
        'number.min': 'Number of rooms must be at least 1',
        'any.required': 'Number of rooms is required',
        'number.base': 'Number of rooms must be a number',
    }),
    dateListed: Joi.date()
        .max(today) // Dynamically set to today's date
        .required()
        .messages({
            'date.max': 'Date listed cannot be in the future',
            'any.required': 'Date listed is required',
            'date.base': 'Date listed must be a valid date (YYYY-MM-DD)',
        }),
});

const propertyForm = document.querySelector("#add-property-propertyForm");
if (!propertyForm) {
    console.error('Property form not found');
} else {
    propertyForm.addEventListener('submit', async evt => {
        evt.preventDefault();

        // Clear previous errors and invalid states
        document.querySelectorAll('.invalid-feedback').forEach(errorDiv => {
            errorDiv.textContent = '';
        });
        document.querySelectorAll('.form-control, .form-select').forEach(input => {
            input.classList.remove('is-invalid');
        });

        const formData = {
            propertyName: document.querySelector('#propertyName').value.trim(),
            address: document.querySelector('#address').value.trim(),
            price: Number(document.querySelector('#price').value),
            type: document.querySelector('#type').value,
            size: Number(document.querySelector('#size').value),
            status: document.querySelector('#status').value,
            numberOfRooms: Number(document.querySelector('#numberOfRooms').value),
            dateListed: document.querySelector('#dateListed').value,
        };

        // Validate the form data
        const { error } = formSchema.validate(formData, { abortEarly: false });
        if (error) {
            error.details.forEach(detail => {
                const field = detail.path[0];
                const errorDiv = document.querySelector(`#${field}-error`);
                const input = document.querySelector(`#${field}`);
                if (errorDiv && input) {
                    errorDiv.textContent = detail.message;
                    input.classList.add('is-invalid');
                }
            });
            return;
        }

        const json = JSON.stringify(formData);

        try {
            const result = await fetch('/api/properties', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: json
            });

            if (result.status === 201) {
                const property = await result.json();
                alert('Your property got created');
                window.location = `/properties/${property.propertyId}`;
            } else {
                console.error('Failed to create property, status:', result.status);
                alert('Failed to create property');
            }
        } catch (error) {
            console.error('Error during fetch:', error);
            alert('Failed to create property due to network error');
        }
    });
}