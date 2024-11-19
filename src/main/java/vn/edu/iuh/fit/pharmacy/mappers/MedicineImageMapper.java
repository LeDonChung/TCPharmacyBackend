package vn.edu.iuh.fit.pharmacy.mappers;

import org.mapstruct.*;
import vn.edu.iuh.fit.pharmacy.POJOs.MedicineImage;
import vn.edu.iuh.fit.pharmacy.utils.response.MedicineImageResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicineImageMapper {
    @Mapping(target = "url", source = "url")
    MedicineImageResponse toDto(MedicineImage medicineImage);
}