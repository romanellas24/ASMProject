package api.service;


import api.dto.*;
import api.exception.NotFoundException;
import api.utils.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    OrderDTO getOrder(Integer id) throws NotFoundException, Exception;
    OrderDTO getOrder(Integer id, String companyName) throws Exception;
    Integer createOrder(DishBasicInfoDTO[] dishes, LocalDateTime deliveryTime) throws Exception;
    void updateOrderStatus(Integer id, OrderStatus status) throws Exception;
    Boolean existsOrder(Integer id);
    Boolean existsOrder(Integer id, String companyName);
    Boolean deleteOrder(Integer id);
    Boolean deleteOrder(OrderMappingDTO mapping);
    Boolean isIdCompanyValid(Integer id, String companyName) throws Exception;
    List<OrderDTO> getOrdersByDayPaged(LocalDate day, Integer page);
    void saveMapping(OrderMappingDTO orderMappingDTO) throws Exception;
    OrderMappingDTO getMapping(String companyName, Integer companyOrderId) throws Exception;

}
