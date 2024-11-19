package vn.edu.iuh.fit.pharmacy.mappers;

import org.mapstruct.*;
import vn.edu.iuh.fit.pharmacy.POJOs.Tag;
import vn.edu.iuh.fit.pharmacy.utils.response.TagResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {
    @Mapping(target = "medicines", ignore = true)
    TagResponse toDto(Tag tag);
}