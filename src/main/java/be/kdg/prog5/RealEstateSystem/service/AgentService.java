package be.kdg.prog5.RealEstateSystem.service;

import be.kdg.prog5.RealEstateSystem.domain.Agent;
import be.kdg.prog5.RealEstateSystem.domain.AgentProperty;
import be.kdg.prog5.RealEstateSystem.domain.Property;
import be.kdg.prog5.RealEstateSystem.domain.Role;
import be.kdg.prog5.RealEstateSystem.domain.exception.NotFoundException;
import be.kdg.prog5.RealEstateSystem.repository.AgentPropertyRepository;
import be.kdg.prog5.RealEstateSystem.repository.AgentRepository;
import be.kdg.prog5.RealEstateSystem.repository.PropertyRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.PropertyResourceBundle;
import java.util.UUID;

@Service
@Transactional
public class AgentService implements AgentServiceInterface {

    private final AgentRepository agentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AgentPropertyRepository agentPropertyRepository;
    private final PropertyRepository propertyRepository;


    public AgentService(AgentRepository agentRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AgentPropertyRepository agentPropertyRepository, PropertyRepository propertyRepository) {
        this.agentRepository = agentRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.agentPropertyRepository = agentPropertyRepository;
        this.propertyRepository = propertyRepository;
    }

        @Override
        public List<Agent> getALL() {
        return agentRepository.findAll();
        }


        public Agent findAgentById(UUID agentId) {
            return agentRepository.findById(agentId).orElseThrow(() -> NotFoundException.forAgent(agentId));
        }

        public void remove(final UUID agentId) {
        final Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> NotFoundException.forAgent(agentId));
        agentPropertyRepository.deleteAll(agent.getAgentProperties());
        agentRepository.deleteById(agentId);
        }

    public List<Property> getProperties(final UUID id) {
        return agentRepository.findAgentWithAssignedProperties(id)
                .orElseThrow(() -> NotFoundException.forAgent(id))
                .getAgentProperties()
                .stream()
                .map(AgentProperty::getProperty)
                .toList();
    }

    public Agent add(final String agentName, final String password, final String contactInfo, final String licenceNumber, final String email, final List<UUID> agentPropertiesIds) {
        final Agent agent = new Agent();
        agent.setAgentName(agentName);
        agent.setPassword(bCryptPasswordEncoder.encode(password));
        agent.setContactInfo(contactInfo);
        agent.setLicenceNumber(licenceNumber);
        agent.setRole(Role.USER);
        agent.setEmail(email);
        agent.setAgentProperties(agentPropertiesIds.stream()
                .map(propertyId -> propertyRepository.findById(propertyId).orElseThrow(() -> NotFoundException.forProperty(propertyId)))
                .map(property -> new AgentProperty(agent, property))
                .toList());
        return agentRepository.save(agent);
    }


    public Agent updateAgent(UUID agentId, String contactInfo, String licenceNumber, String email) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> NotFoundException.forAgent(agentId));

        agent.setContactInfo(contactInfo);
        agent.setLicenceNumber(licenceNumber);
        agent.setEmail(email);

        // No need to call save() explicitly with @Transactional
        return agent;
    }
}
