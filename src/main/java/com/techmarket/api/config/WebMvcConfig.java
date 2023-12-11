package com.techmarket.api.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.techmarket.api.component.LogInterceptor;
import com.techmarket.api.constant.UserBaseConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    @Autowired
    LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] exclusive = {"/v1/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**"};
        registry.addInterceptor(logInterceptor).addPathPatterns("/**").excludePathPatterns(exclusive);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
        builder.dateFormat(new SimpleDateFormat(UserBaseConstant.DATE_TIME_FORMAT));
        builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(UserBaseConstant.DATE_FORMAT)));
        builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(UserBaseConstant.DATE_TIME_FORMAT)));
        builder.indentOutput(true);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
        converters.add(new MappingJackson2XmlHttpMessageConverter(builder.createXmlMapper(true).build()));
        converters.add(new ResourceHttpMessageConverter());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateFormatter dateFormatter = new DateFormatter(UserBaseConstant.DATE_TIME_FORMAT);
        dateFormatter.setLenient(true);
        registry.addFormatter(dateFormatter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleDateFormat format = new SimpleDateFormat(WebMvcConfig.DATE_TIME_FORMAT);
        objectMapper.setDateFormat(format);
        return objectMapper;
    }
    @Bean
    public FilterRegistrationBean<CorsFilter> filterRegistrationBean(){
        final UrlBasedCorsConfigurationSource source = corsConfigurationSource();
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
    @Bean
    public CorsFilter corsFilter(){
        final UrlBasedCorsConfigurationSource source = corsConfigurationSource();
        return new CorsFilter(source);
    }
    private UrlBasedCorsConfigurationSource corsConfigurationSource(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Accept", "Origin","Content-Disposition","Cache-Control"));
        configuration.setExposedHeaders(Arrays.asList("Accept", "Origin", "Content-Type", "Depth", "User-Agent", "Authorization", "Cache-Control","Content-Disposition"));
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
