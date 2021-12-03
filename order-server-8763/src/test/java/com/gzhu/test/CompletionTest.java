package com.gzhu.test;

import lombok.val;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class CompletionTest {
    private final RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
            HttpHost.create("http://192.168.10.129:9200")));
    @Test
    public void testSuggest() throws IOException {
        // 准备request
        SearchRequest request = new SearchRequest("order");
        // 准备DSL
        request.source().suggest(new SuggestBuilder().addSuggestion(
                "my_suggestions",
                SuggestBuilders.completionSuggestion("suggestion")
                        .prefix("s")
                        .skipDuplicates(true)
                        .size(10)
        ));
        // 发起请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        // 解析结果
        Suggest suggest = response.getSuggest();
        CompletionSuggestion my_suggestions = suggest.getSuggestion("my_suggestions");
        List<CompletionSuggestion.Entry.Option> options = my_suggestions.getOptions();
        for (CompletionSuggestion.Entry.Option option : options){
            String text = option.getText().toString();
            System.out.println(text);
        }

    }
}
