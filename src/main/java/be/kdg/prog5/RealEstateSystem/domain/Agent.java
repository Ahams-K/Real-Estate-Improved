package be.kdg.prog5.RealEstateSystem.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.util.List;
import java.util.UUID;


@Entity
@Data
public class Agent{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID agentId;

    private String agentName;
    private String password;
    private String contactInfo;
    private String licenceNumber;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    private RealEstateAgency agency;

    @OneToMany(mappedBy = "agent", fetch = FetchType.LAZY,  cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<AgentProperty> agentProperties;

    public Agent(){

    }

    public boolean isPropertyManaged(final UUID propertyId) {
        return agentProperties
                .stream()
                .map(AgentProperty::getAgent)
                .anyMatch(agent -> agent.getAgentId().equals(propertyId));
    }

}
