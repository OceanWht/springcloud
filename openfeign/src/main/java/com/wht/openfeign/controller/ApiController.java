package com.wht.openfeign.controller;

import com.wht.openfeign.api.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @Autowired
    ApiService apiService;

    @RequestMapping("index")
    public String index(){
        return apiService.index();
    }
}
