package com.gzhu;

import org.apache.http.HttpStatus;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Order(-1)
@Component
public class AuthorizeFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取请求参数
        MultiValueMap<String, String> params = exchange.getRequest().getQueryParams();
        // 2.获取authorization参数
        String auth = params.getFirst("authorization");
        //3.校验
        if ("admin".equals(auth)) {
            //放行
            return chain.filter(exchange);
        }
        //4.拦截
        //4.1禁止访问
        exchange.getResponse().setRawStatusCode(HttpStatus.SC_UNAUTHORIZED);
        //4.2处理结束
        return exchange.getResponse().setComplete();

    }
}
