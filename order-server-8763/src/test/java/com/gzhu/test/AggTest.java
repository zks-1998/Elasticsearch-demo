package com.gzhu.test;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.ingest.IngestStats;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Stats;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class AggTest {
    private final RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
            HttpHost.create("http://192.168.10.129:9200")));
    @Test
    void testBucketAgg() throws IOException {
        // 1.请求
        SearchRequest request = new SearchRequest("order");
        // 2.DSL
        request.source().query(QueryBuilders.termQuery("num",1));
        request.source().size(0);
        request.source().aggregation(AggregationBuilders
               .terms("userCount")
               .field("userId")
               .size(6));
        // 3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.解析结果
        Aggregations aggregations = response.getAggregations();
        Terms userCount = aggregations.get("userCount");
        List<? extends Terms.Bucket> buckets = userCount.getBuckets();
        for(Terms.Bucket bucket : buckets){
            String userId = bucket.getKeyAsString();
            long doc_count = bucket.getDocCount();
            System.out.println("key:"+userId+ " "+ "doc_count:"+doc_count);
        }
    }

    @Test
    void testMetricAgg() throws IOException {
        // 1.请求
        SearchRequest request = new SearchRequest("order");
        // 2.DSL
        request.source().size(0);
        request.source().aggregation(AggregationBuilders
                .terms("userCount")
                .field("userId")
                .size(6).subAggregation(AggregationBuilders
                        .stats("price_stats")
                        .field("price")));
        // 3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.解析结果
        Aggregations aggregations = response.getAggregations();

        Terms userCount = aggregations.get("userCount");
        List<? extends Terms.Bucket> buckets = userCount.getBuckets();
        for(Terms.Bucket bucket : buckets){
            String userId = bucket.getKeyAsString();
            long doc_count = bucket.getDocCount();
            Stats price_stats = bucket.getAggregations().get("price_stats");
            double avg = price_stats.getAvg();
            System.out.println("key:"+userId+ " "+ "doc_count:"+doc_count+ " "+"avg"+ avg);
        }
    }

    @Test
    void testMulConditions() throws IOException {
        // request
        SearchRequest request = new SearchRequest("order");
        // 带过滤条件
        request.source().query(QueryBuilders.termQuery("num",4));
        // 不要求文档
        request.source().size(0);
        // 聚合
        buildAgg(request);
        // 发出请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        Map<String, List<String>> result = new HashMap<>();
        Aggregations aggregations = response.getAggregations();

        List<String> userList = getAggByName(aggregations, "userAgg");
        result.put("用户", userList);


        List<String> priceList = getAggByName(aggregations, "priceAgg");
        result.put("价格", priceList);

        System.out.println(priceList);
    }

    private void buildAgg(SearchRequest request) {
        request.source().aggregation(AggregationBuilders
                .terms("userAgg")
                .field("userId")
                .size(6));
        request.source().aggregation(AggregationBuilders
                .terms("priceAgg")
                .field("price")
                .size(13));
    }

    private List<String> getAggByName(Aggregations aggregations, String aggName) {
        // 根据聚合名称获取聚合结果
        Terms terms = aggregations.get(aggName);
        // 获取buckets
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        // 遍历
        List<String> list = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            // 获取key
            String key = bucket.getKeyAsString();
            list.add(key);
        }
        return list;
    }

}
