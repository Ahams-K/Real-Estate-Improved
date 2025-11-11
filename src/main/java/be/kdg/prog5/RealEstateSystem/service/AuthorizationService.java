package be.kdg.prog5.RealEstateSystem.service;

import be.kdg.prog5.RealEstateSystem.domain.Agent;
import be.kdg.prog5.RealEstateSystem.domain.Property;
import be.kdg.prog5.RealEstateSystem.domain.Role;
import be.kdg.prog5.RealEstateSystem.domain.exception.NotFoundException;
import be.kdg.prog5.RealEstateSystem.repository.AgentRepository;
import be.kdg.prog5.RealEstateSystem.repository.PropertyRepository;
import be.kdg.prog5.RealEstateSystem.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class AuthorizationService {
    private final PropertyRepository propertyRepository;
    private final AgentRepository agentRepository;
    public AuthorizationService(PropertyRepository propertyRepository, AgentRepository agentRepository) {
        this.propertyRepository = propertyRepository;
        this.agentRepository = agentRepository;
    }

    public boolean isModificationAllowed(final CustomUserDetails details, final UUID propertyId) {
        log.info("Checking modification permission for property {} by user: agentId={}, role={}", propertyId,
                details != null ? details.getAgentId() : "null",
                details != null ? details.getRole() : "null");
        if (details == null) {
            log.warn("No authenticated user, denying modification");
            return false; // Explicitly deny if no user
        }
        final Property property = propertyRepository.findPropertiesWithAssigned(propertyId)
                .orElseThrow(() -> {
                    log.error("Property {} not found", propertyId);
                    return NotFoundException.forProperty(propertyId);
                });
        return isModificationAllowed(details, property);
    }

    public boolean isModificationAllowed(final CustomUserDetails details, final Property property) {
        if (details == null) {
            return false;
        }
        if (details.getRole() == Role.ADMIN) {
            return true;
        }
        return property.isAgentAssigned(details.getAgentId());
    }

    public boolean isAgentModificationAllowed(final CustomUserDetails details, final Agent agent) {
        if (details == null) {
            log.warn("No authenticated user, denying agent modification");
            return false;
        }
        // Admins can modify any agent
        if (details.getRole() == Role.ADMIN) {
            log.info("Admin user, allowing agent modification");
            return true;
        }
        // Agents can only modify themselves
        boolean isSelf = agent.getAgentId().equals(details.getAgentId());
        log.info("Agent modification check: isSelf={}, agentId={}, userAgentId={}", isSelf, agent.getAgentId(), details.getAgentId());
        return isSelf;
    }
}



