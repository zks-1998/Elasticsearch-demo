package com.gzhu.test;

import com.alibaba.fastjson.JSON;
import com.gzhu.pojo.Order;
import com.gzhu.service.OrderService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class DocTest {
    @Autowired
    private OrderService orderService;

    private final RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
            HttpHost.create("http://192.168.10.129:9200")));

    @Test
    public void testAddDoc() throws IOException {
        // 1.从数据库查询订单信息
        Order order = orderService.queryOrderById(101L);
        // 2.准备request对象  POST /order/_doc/101
        IndexRequest request = new IndexRequest("order").id(order.getId().toString());
        // 3.准备JSON文档，要求JSON格式（推荐使用fastjson）
        request.source(JSON.toJSONString(order), XContentType.JSON);
        // 4.发送添加请求
        client.index(request, RequestOptions.DEFAULT);
    }

    @Test
    void testGetDocumentById() throws IOException {
        // 1.准备Request      // GET /order/_doc/{id}
        GetRequest request = new GetRequest("order", "101");
        // 2.发送请求
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        // 3.解析响应结果
        String json = response.getSourceAsString();
        Order order = JSON.parseObject(json, Order.class);
        System.out.println("order= " + order);
    }

    @Test
    void testUpdateById() throws IOException {
        // 1.准备Request
        UpdateRequest request = new UpdateRequest("order", "101");
        // 2.准备参数
        request.doc(
                "num", "2"
        );
        // 3.发送请求
        client.update(request, RequestOptions.DEFAULT);
    }

    @Test
    void testDeleteDocumentById() throws IOException {
        // 1.准备Request      // DELETE /order/_doc/{id}
        DeleteRequest request = new DeleteRequest("order", "101");
        // 2.发送请求
        client.delete(request, RequestOptions.DEFAULT);
    }

    @Test
    void testBulkRequest() throws IOException {
        // 查询所有的订单数据
        List<Order> list = orderService.findAll();
        // 1.准备Request
        BulkRequest request = new BulkRequest();
        // 2.准备参数
        for (Order order : list) {
            // 2.1 转json
            String json = JSON.toJSONString(order);
            // 2.2 添加请求
            request.add(new IndexRequest("order").id(order.getId().toString()).source(json, XContentType.JSON));
        }
        // 3.发送请求
        client.bulk(request, RequestOptions.DEFAULT);
    }

}
