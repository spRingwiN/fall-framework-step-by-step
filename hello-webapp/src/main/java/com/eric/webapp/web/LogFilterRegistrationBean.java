package com.eric.webapp.web;

import com.eric.fall.annotation.Component;
import com.eric.fall.annotation.Order;
import com.eric.fall.web.FilterRegistrationBean;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@Component
@Order(50)
public class LogFilterRegistrationBean extends FilterRegistrationBean {
    @Override
    public List<String> getUrlPatterns() {
        return List.of("/*");
    }

    @Override
    public Filter getFilter() {
        return new LogFilter();
    }
}

class LogFilter implements Filter {

    final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        logger.info("{} {}", req.getMethod(), req.getRequestURI());
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
