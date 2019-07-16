package com.wht.gateway.errorhandler;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义异常处理类
 * DefaultErrorWebExceptionHandler 是springboot提供的默认异常处理类
 * 需要重写
 */
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler {

    /**
     * 默认构造器，后续需要注入到springIOC容器里
     * @param errorAttributes
     * @param resourceProperties
     * @param errorProperties
     * @param applicationContext
     */
    public JsonExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    /**
     *获取异常属性
     * @param request
     * @param includeStackTrace
     * @return
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        int code = 500;
        Throwable error = super.getError(request);
        if (error instanceof NotFoundException) {
            code = 404;
        }

        return response(code, this.buildMessage(request, error));
    }


    /**
     * 构建异常消息
     * @param request
     * @param ex
     * @return
     */
    private String buildMessage(ServerRequest request, Throwable ex) {
        StringBuilder message = new StringBuilder("Failed to handle request [");
        message.append(request.method())
                .append(" ")
                .append(request.uri())
                .append("]");
        if (ex != null) {
            message.append(": ")
                    .append(ex.getMessage());
        }

        return message.toString();
    }

    /**
     * 构建返回的JSON格式数据
     *
     * @param status       状态码
     * @param errorMessage 异常信息
     * @return 返回数据
     */
    private static Map<String, Object> response(int status, String errorMessage) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", status);
        map.put("message", errorMessage);
        map.put("data", null);
        return map;
    }

    /**
     * 指定响应处理方法为JSON的处理方法
     * @param errorAttributes
     * @return
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(),this::renderErrorResponse);
        // return super.getRoutingFunction(errorAttributes);
    }

    /**
     * 根据code获取对应的httpstatus
     * @param errorAttributes
     * @return
     */
    @Override
    protected HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {
        int statusCode = (int)errorAttributes.get("code");
        return HttpStatus.valueOf(statusCode);
        //return super.getHttpStatus(errorAttributes);
    }
}
