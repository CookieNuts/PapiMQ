package com.cookienats.common.papimq;

import com.cookienats.common.papimq.common.MQArgumentsResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class PapimqApplication extends WebMvcConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(PapimqApplication.class, args);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new MQArgumentsResolver());
    }

    // 使用 protobuf 作为消息协议(序列化)
    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }
    // 配置restTeamplete 解析 protobuf（反序列化)
    @Bean
    RestTemplate restTemplate(ProtobufHttpMessageConverter hmc) {
        return new RestTemplate(Collections.singletonList(hmc));
    }

}
