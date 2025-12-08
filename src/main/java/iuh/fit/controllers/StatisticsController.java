package iuh.fit.controllers;

import iuh.fit.dtos.StatisticsDTO;
import iuh.fit.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
public class StatisticsController {

    private final StatisticsService statisticsService;

    // Thống kê theo ngày
    @GetMapping("/day")
    public ResponseEntity<Map<String, Object>> getStatsByDay(
            @RequestParam(required = true) LocalDate startDate,
            @RequestParam(required = true) LocalDate endDate) {

        List<StatisticsDTO> result = statisticsService.getStatsByDay(startDate, endDate);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Lấy thống kê theo ngày thành công");
        response.put("data", result);
        return ResponseEntity.ok(response);
    }

    // Thống kê theo tháng
    @GetMapping("/month")
    public ResponseEntity<Map<String, Object>> getStatsByMonth(
            @RequestParam(required = true) Integer year) {

        List<StatisticsDTO> result = statisticsService.getStatsByMonth(year);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Lấy thống kê theo tháng thành công");
        response.put("data", result);
        return ResponseEntity.ok(response);
    }

    // Thống kê khách hàng top
    @GetMapping("/customers")
    public ResponseEntity<Map<String, Object>> getTopCustomers(
            @ParameterObject Pageable pageable) {

        Page<StatisticsDTO> result = statisticsService.getTopCustomers(pageable);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Lấy danh sách khách hàng hàng đầu thành công");
        response.put("data", result);
        return ResponseEntity.ok(response);
    }

    // Thống kê sản phẩm bán chạy
    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getTopSellingProducts(
            @ParameterObject Pageable pageable) {

        Page<StatisticsDTO> result = statisticsService.getTopSellingProducts(pageable);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Lấy danh sách sản phẩm bán chạy thành công");
        response.put("data", result);
        return ResponseEntity.ok(response);
    }
}