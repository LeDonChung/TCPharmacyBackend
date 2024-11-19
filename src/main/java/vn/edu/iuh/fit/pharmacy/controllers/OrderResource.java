package vn.edu.iuh.fit.pharmacy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.pharmacy.exceptions.MedicineException;
import vn.edu.iuh.fit.pharmacy.exceptions.OrderException;
import vn.edu.iuh.fit.pharmacy.exceptions.UserException;
import vn.edu.iuh.fit.pharmacy.service.OrderService;
import vn.edu.iuh.fit.pharmacy.utils.request.CreateOrderRequest;
import vn.edu.iuh.fit.pharmacy.utils.response.OrderResponse;

@RestController
@RequestMapping("/orders")
public class OrderResource {

    @Autowired
    public OrderService orderService;

    @PostMapping("")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) throws UserException, OrderException, MedicineException {
        return ResponseEntity.ok(orderService.createOrder(request));
    }
}
