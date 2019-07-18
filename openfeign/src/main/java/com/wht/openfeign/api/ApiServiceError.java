package com.wht.openfeign.api;

import org.springframework.stereotype.Component;

@Component
public class ApiServiceError implements ApiService{
    @Override
    public String index() {
        return "服务器发生错误";
    }
}
