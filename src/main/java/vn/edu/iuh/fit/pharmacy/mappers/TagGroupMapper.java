package vn.edu.iuh.fit.pharmacy.mappers;

import org.mapstruct.*;
import vn.edu.iuh.fit.pharmacy.POJOs.TagGroup;
import vn.edu.iuh.fit.pharmacy.utils.response.TagGroupResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {TagMapper.class})
public interface TagGroupMapper {

    TagGroupResponse toDto(TagGroup tagGroup);

}