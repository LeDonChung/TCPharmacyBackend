package vn.edu.iuh.fit.pharmacy.mappers;

import org.mapstruct.*;
import vn.edu.iuh.fit.pharmacy.POJOs.Category;
import vn.edu.iuh.fit.pharmacy.utils.response.CategoryResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {CategoryMapper.class})
public interface CategoryMapper {
    @Mapping(target = "parent.id", source = "parent")
    Category toEntity(CategoryResponse categoryResponse);

    // Chỉ để lại ID của parent
    @Mapping(target = "parent", source = "parent.id")
    CategoryResponse toDto(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "parent.id", source = "parent")
    Category partialUpdate(CategoryResponse categoryResponse, @MappingTarget Category category);
}