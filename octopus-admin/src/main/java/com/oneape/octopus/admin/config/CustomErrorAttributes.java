package com.oneape.octopus.admin.config;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> map = super.getErrorAttributes(webRequest, includeStackTrace);

        Map<String, Object> newMap = new HashMap<>();
        newMap.put("error_code", map.getOrDefault("status", 0));
        newMap.put("error_message", map.getOrDefault("error", ""));
        return newMap;
    }
}
