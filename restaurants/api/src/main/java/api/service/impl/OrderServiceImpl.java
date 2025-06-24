package api.service.impl;

import api.dao.DishOrderDAO;
import api.dao.OrderDAO;
import api.dao.OrderMappingDAO;
import api.dto.DishInOrderDTO;
import api.dto.OrderDTO;
import api.dto.OrderMappingDTO;
import api.dto.ResponseOrderDTO;
import api.entity.Order;
import api.entity.OrderMapping;
import api.exception.NotFoundException;
import api.service.OrderService;
import api.utils.StringToDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private DishOrderDAO dishOrderDAO;

    @Autowired
    private OrderMappingDAO orderMappingDAO;


    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrder(Integer id) throws NotFoundException, Exception {
        Optional<Order> order = orderDAO.findById(id);
        if(order.isEmpty())
            throw new NotFoundException("Id not found");
        return OrderDTO.fromOrder(order.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createOrder(DishInOrderDTO[] dishIds, LocalDateTime deliveryTime) throws Exception {
        //order ids checked before send to rabbit
        //create order
        Order order = new Order(deliveryTime);
        orderDAO.save(order);

        Integer orderId = order.getId();

        // add dish for order
        for (DishInOrderDTO dish : dishIds) {
            dishOrderDAO.insertDishOrder(orderId, dish.getId(), dish.getQuantity());
        }
        return orderId;
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existsOrder(Integer id) {
        return orderDAO.existsById(id);
    }

    @Override
    public Boolean deleteOrder(Integer id) {
        try{
            orderDAO.deleteById(id);
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean deleteOrder(OrderMappingDTO mapping) {
        try{
            orderMappingDAO.deleteById(mapping.getOrderId());
            orderDAO.deleteById(mapping.getOrderId());
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    @Override
    public List<OrderDTO> getOrdersByDayPaged(LocalDate day, Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Order> pagedOrders = orderDAO.findAllByDeliveryTimeAfterAndDeliveryTimeBeforeOrderByDeliveryTimeAsc(
                LocalDateTime.now(),
                StringToDate.getEndOfDay(day),
                pageable);

        List<Order> orders = pagedOrders.getContent();

        return orders.stream().map(OrderDTO::fromOrder).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveMapping(ResponseOrderDTO responseOrderDTO, OrderMappingDTO orderMappingDTO) throws Exception {
        OrderMapping orderMapping = new OrderMapping();
        Order order = orderDAO.findById(responseOrderDTO.getOrder().getId()).orElse(null);
        if (order == null) {
            throw new NotFoundException("order not found");
        }
        orderMapping.setOrder(order);
        orderMapping.setCompanyId(orderMappingDTO.getCompanyId());
        orderMapping.setCompanyName(orderMappingDTO.getCompanyName());

        orderMappingDAO.save(orderMapping);
    }

    @Override
    public OrderMappingDTO getMapping(String companyName, Integer companyOrderId) throws Exception {
        Optional<OrderMapping> orderMapping = orderMappingDAO.findFirstByCompanyIdAndCompanyName(companyOrderId, companyName);
        if (orderMapping.isEmpty()) {
            throw new NotFoundException("Company order id not found");
        }
        return OrderMappingDTO.fromEntity(orderMapping.get());
    }
}
