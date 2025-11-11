package be.kdg.prog5.RealEstateSystem.webapi;

import be.kdg.prog5.RealEstateSystem.domain.Agent;
import be.kdg.prog5.RealEstateSystem.security.CustomUserDetails;
import be.kdg.prog5.RealEstateSystem.service.AgentService;
import be.kdg.prog5.RealEstateSystem.service.AuthorizationService;
import be.kdg.prog5.RealEstateSystem.webapi.dto.AgentDto;
import be.kdg.prog5.RealEstateSystem.webapi.dto.PropertyDto;
import be.kdg.prog5.RealEstateSystem.webapi.dto.request.AddAgentDto;
import be.kdg.prog5.RealEstateSystem.webapi.dto.request.UpdateAgentDto;
import be.kdg.prog5.RealEstateSystem.webapi.dto.response.AgentMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/agents")
public class AgentApiController {
    private final AgentService agentService;
    private final AgentMapper agentMapper;
    private final AuthorizationService authorizationService;

    public AgentApiController(AgentService agentService, AgentMapper agentMapper, AuthorizationService authorizationService) {
        this.agentService = agentService;
        this.agentMapper = agentMapper;
        this.authorizationService = authorizationService;
    }


    @GetMapping("/{id}/properties")
    public ResponseEntity<List<PropertyDto>> getProperties(@PathVariable("id") final UUID id) {
        final List<PropertyDto> result = agentService.getProperties(id)
                .stream()
                .map(PropertyDto::fromProperty)
                .toList();
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(
            @PathVariable("id") final UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Agent agent = agentService.findAgentById(id); // Fetch Agent object
        if (!authorizationService.isAgentModificationAllowed(userDetails, agent)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        agentService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgentDto> updateAgent(
            @PathVariable("id") UUID id,
            @RequestBody @Valid UpdateAgentDto updateAgentDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Agent agent = agentService.findAgentById(id);
        if (!authorizationService.isAgentModificationAllowed(userDetails, agent)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Agent updatedAgent = agentService.updateAgent(
                id,
                updateAgentDto.contactInfo(),
                updateAgentDto.licenceNumber(),
                updateAgentDto.email()
        );
        return ResponseEntity.ok(agentMapper.toAgentDto(updatedAgent));
    }
}
