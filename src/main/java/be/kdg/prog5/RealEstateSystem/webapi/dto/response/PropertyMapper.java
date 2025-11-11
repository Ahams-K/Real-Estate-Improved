package be.kdg.prog5.RealEstateSystem.webapi.dto.response;


import be.kdg.prog5.RealEstateSystem.domain.Property;
import be.kdg.prog5.RealEstateSystem.webapi.dto.PropertyDto;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface PropertyMapper {
    PropertyDto toPropertyDto(Property property);
}
