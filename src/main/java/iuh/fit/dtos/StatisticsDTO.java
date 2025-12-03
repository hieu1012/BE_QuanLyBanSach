package iuh.fit.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsDTO {
    private LocalDate date;
    private Long totalOrders;
    private Double totalRevenue;
    private Long totalCustomers;


    private String name;
    private Long count;
    private Double totalValue;


    private List<StatisticsDTO> details;
}