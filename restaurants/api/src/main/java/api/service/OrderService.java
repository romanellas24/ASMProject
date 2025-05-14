package api.service;


import api.dto.OrderDTO;
import api.exception.NotFoundException;

import java.time.LocalDateTime;

public interface OrderService {
    OrderDTO getOrder(Integer id) throws NotFoundException, Exception;
    Integer createOrder(Integer[] dishIds, LocalDateTime deliveryTime) throws Exception;
}
