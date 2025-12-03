package iuh.fit.services.impl;

import iuh.fit.dtos.StatisticsDTO;
import iuh.fit.repositories.OrderItemRepository;
import iuh.fit.repositories.OrderRepository;
import iuh.fit.repositories.UserRepository;
import iuh.fit.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    @Override
    public List<StatisticsDTO> getStatsByDay(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<Object[]> results = orderRepository.getDailyStats(start, end);

        return results.stream()
                .map(row -> {
                    LocalDate date = (LocalDate) row[0];
                    Double revenue = ((BigDecimal) row[1]).doubleValue();
                    Long orders = (Long) row[2];
                    return StatisticsDTO.builder()
                            .date(date)
                            .totalRevenue(revenue)
                            .totalOrders(orders)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<StatisticsDTO> getStatsByMonth(Integer year) {
        List<Object[]> results = orderRepository.getMonthlyStats(year);

        return results.stream()
                .map(row -> {
                    Integer month = (Integer) row[0];
                    Integer y = (Integer) row[1];
                    Double revenue = ((BigDecimal) row[2]).doubleValue();
                    Long orders = (Long) row[3];
                    return StatisticsDTO.builder()
                            .name(Month.of(month).name() + "/" + y)
                            .totalRevenue(revenue)
                            .totalOrders(orders)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<StatisticsDTO> getTopCustomers(Pageable pageable) {
        List<Object[]> results = orderRepository.getTopCustomerStats(pageable);

        List<StatisticsDTO> customerStats = results.stream()
                .map(row -> {
                    // Long id = (Long) row[0]; // ID User
                    String fullName = (String) row[1];
                    Long count = (Long) row[2];
                    Double totalValue = ((BigDecimal) row[3]).doubleValue();

                    return StatisticsDTO.builder()
                            .name(fullName)
                            .count(count)
                            .totalValue(totalValue)
                            .build();
                })
                .collect(Collectors.toList());
        return new PageImpl<>(customerStats, pageable, pageable.getPageSize());
    }

    @Override
    public Page<StatisticsDTO> getTopSellingProducts(Pageable pageable) {
        List<Object[]> results = orderItemRepository.getTopSellingProductStats(pageable);

        List<StatisticsDTO> productStats = results.stream()
                .map(row -> {
                    String title = (String) row[0];
                    Long count = (Long) row[1];
                    Double totalValue = ((BigDecimal) row[2]).doubleValue();

                    return StatisticsDTO.builder()
                            .name(title)
                            .count(count)
                            .totalValue(totalValue)
                            .build();
                })
                .collect(Collectors.toList());

        return new PageImpl<>(productStats, pageable, pageable.getPageSize());
    }
}