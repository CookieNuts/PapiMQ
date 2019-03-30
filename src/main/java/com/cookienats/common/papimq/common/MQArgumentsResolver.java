package com.cookienats.common.papimq.common;

import com.cookienats.common.papimq.common.utils.ProtoCodecUtil;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MQArgumentsResolver implements HandlerMethodArgumentResolver {
    protected Logger gameLogger = LoggerFactory.getLogger(MQArgumentsResolver.class);

    public MQArgumentsResolver() {
        System.out.println("ArgumentResolver init.");
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        ServletWebRequest req = (ServletWebRequest)nativeWebRequest;
        if(methodParameter.getParameterType().getName().endsWith("Request") && req.getRequest() instanceof HttpServletRequest){
            HttpServletRequest request = (HttpServletRequest) req.getRequest();
            Field field = methodParameter.getParameterType().getDeclaredField("PARSER");
            GeneratedMessageV3 msg = null;
            try {
                field.setAccessible(true);
                msg = ProtoCodecUtil.decodeOnlyObject(readBytes(request.getInputStream()), (Parser<? extends GeneratedMessageV3>) field.get(null));
            }finally {
                field.setAccessible(false);
            }

            return msg;
        }else if(methodParameter.getParameterType().getSuperclass().equals(GeneratedMessageV3.Builder.class)){
            Class<?> paramClass = methodParameter.getParameterType();
            Class<?> outClass = paramClass.getDeclaringClass();
            Method method = outClass.getMethod("newBuilder");
            GeneratedMessageV3.Builder builder = null;
            try{
                method.setAccessible(true);
                builder = (GeneratedMessageV3.Builder) method.invoke(null);
            }finally{
                method.setAccessible(false);
            }
            return builder;
        }else if(methodParameter.getParameterType().equals(String.class)){
            return req.getParameter(methodParameter.getParameterName());
        }
        return null;
    }

    private static byte[] readBytes(InputStream in) throws IOException {
        BufferedInputStream bufin = new BufferedInputStream(in);
        int buffSize = 1024;
        ByteArrayOutputStream out = new ByteArrayOutputStream(buffSize);

        byte[] temp = new byte[buffSize];
        int size = 0;
        while ((size = bufin.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        bufin.close();

        byte[] content = out.toByteArray();
        return content;
    }
}
