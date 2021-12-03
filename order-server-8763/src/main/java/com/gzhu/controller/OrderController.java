package com.gzhu.controller;

import com.gzhu.constants.MqConstants;
import com.gzhu.pojo.Order;
import com.gzhu.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderService orderService;

    @GetMapping("/order/{id}")
    public Order queryById(@PathVariable("id") Long id){
       return orderService.queryOrderById(id);
    }

    @PostMapping("/order/insert")
    public void insertOrder(){
        Long id = 666L;
        Long userId = 2L;
        String name = "刘刘求";
        Long price = 3999L;
        Integer num = 1;
        String address = "重庆";

        Order order = new Order();
        order.setId(id);
        order.setUserId(userId);
        order.setName(name);
        order.setPrice(price);
        order.setNum(num);
        order.setAddress(address);

        orderService.insertOrder(order);
        rabbitTemplate.convertAndSend(MqConstants.ORDER_EXCHANGE,MqConstants.ORDER_INSERT_KEY,order.getId());
    }

    @PutMapping("/order/update/{id}")
    public void updateOrder(@PathVariable("id") Long id){
        Order order = queryById(id);
        order.setPrice(8879L);
        orderService.updateOrder(order);
        rabbitTemplate.convertAndSend(MqConstants.ORDER_EXCHANGE,MqConstants.ORDER_INSERT_KEY,order.getId());
    }
    @DeleteMapping("/order/delete/{id}")
    public void deleteOrder(@PathVariable("id") Long id){
        orderService.deleteOrder(id);
        rabbitTemplate.convertAndSend(MqConstants.ORDER_EXCHANGE,MqConstants.ORDER_DELETE_KEY,id);
    }
}
