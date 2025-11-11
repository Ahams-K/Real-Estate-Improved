package be.kdg.prog5.RealEstateSystem.webapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddAgencyDto(
        @NotBlank(message = "Agency name is required")
        @Size(min = 2, max = 50, message = "Agency name must be between 2 and 50 characters")
        String agencyName,

        @NotBlank(message = "Address is required")
        @Size(min = 10, max = 100, message = "Address must be between 10 and 100 characters")
        String address,

        @NotBlank(message = "City is required")
        String city,

        String contactInfo,

        String image
) {}