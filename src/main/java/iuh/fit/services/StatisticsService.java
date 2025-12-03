package iuh.fit.services;

import iuh.fit.dtos.StatisticsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface StatisticsService {

    //Thống kê doanh thu, đơn hàng theo ngày
    List<StatisticsDTO> getStatsByDay(LocalDate startDate, LocalDate endDate);

    //Thống kê doanh thu, đơn hàng theo tháng

    List<StatisticsDTO> getStatsByMonth(Integer year);

    //Thống kê khách hàng top (số lượng đơn hàng hoặc tổng giá trị)
    Page<StatisticsDTO> getTopCustomers(Pageable pageable);

    //Thống kê sản phẩm bán chạy (số lượng bán)
    Page<StatisticsDTO> getTopSellingProducts(Pageable pageable);
}
