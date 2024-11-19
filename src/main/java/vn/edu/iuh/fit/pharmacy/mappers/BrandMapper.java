package vn.edu.iuh.fit.pharmacy.mappers;

import org.mapstruct.*;
import vn.edu.iuh.fit.pharmacy.POJOs.Brand;
import vn.edu.iuh.fit.pharmacy.utils.response.BrandResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BrandMapper {
    Brand toEntity(BrandResponse brandResponse);

    BrandResponse toDto(Brand brand);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Brand partialUpdate(BrandResponse brandResponse, @MappingTarget Brand brand);
}