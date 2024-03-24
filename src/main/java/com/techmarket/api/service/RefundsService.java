package com.techmarket.api.service;

import com.techmarket.api.form.refunds.CreateRefundsForm;
import com.techmarket.api.model.Order;
import com.techmarket.api.repository.OrderRepository;
import com.techmarket.api.repository.RefundsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefundsService {
    
    @Autowired
    private RefundsRepository refundsRepository;
    @Autowired
    private OrderRepository orderRepository;
    
    public void createRefunds(CreateRefundsForm createRefundsForm , long orderId)
    {
        Order order = orderRepository.findById(orderId).orElse(null);

    }
}
