package be.kdg.prog5.RealEstateSystem.domain.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    private NotFoundException(String message) {
        super(message);
    }


    public static NotFoundException forAgent(final UUID agentId){
      return new NotFoundException("Agent with ID %s was not found".formatted(agentId));
    }

    public static NotFoundException forProperty(final UUID propertyId){
      return new NotFoundException(String.format("Property with ID %s was not found", propertyId));
    }

    public static NotFoundException forAgency(final UUID agencyId) {
        return new NotFoundException("Agency with ID %s was not found".formatted(agencyId));
    }
}
