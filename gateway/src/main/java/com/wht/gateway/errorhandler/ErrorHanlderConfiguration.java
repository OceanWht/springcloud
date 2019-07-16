package com.wht.gateway.errorhandler;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.List;

/**
 * @EnableConfigurationProperties 与 @ConfigurationProperties 差不多，但是会自动注入
 */
@SpringBootConfiguration
@EnableConfigurationProperties({ServerProperties.class, ResourceProperties.class})
public class ErrorHanlderConfiguration {
    private final ServerProperties serverProperties;

    private final ResourceProperties resourceProperties;

    private final List<ViewResolver> resolverList;

    private final ServerCodecConfigurer serverCodecConfigurer;

    private final ApplicationContext applicationContext;

    /**
     * 构造器，必须添加，否则报红
     *
     * @param serverProperties
     * @param resourceProperties
     * @param resolverList
     * @param serverCodecConfigurer
     * @param applicationContext
     */
    public ErrorHanlderConfiguration(ServerProperties serverProperties, ResourceProperties resourceProperties, List<ViewResolver> resolverList, ServerCodecConfigurer serverCodecConfigurer, ApplicationContext applicationContext) {
        this.serverProperties = serverProperties;
        this.resourceProperties = resourceProperties;
        this.resolverList = resolverList;
        this.serverCodecConfigurer = serverCodecConfigurer;
        this.applicationContext = applicationContext;
    }

    /**
     * gateway启动时启动该方法，将JsonExceptionHanlder注入到SpringIOC中
     *
     * @param errorAttributes
     * @return
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes) {
        JsonExceptionHandler jsonExceptionHandler = new JsonExceptionHandler(
                errorAttributes,
                this.resourceProperties,
                this.serverProperties.getError(),
                this.applicationContext
        );

        jsonExceptionHandler.setViewResolvers(this.resolverList);
        jsonExceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
        jsonExceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());

        return jsonExceptionHandler;
    }
}
