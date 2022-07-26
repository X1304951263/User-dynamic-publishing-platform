package com.Config;

import com.Interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 13049
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 注入 token 拦截器
    @Autowired
    private TokenInterceptor interceptor;

    /**
     * 重写添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加自定义拦截器，并拦截对应 url"
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login/**");
    }
}