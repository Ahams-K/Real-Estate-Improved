package be.kdg.prog5.RealEstateSystem.webapi.dto.request;

import be.kdg.prog5.RealEstateSystem.domain.PropertyStatus;
import be.kdg.prog5.RealEstateSystem.domain.PropertyType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record AddPropertyDto(

        @NotEmpty(message = "Property name is required")
        String propertyName,

        @NotBlank(message = "Property address is required")
        @Size(min = 10, max = 50, message = "Property address is required")
        String address,

        @NotNull(message = "Property price is required")
        @DecimalMax(value = "500000000000.00", message = "Property price must not exceed 500 billion")
        @DecimalMin(value = "0.01", message = "Property price must be greater than 0")
        double price,

        @NotNull(message = "Property type is required")
        PropertyType type,

        @DecimalMax(value = "500000.00", message = "Property size must not exceed 500,000 square meters")
        @DecimalMin(value = "1.00", message = "Property size must be at least 1 square meter")
        double size,

        @NotNull(message = "Property status is required")
        PropertyStatus status,

        @Min(value = 1, message = "Number of rooms must be at least 1")
        @Max(value = 3000, message = "Number of rooms must not exceed 3000")
        int numberOfRooms,

        @PastOrPresent(message = "The date listed must be in the past or present")
        LocalDate dateListed

) {
}
