package api.service;


import api.dto.DishInOrderDTO;
import api.dto.OrderDTO;
import api.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    OrderDTO getOrder(Integer id) throws NotFoundException, Exception;
    Integer createOrder(DishInOrderDTO[] dishes, LocalDateTime deliveryTime) throws Exception;
    Boolean existsOrder(Integer id);
    Boolean deleteOrder(Integer id);
    List<OrderDTO> getOrdersByDayPaged(LocalDate day, Integer page);
}
