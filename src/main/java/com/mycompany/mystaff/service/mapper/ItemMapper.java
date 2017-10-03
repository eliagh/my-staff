package com.mycompany.mystaff.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mycompany.mystaff.domain.Item;
import com.mycompany.mystaff.service.dto.ItemDTO;

/**
 * Mapper for the entity Item and its DTO ItemDTO.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class, })
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {

  @Mapping(source = "company.id", target = "companyId")
  @Mapping(source = "company.name", target = "companyName")
  ItemDTO toDto(Item item);

  @Mapping(source = "companyId", target = "company")
  Item toEntity(ItemDTO itemDTO);

  default Item fromId(Long id) {
    if (id == null) {
      return null;
    }
    Item item = new Item();
    item.setId(id);
    return item;
  }

}
