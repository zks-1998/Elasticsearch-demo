package com.gzhu.pojo;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class Order {
    private Long id;
    private Long userId;
    private String name;
    private Long price;
    private Integer num;
    private String address;
    private User user;
    private List<Object> suggestion;

    public Order(Order order) {
        this.id = order.getId();
        this.userId = order.getUserId();
        this.name = order.getName();
        this.price = order.getPrice();
        this.num = order.getNum();
        this.address = order.getAddress();
    }
}
