package vn.edu.iuh.fit.pharmacy.mappers;

import org.mapstruct.*;
import vn.edu.iuh.fit.pharmacy.POJOs.Order;
import vn.edu.iuh.fit.pharmacy.utils.request.CreateOrderRequest;
import vn.edu.iuh.fit.pharmacy.utils.response.OrderResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {OrderDetailMapper.class, AddressMapper.class, UserMapper.class})
public interface OrderMapper {

    @Mapping(target = "user.id", source = "user")
    @Mapping(target = "orderDetails", ignore = true)
    Order toEntity(CreateOrderRequest createOrderRequest);



    OrderResponse toDto(Order order);

}