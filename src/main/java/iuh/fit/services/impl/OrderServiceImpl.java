package iuh.fit.services.impl;

import iuh.fit.dtos.order.OrderDTO;
import iuh.fit.dtos.order.OrderSummaryDTO;
import iuh.fit.entities.Order;
import iuh.fit.entities.User;
import iuh.fit.entities.enums.OrderStatus;
import iuh.fit.entities.enums.PaymentType;
import iuh.fit.repositories.OrderItemRepository;
import iuh.fit.repositories.OrderRepository;
import iuh.fit.repositories.UserRepository;
import iuh.fit.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository  orderItemRepository;
    private final ModelMapper modelMapper;
//    private final ModelMapper modelMapper;

    //Tạo dơn hàng mới
    @Override
    public OrderDTO createOrder(OrderDTO  orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDTO.class);
    }

    //Lấy đơn hàng theo ID
    @Override
    public OrderDTO findOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));
        return modelMapper.map(order, OrderDTO.class);
    }

    //Lấy danh sách đơn hàng của 1 user
    @Override
    public List<OrderDTO> findOrdersByUserId(Integer userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(o -> modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }

    //Lấy danh sách đơn hàng có phân trang
    @Override
    public Page<OrderSummaryDTO> findAllOrders(Pageable pageable){
        return  orderRepository.findAllWithPaging(pageable)
                .map(o -> modelMapper.map(o, OrderSummaryDTO.class));
    }

    //Lọc theo trạng thái
    @Override
    public List<OrderDTO> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status)
                .stream()
                .map(o -> modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }

    //Lọc đơn hàng trong khoảng thời gian
    public List<OrderDTO> findOrdersBetweenDates(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findOrdersBetweenDates(start, end)
                .stream()
                .map(o -> modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }

    //tìm kiếm đơn hàng
    @Override
    public List<OrderDTO> searchOrders(String keyword){
        return orderRepository.search(keyword)
                .stream()
                .map(o -> modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }

    //Cập nhật trạng thái đơn hàng
    @Override
    public OrderDTO updateOrderStatus(Integer orderId, OrderStatus newStatus){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));
        order.setStatus(newStatus);
        Order updated = orderRepository.save(order);
        return modelMapper.map(updated, OrderDTO.class);
    }

    @Override
    public void deleteOrder(Integer id){
        orderRepository.deleteById(id);
    }

}
