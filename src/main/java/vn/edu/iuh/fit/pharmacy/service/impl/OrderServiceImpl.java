package vn.edu.iuh.fit.pharmacy.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.pharmacy.POJOs.*;
import vn.edu.iuh.fit.pharmacy.exceptions.MedicineException;
import vn.edu.iuh.fit.pharmacy.exceptions.OrderException;
import vn.edu.iuh.fit.pharmacy.exceptions.UserException;
import vn.edu.iuh.fit.pharmacy.mappers.AddressMapper;
import vn.edu.iuh.fit.pharmacy.mappers.OrderMapper;
import vn.edu.iuh.fit.pharmacy.repositories.MedicineRepository;
import vn.edu.iuh.fit.pharmacy.repositories.OrderRepository;
import vn.edu.iuh.fit.pharmacy.repositories.UserRepository;
import vn.edu.iuh.fit.pharmacy.service.OrderService;
import vn.edu.iuh.fit.pharmacy.utils.request.CreateOrderRequest;
import vn.edu.iuh.fit.pharmacy.utils.request.OrderDetailRequest;
import vn.edu.iuh.fit.pharmacy.utils.response.OrderResponse;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private AddressMapper addressMapper;
    @Transactional
    @Override
    public OrderResponse createOrder(CreateOrderRequest request) throws UserException, OrderException, MedicineException {
        // check
        User user = userRepository.findById(request.getUser()).orElse(null);
        if (user == null) {
            throw new UserException("Tài khoản không tồn tại.");
        }

        Collection<OrderDetailRequest> orderDetailRequests = request.getOrderDetails();

        // Kiểm tra tồn kho
        for (OrderDetailRequest orderDetailRequest : orderDetailRequests) {
            Medicine medicine = medicineRepository.findById(orderDetailRequest.getMedicineId()).orElse(null);
            if(medicine == null) {
                throw new MedicineException(String.format("Sản phẩm với mã %s tồn tại.", orderDetailRequest.getMedicineId()));
            }
            if(medicine.getQuantity() <= 0) {
                throw new MedicineException(String.format("Sản phẩm %s đã hết.", medicine.getName()));
            }
            if (orderDetailRequest.getQuantity() > medicine.getQuantity()) {
                throw new OrderException("Số lượng sản phẩm không đủ.");
            }
        }
        if(user.getPoint() == null) {
            user.setPoint(
                    Point.builder()
                            .currentPoint(0)
                            .rank(Rank.Silver)
                            .updateAt(Timestamp.from(Instant.now()))
                            .user(user)
                            .pointHistories(List.of())
                            .build()
            );

            user = userRepository.saveAndFlush(user);
        }
        // Kiểm tra điểm nếu sử dụng
        if (request.isUsePoint()) {
            if (user.getPoint().getCurrentPoint() < request.getPoint()) {
                throw new OrderException("Điểm không đủ.");
            }
        }


        Order order = orderMapper.toEntity(request);
        // List of OrderDetail
        for (OrderDetailRequest orderDetailRequest : orderDetailRequests) {
            Medicine medicine = medicineRepository.findById(orderDetailRequest.getMedicineId()).orElse(null);
            if (medicine != null) {
                OrderDetail orderDetail = OrderDetail.builder()
                        .medicine(medicine)
                        .quantity(orderDetailRequest.getQuantity())
                        .order(order)
                        .id(OrderDetailId.builder()
                                .medicineId(medicine.getId())
                                .orderId(order.getId())
                                .build())
                        .price(orderDetailRequest.getPrice())
                        .discount(orderDetailRequest.getDiscount())
                        .build();
                order.getOrderDetails().add(orderDetail);
            }
        }



        order.setUser(user);
        order.setOrderDate(Timestamp.from(Instant.now()));
        order.setStatus(OrderStatus.PENDING);
        order.getAddress().setId(null);

        order = orderRepository.saveAndFlush(order);


        // Cập nhật point
        int pointSet = 0;
        if (order.isUsePoint()) {

            pointSet = user.getPoint().getCurrentPoint() - order.getPoint();
        } else {
            pointSet = user.getPoint().getCurrentPoint() + order.getPoint();
        }

        user.getPoint().setCurrentPoint(pointSet);
        user.getPoint().setUpdateAt(Timestamp.from(Instant.now()));

        userRepository.saveAndFlush(user);

        PointHistory pointHistory = PointHistory.builder()
                .createdAt(order.getOrderDate())
                .changeType(order.isUsePoint() ? ChangeTypePoint.USE : ChangeTypePoint.EARN)
                .order(order)
                .point(user.getPoint())
                .pointsChange(order.getPoint())
                .pointsBalance(user.getPoint().getCurrentPoint())
                .build();

        user.getPoint().getPointHistories().add(pointHistory);

        userRepository.saveAndFlush(user);


        // Cập nhật số lượng tồn kho
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            Medicine medicine = medicineRepository.findById(orderDetail.getMedicine().getId()).orElse(null);
            if (medicine != null) {
                medicine.setQuantity(medicine.getQuantity() - orderDetail.getQuantity());
                medicineRepository.saveAndFlush(medicine);
            }
        }

        return orderMapper.toDto(order);
    }
}
