package be.kdg.prog5.RealEstateSystem.service;

import be.kdg.prog5.RealEstateSystem.domain.Property;
import be.kdg.prog5.RealEstateSystem.domain.PropertyStatus;
import be.kdg.prog5.RealEstateSystem.domain.PropertyType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PropertyServiceInterface {
    List<Property> getAll();

    Property findPropertyById(UUID propertyId);

    boolean isAgentAssigned(final UUID propertyId, final UUID agentId);

    List<Property> getFilteredProperties(Double minPrice, Double maxPrice, PropertyType type, PropertyStatus status);
}
