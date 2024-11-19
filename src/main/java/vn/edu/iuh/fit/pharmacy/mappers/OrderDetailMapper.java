package vn.edu.iuh.fit.pharmacy.mappers;

import org.mapstruct.*;
import vn.edu.iuh.fit.pharmacy.POJOs.OrderDetail;
import vn.edu.iuh.fit.pharmacy.utils.request.OrderDetailRequest;
import vn.edu.iuh.fit.pharmacy.utils.response.OrderDetailResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderDetailMapper {

    @Mapping(target = "order.id", source = "orderId")
    @Mapping(target = "medicine.id", source = "medicineId")
    @Mapping(target = "id.orderId", source = "orderId")
    @Mapping(target = "id.medicineId", source = "medicineId")
    OrderDetail toEntity(OrderDetailRequest orderDetailRequest);


    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "medicineId", source = "medicine.id")
    OrderDetailResponse toDto(OrderDetail orderDetail);

}