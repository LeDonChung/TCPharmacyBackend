package vn.edu.iuh.fit.pharmacy.service;

import vn.edu.iuh.fit.pharmacy.exceptions.MedicineException;
import vn.edu.iuh.fit.pharmacy.exceptions.OrderException;
import vn.edu.iuh.fit.pharmacy.exceptions.UserException;
import vn.edu.iuh.fit.pharmacy.utils.request.CreateOrderRequest;
import vn.edu.iuh.fit.pharmacy.utils.response.OrderResponse;

public interface OrderService {
    public OrderResponse createOrder(CreateOrderRequest request) throws UserException, OrderException, MedicineException;
}
