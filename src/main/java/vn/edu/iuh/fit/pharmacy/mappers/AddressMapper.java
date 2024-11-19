package vn.edu.iuh.fit.pharmacy.mappers;

import org.mapstruct.*;
import vn.edu.iuh.fit.pharmacy.POJOs.Address;
import vn.edu.iuh.fit.pharmacy.utils.request.AddressRequest;
import vn.edu.iuh.fit.pharmacy.utils.response.AddressResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    @Mapping(target = "_default", source = "default")
    AddressResponse toDto(Address address);

    Address toEntity(AddressRequest addressRequest);
}