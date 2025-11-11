package be.kdg.prog5.RealEstateSystem.controller.viewmodel;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AddAgentForm {
    private String agentName;
    private String password;
    private String contactInfo;
    private String licenceNumber;
    private String email;
    private List<UUID> agentProperties = new ArrayList<>();
}
