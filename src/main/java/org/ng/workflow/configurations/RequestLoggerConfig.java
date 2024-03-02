package org.ng.workflow.configurations;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggerConfig {
    @Bean
    public FilterRegistrationBean<CommonsRequestLoggingFilter> requestLoggingFilter() {
        FilterRegistrationBean<CommonsRequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();

        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setIncludeHeaders(true);
        loggingFilter.setBeforeMessagePrefix("Before req : ");
        loggingFilter.setAfterMessagePrefix("After req : ");
        loggingFilter.setMaxPayloadLength(51200);

        registrationBean.setFilter(loggingFilter);
        registrationBean.addUrlPatterns("/items/*");
        return registrationBean;
    }
}
