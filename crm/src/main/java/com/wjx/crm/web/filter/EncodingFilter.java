package com.wjx.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("进入到过滤编码的过滤器");

        //过滤post请求中文参数乱码
        req.setCharacterEncoding("UTF-8");
        //过滤响应流中文乱码
        resp.setContentType("text/html;charset=utf-8");

        //放行请求
        chain.doFilter(req,resp);
    }
}
