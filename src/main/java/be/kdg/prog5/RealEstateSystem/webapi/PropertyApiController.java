package be.kdg.prog5.RealEstateSystem.webapi;

import be.kdg.prog5.RealEstateSystem.domain.Property;
import be.kdg.prog5.RealEstateSystem.domain.Role;
import be.kdg.prog5.RealEstateSystem.security.CustomUserDetails;
import be.kdg.prog5.RealEstateSystem.service.PropertyService;
import be.kdg.prog5.RealEstateSystem.webapi.dto.AgentDto;
import be.kdg.prog5.RealEstateSystem.webapi.dto.PropertyDto;
import be.kdg.prog5.RealEstateSystem.webapi.dto.request.AddPropertyDto;
import be.kdg.prog5.RealEstateSystem.webapi.dto.request.PatchPropertyDto;
import be.kdg.prog5.RealEstateSystem.webapi.dto.response.PropertyMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/api/properties")
public class PropertyApiController {
    private final PropertyService propertyService;
    private final PropertyMapper propertyMapper;

    public PropertyApiController(PropertyService propertyService, PropertyMapper propertyMapper) {
        this.propertyService = propertyService;
        this.propertyMapper = propertyMapper;
    }


    @GetMapping("/{id}/agents")
    public ResponseEntity<List<AgentDto>> getAgents(@PathVariable("id") final UUID id){
        final List<AgentDto> result = propertyService.getAgents(id)
                .stream()
                .map(AgentDto::fromAgent)
                .toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<PropertyDto> addProperty(@RequestBody @Valid final AddPropertyDto addPropertyDto,
                                                   @AuthenticationPrincipal final CustomUserDetails userDetails){
        final Property property = propertyService.add(addPropertyDto.propertyName(), addPropertyDto.address(), addPropertyDto.price(), addPropertyDto.type(), addPropertyDto.size(), addPropertyDto.status(), addPropertyDto.numberOfRooms(), addPropertyDto.dateListed(), userDetails.getAgentId());
        return  ResponseEntity.status(HttpStatus.CREATED).body(propertyMapper.toPropertyDto(property));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.isModificationAllowed(#principal, #id)")
    public ResponseEntity<Void> remove(@PathVariable("id") final UUID id, @AuthenticationPrincipal CustomUserDetails principal) {
        try {
            propertyService.remove(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting property {}: {}", id, e.getMessage(), e);
            throw e; // Re-throw to ensure the exception is propagated
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@authorizationService.isModificationAllowed(#principal, #id)")
    public ResponseEntity<PropertyDto> patch(@PathVariable("id") final UUID id,
                                             @RequestBody @Valid final PatchPropertyDto patchProperty,
                                             @AuthenticationPrincipal CustomUserDetails principal) {
        log.info("Patch request received for property {} with principal: {}", id, principal);
        if (principal == null) {
            log.warn("Principal is null for patch request");
        } else {
            log.info("User details: agentId={}, role={}", principal.getAgentId(), principal.getRole());
        }
        try {
            final Property property = propertyService.patch(id, patchProperty.propertyName(), patchProperty.address(), patchProperty.price(), patchProperty.type(), patchProperty.size(), patchProperty.status(), patchProperty.numberOfRooms(), patchProperty.datelisted());
            log.info("Property {} patched successfully", id);
            return ResponseEntity.status(HttpStatus.OK).body(propertyMapper.toPropertyDto(property));
        } catch (Exception e) {
            log.error("Error patching property {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

}



