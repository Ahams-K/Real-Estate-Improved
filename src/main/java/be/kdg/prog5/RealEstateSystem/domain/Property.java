package be.kdg.prog5.RealEstateSystem.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Property {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID propertyId;

    private String propertyName;
    private String address;
    private double price;

    @Enumerated(EnumType.STRING)
    private PropertyType type;

    private double size;

    @Enumerated(EnumType.STRING)
    private PropertyStatus status;

    private int numberOfRooms;
    private LocalDate dateListed;
    private String image;


    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY)
    private List<AgentProperty> agentProperties;

    public boolean isAgentAssigned(final UUID agentId) {
        return agentProperties
                .stream()
                .map(AgentProperty::getAgent)
                .anyMatch(agent -> agent.getAgentId().equals(agentId));
    }

    public Property() {

    }

}
