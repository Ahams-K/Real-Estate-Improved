package be.kdg.prog5.RealEstateSystem.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
public class RealEstateAgency {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID agencyId;

    private String agencyName;
    private String address;
    private String contactInfo;
    private String city;
    private String image;

    @OneToMany(mappedBy = "agency", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Agent> agents = new HashSet<>();


    public RealEstateAgency(){

    }

    public void addAgent(Agent agent) {
        if (!agents.contains(agent)) {
            agents.add(agent);
            agent.setAgency(this); // maintain bidirectional relationship
        }
    }
}
