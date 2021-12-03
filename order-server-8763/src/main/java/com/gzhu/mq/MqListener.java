package com.gzhu.mq;

import com.alibaba.fastjson.JSON;
import com.gzhu.constants.MqConstants;
import com.gzhu.pojo.Order;
import com.gzhu.service.OrderService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MqListener {
    @Autowired
    private OrderService orderService;
    private final RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
            HttpHost.create("http://192.168.10.129:9200")));

    @RabbitListener(queues = MqConstants.ORDER_INSERT_QUEUE)
    public void listenHotelInsertOrUpdate(Long id) throws IOException {
        // 1.从数据库查询订单信息
        Order order = orderService.queryOrderById(id);
        // 2.准备request对象  POST /order/_doc/101
        IndexRequest request = new IndexRequest("order").id(order.getId().toString());
        // 3.准备JSON文档，要求JSON格式（推荐使用fastjson）
        request.source(JSON.toJSONString(order), XContentType.JSON);
        // 4.发送添加请求
        client.index(request, RequestOptions.DEFAULT);
    }

    @RabbitListener(queues = MqConstants.ORDER_DELETE_QUEUE)
    public void listenHotelDelete(Long id) throws IOException {
        // 1.准备Request      // DELETE /order/_doc/{id}
        DeleteRequest request = new DeleteRequest("order", String.valueOf(id));
        // 2.发送请求
        client.delete(request, RequestOptions.DEFAULT);
    }
}
