package com.gzhu.mapper;

import com.gzhu.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderMapper {
    // 根据id查询订单
    Order findById(Long id);
    // 查询所有的订单
    List<Order> list();
    // 增加一个订单
    void insertOrder(Order order);
    // 修改一个订单
    void updateOrder(Order order);
    // 删除一个订单
    void deleteOrder(Long id);
}
