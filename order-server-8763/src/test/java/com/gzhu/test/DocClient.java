package com.gzhu.test;

import com.alibaba.fastjson.JSON;
import com.gzhu.pojo.Order;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;

@SpringBootTest
public class DocClient {
    private final RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
            HttpHost.create("http://192.168.10.129:9200")));

    @Test
    void testMatchAll() throws IOException {
        // 1.准备Request，指定要查询的索引库
        SearchRequest request = new SearchRequest("order");
        // 2.准备DSL
        request.source().query(QueryBuilders.matchAllQuery());
        // 3.发送请求，默认返回JSON数据
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.解析响应
        SearchHits searchHits = response.getHits();
        // 4.1.获取总条数
        Long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到"+ total + "条文档");
        // 4.2.文档数组
        SearchHit[] hits = searchHits.getHits();
        // 4.3.遍历
        for(SearchHit hit : hits){
            // 获取文档source
            String json = hit.getSourceAsString();
            // 反序列化
            Order order = JSON.parseObject(json, Order.class);
            System.out.println(order);
        }
    }

    @Test
    void testMatch() throws IOException {
        // 1.准备Request，指定要查询的索引库
        SearchRequest request = new SearchRequest("order");
        // 2.准备DSL
        request.source().query(QueryBuilders.matchQuery("name","Apple"));
        // sort
        request.source().sort("price", SortOrder.ASC);
        // 分页
        request.source().from(0).size(12);
        // 3.发送请求，默认返回JSON数据
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.解析响应
        SearchHits searchHits = response.getHits();
        // 4.1.获取总条数
        Long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到"+ total + "条文档");
        // 4.2.文档数组
        SearchHit[] hits = searchHits.getHits();
        // 4.3.遍历
        for(SearchHit hit : hits){
            // 获取文档source
            String json = hit.getSourceAsString();
            // 反序列化
            Order order = JSON.parseObject(json, Order.class);
            System.out.println(order);
        }
    }

    @Test
    void testMulti_Match() throws IOException {
        // 1.准备Request，指定要查询的索引库
        SearchRequest request = new SearchRequest("order");
        // 2.准备DSL
        request.source().query(QueryBuilders.multiMatchQuery("6999","name","price"));
        // 3.发送请求，默认返回JSON数据
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.解析响应
        SearchHits searchHits = response.getHits();
        // 4.1.获取总条数
        Long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到"+ total + "条文档");
        // 4.2.文档数组
        SearchHit[] hits = searchHits.getHits();
        // 4.3.遍历
        for(SearchHit hit : hits){
            // 获取文档source
            String json = hit.getSourceAsString();
            // 反序列化
            Order order = JSON.parseObject(json, Order.class);
            System.out.println(order);
        }
    }

    @Test
    void testBoolQuery() throws IOException {
        // 1.准备Request，指定要查询的索引库
        SearchRequest request = new SearchRequest("order");
        // 2.准备DSL
        // 2.1 准备BooleanQuery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 2.2 准备条件
        boolQueryBuilder.must(QueryBuilders.termQuery("userId","1"));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").lte(1000));
        request.source().query(boolQueryBuilder);
        // 3.发送请求，默认返回JSON数据
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.解析响应
        SearchHits searchHits = response.getHits();
        // 4.1.获取总条数
        Long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到"+ total + "条文档");
        // 4.2.文档数组
        SearchHit[] hits = searchHits.getHits();
        // 4.3.遍历
        for(SearchHit hit : hits){
            // 获取文档source
            String json = hit.getSourceAsString();
            // 反序列化
            Order order = JSON.parseObject(json, Order.class);
            System.out.println(order);
        }
    }

    @Test
    void testHighlight() throws IOException {
        // 1.准备Request，指定要查询的索引库
        SearchRequest request = new SearchRequest("order");
        // 2.准备DSL
        request.source().query(QueryBuilders.matchQuery("name","Apple"));
        // 2.1 query
        request.source().highlighter(new HighlightBuilder().field("name").requireFieldMatch(false));
        // 3.发送请求，默认返回JSON数据
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.解析响应
        SearchHits searchHits = response.getHits();
        // 4.1.获取总条数
        Long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到"+ total + "条文档");
        // 4.2.文档数组
        SearchHit[] hits = searchHits.getHits();
        // 4.3.遍历
        for(SearchHit hit : hits){
            // 获取文档source
            String json = hit.getSourceAsString();
            // 反序列化
            Order order = JSON.parseObject(json, Order.class);
            // 获取高亮结果
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if(!CollectionUtils.isEmpty(highlightFields)){
                // 根据字段名获取结果
                HighlightField highlightField = highlightFields.get("name");
                if(highlightField != null){
                    // 获取高亮值
                    String name = highlightField.getFragments()[0].string();
                    // 覆盖非高亮结果
                    order.setName(name);
                    System.out.println(order);
                }
            }
        }
    }
}
