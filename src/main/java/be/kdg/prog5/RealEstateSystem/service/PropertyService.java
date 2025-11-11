package be.kdg.prog5.RealEstateSystem.service;

import be.kdg.prog5.RealEstateSystem.domain.*;
import be.kdg.prog5.RealEstateSystem.domain.exception.NotFoundException;
import be.kdg.prog5.RealEstateSystem.repository.AgentPropertyRepository;
import be.kdg.prog5.RealEstateSystem.repository.AgentRepository;
import be.kdg.prog5.RealEstateSystem.repository.PropertyRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PropertyService implements PropertyServiceInterface {
    private final PropertyRepository propertyRepository;
    private final AgentRepository agentRepository;
    private final AgentPropertyRepository agentPropertyRepository;

    public PropertyService(PropertyRepository propertyRepository, AgentRepository agentRepository, AgentPropertyRepository agentPropertyRepository) {
        this.propertyRepository = propertyRepository;
        this.agentRepository = agentRepository;
        this.agentPropertyRepository = agentPropertyRepository;
    }

    @Override
    public List<Property> getAll(){
        return propertyRepository.findAllWithAgents();
    }

    public Property findPropertyById(UUID propertyId){
        return propertyRepository.findById(propertyId).orElseThrow(() -> NotFoundException.forProperty(propertyId));
    }

    public void remove(final UUID propertyId) {
        final Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> NotFoundException.forProperty(propertyId));
        agentPropertyRepository.deleteAll(property.getAgentProperties());
        propertyRepository.deleteById(propertyId);

    }

    public List<Agent> getAgents(final UUID id) {
        return propertyRepository.findPropertiesWithAssigned(id)
                .orElseThrow(() -> NotFoundException.forProperty(id))
                .getAgentProperties()
                .stream()
                .map(AgentProperty::getAgent)
                .toList();
    }

    public Property add(final String propertyName, final String address, final double price, final PropertyType type, final double size, final PropertyStatus status, final int numberOfRooms,final LocalDate dateListed, final UUID agentId) {
        final Property property = new Property();
        property.setPropertyName(propertyName);
        property.setAddress(address);
        property.setPrice(price);
        property.setType(type);
        property.setSize(size);
        property.setStatus(status);
        property.setNumberOfRooms(numberOfRooms);
        property.setDateListed(dateListed);
        var createdProperty = propertyRepository.save(property);
        var agent = agentRepository.findById(agentId).orElseThrow(() -> NotFoundException.forAgent(agentId));
        var agentProperty = new AgentProperty();
        agentProperty.setProperty(createdProperty);
        agentProperty.setAgent(agent);
        agentPropertyRepository.save(agentProperty);
        return createdProperty;
    }

    public Property patch(final UUID propertyId, final String propertyName, final String address, final double price, final PropertyType type, final double size, final PropertyStatus status, final int numberOfRooms, final LocalDate datelisted){
        final Property property = findPropertyById(propertyId);
        if (propertyName != null){
            property.setPropertyName(propertyName);
        }
        if (address != null){
            property.setAddress(address);
        }
        if (price!= 0){
            property.setPrice(price);
        }
        if (type!= null){
            property.setType(type);
        }
        if (size!= 0){
            property.setSize(size);
        }
        if (status!= null){
            property.setStatus(status);
        }
        if (numberOfRooms!= 0){
            property.setNumberOfRooms(numberOfRooms);
        }
        if (datelisted!= null){
            property.setDateListed(datelisted);
        }
        return property;
    }

    public boolean isAgentAssigned(final UUID propertyId, final UUID agentId) {
        return propertyRepository.findPropertiesWithAssigned(propertyId)
                .orElseThrow(() -> NotFoundException.forProperty(propertyId))
                .getAgentProperties()
                .stream()
                .anyMatch(ap -> ap.getAgent().getAgentId().equals(agentId));
    }

    public List<Property> getFilteredProperties(Double minPrice, Double maxPrice, PropertyType type, PropertyStatus status){
        return propertyRepository.findFilteredProperties(minPrice, maxPrice, type, status);
    }



    public Property addPublic(final String propertyName, final String address, final double price, final PropertyType type,
                              final double size, final PropertyStatus status, final int numberOfRooms, final LocalDate dateListed) {
        UUID defaultAgentId = agentRepository.findAll().stream()
                .findFirst()
                .map(Agent::getAgentId)
                .orElseThrow(() -> new IllegalStateException("No agents found in the database. Please add an agent first."));
        return add(propertyName, address, price, type, size, status, numberOfRooms, dateListed, defaultAgentId);
    }

}
