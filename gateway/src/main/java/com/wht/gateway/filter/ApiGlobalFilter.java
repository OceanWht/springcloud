package com.wht.gateway.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class ApiGlobalFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if (StringUtils.isBlank(token)){
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().add("Content-type","text/json;charset=UTF-8");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            Map<String,Object> message = new HashMap<>();
            message.put("code",-1);
            message.put("data","鉴权失败");
            byte[] buffer = message.toString().getBytes(StandardCharsets.UTF_8);
            DataBuffer dataBuffer = response.bufferFactory().wrap(buffer);
            return response.writeWith(Mono.just(dataBuffer));
        }
        return chain.filter(exchange);
    }
}
