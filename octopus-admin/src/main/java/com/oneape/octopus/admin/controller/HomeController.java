package com.oneape.octopus.admin.controller;

import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.dto.ApiResult;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/")
public class HomeController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    private final ErrorAttributes errorAttributes;

    public HomeController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @GetMapping(value = {"", "index", "index.htm", "index.html"})
    public ApiResult<String> index() {
        return new ApiResult<>("hello world");
    }

    /**
     * Returns the path of the error page.
     *
     * @return the error path
     */
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }


    @RequestMapping(value = ERROR_PATH, produces = "text/html", method = {RequestMethod.GET, RequestMethod.POST})
    public String errorPageHandler(HttpServletRequest request, HttpServletResponse response) {
        int status = response.getStatus();
        return "status";
    }


    @RequestMapping(value = ERROR_PATH, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    @ExceptionHandler(value = {BizException.class})
    public ApiResult<String> errorHandler(HttpServletRequest request, final Exception ex, final WebRequest req) {
        Map<String, Object> attr = errorAttributes.getErrorAttributes(req, false);
        int status = getStatus(request);

        return ApiResult.ofError(status, String.valueOf(attr.getOrDefault("error_message", "error")));
    }

    private int getStatus(HttpServletRequest request) {
        Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (status != null) {
            return status;
        }
        return 500;
    }

}

