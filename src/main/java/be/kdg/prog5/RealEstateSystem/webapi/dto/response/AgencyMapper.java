package be.kdg.prog5.RealEstateSystem.webapi.dto.response;

import be.kdg.prog5.RealEstateSystem.domain.RealEstateAgency;
import be.kdg.prog5.RealEstateSystem.webapi.dto.AgencyDto;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface AgencyMapper {
    AgencyDto toAgencyDto(RealEstateAgency agency);
}