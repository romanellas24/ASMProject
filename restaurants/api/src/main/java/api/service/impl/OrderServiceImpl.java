package api.service.impl;

import api.dao.DishOrderDAO;
import api.dao.OrderDAO;
import api.dto.OrderDTO;
import api.entity.Order;
import api.exception.NotFoundException;
import api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private DishOrderDAO dishOrderDAO;

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
    public Integer createOrder(Integer[] dishIds, LocalDateTime deliveryTime) throws Exception {
        //order ids checked before send to rabbit
        //create order
        Order order = new Order(deliveryTime);
        orderDAO.save(order);

        Integer orderId = order.getId();

        // add dish for order
        for (Integer dishId : dishIds) {
            dishOrderDAO.insertDishOrder(orderId, dishId);
        }
        return orderId;
    }


}
