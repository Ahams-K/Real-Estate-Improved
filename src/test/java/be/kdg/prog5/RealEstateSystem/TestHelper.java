package be.kdg.prog5.RealEstateSystem;

import be.kdg.prog5.RealEstateSystem.domain.*;
import be.kdg.prog5.RealEstateSystem.domain.exception.NotFoundException;
import be.kdg.prog5.RealEstateSystem.repository.AgentPropertyRepository;
import be.kdg.prog5.RealEstateSystem.repository.AgentRepository;
import be.kdg.prog5.RealEstateSystem.repository.PropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
public class TestHelper {
    private static final Logger logger = LoggerFactory.getLogger(TestHelper.class);

    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private AgentPropertyRepository agentPropertyRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void cleanUp() {
        logger.debug("Cleaning up test data");
        agentPropertyRepository.deleteAll();
        agentRepository.deleteAll();
        propertyRepository.deleteAll();
    }

    public Property createProperty() {
        Property property = new Property();
        property.setNumberOfRooms(3);
        property.setPrice(250000.0);
        property.setSize(100.0);
        property.setStatus(PropertyStatus.valueOf("AVAILABLE"));
        property.setType(PropertyType.valueOf("RESIDENTIAL"));
        logger.debug("Creating property: {}", property);
        return propertyRepository.save(property);
    }

    public Agent createAgent() {
        return createAgent("gloria@example.com", Role.USER);
    }

    public Agent createAgent(String email) {
        return createAgent(email, Role.USER);
    }

    public Agent createAgent(String email, Role role) {
        Agent agent = new Agent();
        agent.setAgentName("Agent " + email.split("@")[0]);
        agent.setEmail(email);
        agent.setContactInfo("+320000000");
        agent.setLicenceNumber("NON-" + UUID.randomUUID().toString().substring(0, 4));
        agent.setRole(role);
        String encodedPassword = passwordEncoder.encode("password");
        agent.setPassword(encodedPassword);
        logger.debug("Creating agent: email={}, role={}, passwordSet={}", email, role, encodedPassword != null);
        return agentRepository.save(agent);
    }

    public AgentProperty propertyOwnedByAgent(Agent agent, Property property) {
        AgentProperty agentProperty = new AgentProperty();
        agentProperty.setProperty(property);
        agentProperty.setAgent(agent);
        logger.debug("Linking agent {} to property {}", agent.getEmail(), property.getPropertyId());
        return agentPropertyRepository.save(agentProperty);
    }
}
