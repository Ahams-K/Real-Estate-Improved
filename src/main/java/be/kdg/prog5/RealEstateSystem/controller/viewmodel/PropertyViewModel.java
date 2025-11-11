package be.kdg.prog5.RealEstateSystem.controller.viewmodel;

import be.kdg.prog5.RealEstateSystem.domain.Property;
import be.kdg.prog5.RealEstateSystem.domain.PropertyStatus;
import be.kdg.prog5.RealEstateSystem.domain.PropertyType;

import java.time.LocalDate;
import java.util.UUID;

public record PropertyViewModel(UUID propertyId, String propertyName, String address, double price, PropertyType type,
                                double size, PropertyStatus status, int numberOfRooms, LocalDate dateListed,
                                String image, boolean isModificationAllowed) {
    public static PropertyViewModel from(final Property property, final boolean isModificationAllowed) {
        return new PropertyViewModel(
                property.getPropertyId(),
                property.getPropertyName(),
                property.getAddress(),
                property.getPrice(),
                property.getType(),
                property.getSize(),
                property.getStatus(),
                property.getNumberOfRooms(),
                property.getDateListed(),
                property.getImage(),
                isModificationAllowed
        );
    }

}
