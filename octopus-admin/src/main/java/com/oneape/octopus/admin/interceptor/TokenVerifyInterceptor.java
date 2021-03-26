package com.oneape.octopus.admin.interceptor;

import com.oneape.octopus.commons.cause.UnauthorizedException;
import com.oneape.octopus.admin.config.ApplicationContextProvider;
import com.oneape.octopus.admin.config.SessionThreadLocal;
import com.oneape.octopus.dto.system.UserDTO;
import com.oneape.octopus.admin.service.system.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TokenVerifyInterceptor extends HandlerInterceptorAdapter {

    private volatile AccountService accountService;

    private static final String KEY_TOKEN = "TOKEN_KEY_";

    private static List<String> filterUris       = new ArrayList<>();
    private static List<String> filterUriOfStart = new ArrayList<>();

    static {
        filterUris.add("/");
        filterUris.add("/error");
        filterUris.add("/checkHealth");
        filterUris.add("/index.html");
        filterUris.add("/index.htm");
        filterUris.add("/account/login");
        filterUris.add("/account/reg");

        // 过滤以xx开始的url
        filterUriOfStart.add("/webjars/"); // 静态资源过滤
        filterUriOfStart.add("/swagger");  // swagger相关请求过滤
        filterUriOfStart.add("/activate/");
        filterUriOfStart.add("/datav/");
    }

    private static boolean exInclude(String uri) {
        if (filterUriOfStart.size() > 0) {
            for (String tmp : filterUriOfStart) {
                if (StringUtils.startsWith(uri, tmp)) {
                    return true;
                }
            }
        }
        if (filterUris.size() > 0) {
            for (String tmp : filterUris) {
                if (StringUtils.endsWith(uri, tmp)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检测token合法性
     *
     * @param token String 唯一token
     * @return boolean true-成功; false-失败
     */
    private UserDTO getUserInfoByToken(String token) {
        if (accountService == null) {
            synchronized (this) {
                if (accountService == null) {
                    synchronized (this) {
                        accountService = ApplicationContextProvider.getBean(AccountService.class);
                    }
                }
            }
        }
        return accountService.getUserInfoByToken(token);
    }

    /**
     * 获取客户端传入的token
     *
     * @param req HttpServletRequest
     * @return String
     */
    private static String getToken(HttpServletRequest req) {
        String token = req.getParameter("token");
        if (StringUtils.isBlank(token)) {
            token = req.getHeader("token");
        } else {
            //url参数中带过来，token要url编码
            try {
                token = URLEncoder.encode(token, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return token;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否有不需要验证的url
        String uri = request.getRequestURI();
        if (exInclude(uri)) {
            return true;
        }
        //OPTIONS请求不作处理
        if (StringUtils.equalsIgnoreCase("OPTIONS", request.getMethod())) {
            return true;
        }

        String token = getToken(request);
        if (StringUtils.isBlank(token)) {
            throw new UnauthorizedException();
        }

        //检测token的合法性
        UserDTO user = getUserInfoByToken(token);

        if (user == null) {
            log.info("token验证失败! token:{}", token);
            throw new UnauthorizedException();
        }

        SessionThreadLocal.setSession(user);

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        SessionThreadLocal.removeSession(); // 删除
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

}
