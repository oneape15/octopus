package com.oneape.octopus.admin.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.oneape.octopus.admin.config.props.CorsProperties;
import com.oneape.octopus.admin.interceptor.TokenVerifyInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    private static final String LANG = "lang";

    @Resource
    private CorsProperties corsProperties;

    private String[] list2Array(List<String> tmps, String defaultVal) {
        String[] arr;
        if (tmps == null || tmps.size() <= 0) {
            arr = new String[1];
            arr[0] = defaultVal;
        } else {
            arr = tmps.toArray(new String[0]);
        }
        return arr;
    }


    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        //允许的域
        String[] allowedOrigins = list2Array(corsProperties.getAllowOrigin(), "*");

        //允许的方法
        String[] allowMethods = list2Array(corsProperties.getAllowMethods(), "*");

        //允许的头
        String[] allowHeaders = list2Array(corsProperties.getAllowHeaders(), "*");

        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowMethods)
                .allowedHeaders(allowHeaders)
                .exposedHeaders("Content-Disposition")
                .allowCredentials(corsProperties.isAllowCredentials())
                .maxAge(3600);
    }

    /**
     * 修改自定义消息转换器
     *
     * @param converters List
     */
    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //调用父类的配置
        super.configureMessageConverters(converters);

        // 创建fastJson消息转换器
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        //升级最新版本需加
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.MULTIPART_FORM_DATA);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        fastConverter.setSupportedMediaTypes(supportedMediaTypes);

        // 初始化一个转换器配置
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

        //修改配置返回内容的过滤
        //WriteNullListAsEmpty  ：List字段如果为null,输出为[],而非null
        //WriteNullStringAsEmpty ： 字符类型字段如果为null,输出为"",而非null
        //DisableCircularReferenceDetect ：消除对同一对象循环引用的问题，默认为false（如果不配置有可能会进入死循环）
        //WriteNullBooleanAsFalse：Boolean字段如果为null,输出为false,而非null
        //WriteMapNullValue：是否输出值为null的字段,默认为false
        fastJsonConfig.setSerializerFeatures(SerializerFeature.BrowserCompatible,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.DisableCircularReferenceDetect);


        //解决Long转json精度丢失的问题
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        fastJsonConfig.setSerializeConfig(serializeConfig);
        // 将配置设置给转换器并添加到HttpMessageConverter转换器列表中
        fastConverter.setFastJsonConfig(fastJsonConfig);

        //将fastjson添加到视图消息转换器列表内
        converters.add(fastConverter);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenVerifyInterceptor());

        // 国际化
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName(LANG);
        registry.addInterceptor(localeChangeInterceptor);
        super.addInterceptors(registry);
    }

    /**
     * 访问静态资源
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
         * SpringBoot自动配置本身并不会把/swagger-ui.html
         * 这个路径映射到对应的目录META-INF/resources/下面
         * 采用WebMvcConfigurerAdapter将swagger的静态文件进行发布;
         */
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        //将所有/static/** 访问都映射到classpath:/static/ 目录下
//        registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");
        super.addResourceHandlers(registry);
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setMaxUploadSize(102400000);
        resolver.setMaxInMemorySize(4096);
        return resolver;
    }

    @Bean
    public LocaleResolver localeResolver() {
        final String DELIMITER = "_";
        final String LANG_SESSION = "lang_session";
        return new LocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest req) {
                String lang = req.getHeader(LANG);

                // default language is CHINA
                Locale locale = Locale.CHINA;
                if (StringUtils.isNotBlank(lang) && StringUtils.contains(lang, DELIMITER)) {
                    String[] languages = StringUtils.split(lang, DELIMITER);
                    locale = new Locale(languages[0], languages[1]);
                    HttpSession session = req.getSession();
                    session.setAttribute(LANG_SESSION, locale);
                } else {
                    HttpSession session = req.getSession();
                    Locale localeInSession = (Locale) session.getAttribute(LANG_SESSION);
                    if (localeInSession != null) {
                        locale = localeInSession;
                    }
                }
                return locale;
            }

            @Override
            public void setLocale(HttpServletRequest req, @Nullable HttpServletResponse resp, @Nullable Locale locale) {

            }
        };
    }
}
