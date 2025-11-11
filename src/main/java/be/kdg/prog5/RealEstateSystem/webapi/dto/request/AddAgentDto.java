package be.kdg.prog5.RealEstateSystem.webapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddAgentDto(
        @NotBlank(message = "Agent name is requires") @Size(min = 2, max = 30, message = "Agent name must be between 2 and 30 characters. ")
        String agentName,
        @NotBlank(message = "Agent contact information is required")
        String contactInfo,
        @NotBlank(message = "Agent licence number is required")
        String licenceNumber,
        @NotBlank(message = "Agent email is required")
        @Email(message = "Invalid email format")
        String email

) {

}
