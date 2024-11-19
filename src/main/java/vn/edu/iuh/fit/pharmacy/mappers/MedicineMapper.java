package vn.edu.iuh.fit.pharmacy.mappers;

import org.mapstruct.*;
import vn.edu.iuh.fit.pharmacy.POJOs.Medicine;
import vn.edu.iuh.fit.pharmacy.utils.response.MedicineResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {CategoryMapper.class, TagMapper.class, MedicineImageMapper.class})
public interface MedicineMapper {
    MedicineResponse toDto(Medicine medicine);
}