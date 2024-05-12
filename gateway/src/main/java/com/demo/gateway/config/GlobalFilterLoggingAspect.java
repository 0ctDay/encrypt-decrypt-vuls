package com.demo.gateway.config;

import com.demo.gateway.annotations.LoggableGlobalFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Aspect
@Component
public class GlobalFilterLoggingAspect {

    private Logger logger = LogManager.getLogger(GlobalFilterLoggingAspect.class);

    @Before("@within(loggableGlobalFilter) && execution(public * org.springframework.web.server.WebFilter.filter(..)) && args(exchange)")
    public void logGlobalFilterExecution(LoggableGlobalFilter loggableGlobalFilter, ServerWebExchange exchange) {
        Class<?> filterClass = loggableGlobalFilter.getClass();
        logger.info("Executing global filter: " + filterClass.getName() + " for request: " + exchange.getRequest().getURI());
    }
}
