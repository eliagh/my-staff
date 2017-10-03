package com.mycompany.mystaff.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mycompany.mystaff.domain.Location;
import com.mycompany.mystaff.service.dto.LocationDTO;

/**
 * Mapper for the entity Location and its DTO LocationDTO.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class, })
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {

  @Mapping(source = "company.id", target = "companyId")
  @Mapping(source = "company.name", target = "companyName")
  LocationDTO toDto(Location location);

  @Mapping(source = "companyId", target = "company")
  Location toEntity(LocationDTO locationDTO);

  default Location fromId(Long id) {
    if (id == null) {
      return null;
    }
    Location location = new Location();
    location.setId(id);
    return location;
  }

}
