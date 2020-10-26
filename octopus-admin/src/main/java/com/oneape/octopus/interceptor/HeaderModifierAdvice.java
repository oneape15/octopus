package com.oneape.octopus.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class HeaderModifierAdvice implements ResponseBodyAdvice<Object> {
    /**
     * Whether this component supports the given controller method return type
     * and the selected {@code HttpMessageConverter} type.
     *
     * @param returnType    the return type
     * @param converterType the selected converter type
     * @return {@code true} if {@link #beforeBodyWrite} should be invoked;
     * {@code false} otherwise
     */
    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * Invoked after an {@code HttpMessageConverter} is selected and just before
     * its write method is invoked.
     *
     * @param body                  the body to be written
     * @param returnType            the return type of the controller method
     * @param selectedContentType   the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request               the current request
     * @param response              the current response
     * @return the body that was passed in or a modified (possibly new) instance
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        ServletServerHttpRequest ssReq = (ServletServerHttpRequest) request;
        ServletServerHttpResponse ssResp = (ServletServerHttpResponse) response;

        final String originHeader = "Access-Control-Allow-Origin";

        // 对于未添加跨域消息头的响应进行处理
        HttpServletRequest req = ssReq.getServletRequest();
        HttpServletResponse resp = ssResp.getServletResponse();
        if (!resp.containsHeader(originHeader)) {
            String origin = req.getHeader("Origin");
            if (origin == null) {
                String referer = req.getHeader("Referer");
                if (referer != null) {
                    origin = referer.substring(0, referer.indexOf("/", 7));
                }
            }
            resp.setHeader(originHeader, origin);
        }

        String credentialHeader = "Access-Control-Allow-Credentials";
        if (!resp.containsHeader(credentialHeader)) {
            resp.setHeader(credentialHeader, "true");
        }

        return body;
    }
}
