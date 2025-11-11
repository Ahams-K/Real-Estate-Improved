package be.kdg.prog5.RealEstateSystem.webapi.dto.request;

import be.kdg.prog5.RealEstateSystem.domain.PropertyStatus;
import be.kdg.prog5.RealEstateSystem.domain.PropertyType;

import java.time.LocalDate;
import java.util.UUID;

public record PatchPropertyDto(String propertyName, String address, double price, PropertyType type, double size, PropertyStatus status, int numberOfRooms, LocalDate datelisted) {
}
