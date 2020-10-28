package com.oneape.octopus.commons.cause;

public enum StateCode {
    OK(200, "The request was successful."),
    BadRequest(400, "语义有误，当前请求无法被服务器理解"),
    Unauthorized(401, "当前请求需要用户验证"),
    LoginError(401001, "登录失败"),
    RegError(401002, "注册用户失败"),
    PaymentRequired(403, "服务器已经理解请求，但是拒绝执行它"),
    NotFound(404, "请求失败，请求的资源未被服务器发现"),
    MethodNotAllowed(405, "请求方法不能被用于请求相应的资源"),
    NotAcceptable(406, "请求的资源的内容特性无法满足请求头中的条件"),
    TooManyConnections(421, "连接数超过服务器许可最大范围"),
    Locked(423, "当前资源被锁定"),
    InternalServerError(500, "服务器遇到一个无法完成的请求处理"),
    NotImplemented(501, "服务器不支持当前请求所需要的某个功能"),
    BadGateway(502, "作为网关或者代理工作的服务器尝试执行请求时，从上游服务器接收到无效的响应"),

    BizError(1000, "业务处理错误")
    ;


    private int code;
    private String message;

    StateCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
