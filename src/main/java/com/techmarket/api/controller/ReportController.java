package com.techmarket.api.controller;

import com.techmarket.api.dto.ApiMessageDto;
import com.techmarket.api.exportFile.OrderExcelExporter;
import com.techmarket.api.model.Order;
import com.techmarket.api.model.criteria.OrderCriteria;
import com.techmarket.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/reports")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {

    @Autowired
    private OrderExcelExporter orderExcelExporter;
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole('BR_L')")
    public ApiMessageDto<String> exportOrder(HttpServletResponse response , OrderCriteria orderCriteria) throws IOException {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        List<Order> data = orderRepository.findAll(orderCriteria.getCriteria());
        orderExcelExporter.exportToExcel(response,data);
        apiMessageDto.setMessage("export success");
        return apiMessageDto;
    }
}
