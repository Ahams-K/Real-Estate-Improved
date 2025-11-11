package be.kdg.prog5.RealEstateSystem.webapi;

import be.kdg.prog5.RealEstateSystem.domain.RealEstateAgency;
import be.kdg.prog5.RealEstateSystem.service.AgencyService;
import be.kdg.prog5.RealEstateSystem.webapi.dto.AgencyDto;
import be.kdg.prog5.RealEstateSystem.webapi.dto.request.AddAgencyDto;
import be.kdg.prog5.RealEstateSystem.webapi.dto.response.AgencyMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/agencies")
public class AgencyApiController {
    private final AgencyService agencyService;
    private final AgencyMapper agencyMapper;

    public AgencyApiController(AgencyService agencyService, AgencyMapper agencyMapper) {
        this.agencyService = agencyService;
        this.agencyMapper = agencyMapper;
    }

    @GetMapping("/search")
    public ResponseEntity<List<AgencyDto>> searchAgencies(@RequestParam("term") String term) {
        List<RealEstateAgency> agencies = agencyService.getAll().stream()
                .filter(agency -> agency.getAgencyName().toLowerCase().contains(term.toLowerCase()) ||
                        agency.getAddress().toLowerCase().contains(term.toLowerCase()) ||
                        agency.getCity().toLowerCase().contains(term.toLowerCase()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(agencies.stream().map(agencyMapper::toAgencyDto).toList());
    }

    @PostMapping("/add")
    @PreAuthorize("permitAll()") // Allow unauthenticated access for Client project testing
    public ResponseEntity<AgencyDto> addPublicAgency(@RequestBody @Valid AddAgencyDto addAgencyDto) {
        RealEstateAgency agency = new RealEstateAgency();
        agency.setAgencyName(addAgencyDto.agencyName());
        agency.setAddress(addAgencyDto.address());
        agency.setCity(addAgencyDto.city());
        agency.setContactInfo(addAgencyDto.contactInfo());
        agency.setImage(addAgencyDto.image());

        RealEstateAgency createdAgency = agencyService.add(agency);
        return ResponseEntity.status(HttpStatus.CREATED).body(agencyMapper.toAgencyDto(createdAgency));
    }
}