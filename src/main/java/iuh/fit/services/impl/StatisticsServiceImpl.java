package iuh.fit.services.impl;

import iuh.fit.dtos.StatisticsDTO;
import iuh.fit.repositories.OrderItemRepository;
import iuh.fit.repositories.OrderRepository;
import iuh.fit.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    
    private Double safeDouble(Object value) {
        return value != null ? ((Number) value).doubleValue() : 0.0;
    }

    private Long safeLong(Object value) {
        return value != null ? ((Number) value).longValue() : 0L;
    }

    @Override
    public List<StatisticsDTO> getStatsByDay(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<Object[]> results = orderRepository.getDailyStats(start, end);

        return results.stream()
                .map(row -> {
                    // row[0] là Date (SQL Date hoặc Timestamp)
                    // row[1] là SUM(totalPrice) -> Double/BigDecimal
                    // row[2] là COUNT(id) -> Long/BigInteger

                    LocalDate date;
                    if (row[0] instanceof java.sql.Date) {
                        date = ((java.sql.Date) row[0]).toLocalDate();
                    } else if (row[0] instanceof java.sql.Timestamp) {
                        date = ((java.sql.Timestamp) row[0]).toLocalDateTime().toLocalDate();
                    } else {
                        date = LocalDate.parse(row[0].toString());
                    }

                    return StatisticsDTO.builder()
                            .date(date)
                            .totalRevenue(safeDouble(row[1]))
                            .totalOrders(safeLong(row[2]))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<StatisticsDTO> getStatsByMonth(Integer year) {
        List<Object[]> results = orderRepository.getMonthlyStats(year);

        return results.stream()
                .map(row -> {
                    Integer month = safeLong(row[0]).intValue();
                    Integer y = safeLong(row[1]).intValue();

                    return StatisticsDTO.builder()
                            .name(Month.of(month).name() + "/" + y)
                            .totalRevenue(safeDouble(row[2]))
                            .totalOrders(safeLong(row[3]))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<StatisticsDTO> getTopCustomers(Pageable pageable) {
        List<Object[]> results = orderRepository.getTopCustomerStats(pageable);

        List<StatisticsDTO> customerStats = results.stream()
                .map(row -> {
                    // row[0]: user_id, row[1]: fullName
                    String fullName = (String) row[1];

                    return StatisticsDTO.builder()
                            .name(fullName)
                            .count(safeLong(row[2]))
                            .totalValue(safeDouble(row[3]))
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
                    // row[0]: title
                    String title = (String) row[0];

                    return StatisticsDTO.builder()
                            .name(title)
                            .count(safeLong(row[1]))
                            .totalValue(safeDouble(row[2]))
                            .build();
                })
                .collect(Collectors.toList());

        return new PageImpl<>(productStats, pageable, pageable.getPageSize());
    }
}