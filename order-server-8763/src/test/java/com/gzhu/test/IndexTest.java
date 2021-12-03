package com.gzhu.test;


import com.gzhu.constants.IndexConstants;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class IndexTest {

    private final RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
            HttpHost.create("http://192.168.10.129:9200")));
    // 1.创建索引库
    @Test
    public void testCreateIndex() throws IOException {
        // 1.准备Request      PUT /order
        CreateIndexRequest request = new CreateIndexRequest("order");
        // 2.准备请求参数
        request.source(IndexConstants.MAPPING_STRING, XContentType.JSON);
        // 3.发送请求
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    // 2.删除索引库
    @Test
    public void testDeleteIndex() throws IOException {
        // 1.准备Request      PUT /order
        DeleteIndexRequest request = new DeleteIndexRequest("order");
        // 3.发送请求
        client.indices().delete(request, RequestOptions.DEFAULT);
    }

    @Test
    void testExistsIndex() throws IOException {
        // 1.准备Request
        GetIndexRequest request = new GetIndexRequest("order");
        // 3.发送请求
        boolean isExists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(isExists ? "存在" : "不存在");
    }

}
