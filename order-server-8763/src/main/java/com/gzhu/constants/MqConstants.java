package com.gzhu.constants;

public class MqConstants {
    // 采用话题交换机
    public static final String ORDER_EXCHANGE = "order.topic";
    // 新增或修改的消息队列
    public static final String ORDER_INSERT_QUEUE = "order.insert.queue";
    // 删除的消息队列
    public static final String ORDER_DELETE_QUEUE = "order.delete.queue";
    // 新增或修改的RoutingKey
    public final static String ORDER_INSERT_KEY = "order.insert";
    // 删除的RoutingKey
    public final static String ORDER_DELETE_KEY = "order.delete";

}
