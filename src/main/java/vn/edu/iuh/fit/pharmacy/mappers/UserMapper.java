package vn.edu.iuh.fit.pharmacy.mappers;

import org.mapstruct.*;
import vn.edu.iuh.fit.pharmacy.POJOs.User;
import vn.edu.iuh.fit.pharmacy.utils.request.UserRegisterRequest;
import vn.edu.iuh.fit.pharmacy.utils.response.UserResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {AddressMapper.class})
public interface UserMapper {
    User toEntity(UserRegisterRequest request);

    @Mapping(target = "currentPoint", source = "point.currentPoint")
    UserResponse toUserResponse(User user);
}