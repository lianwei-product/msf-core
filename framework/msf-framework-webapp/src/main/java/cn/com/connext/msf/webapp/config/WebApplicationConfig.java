package cn.com.connext.msf.webapp.config;

import cn.com.connext.msf.framework.jwt.GlobalJwtConstant;
import cn.com.connext.msf.framework.query.QueryInfo;
import cn.com.connext.msf.framework.query.QueryPage;
import cn.com.connext.msf.webapp.query.QueryInfoArgumentResolver;
import cn.com.connext.msf.webapp.webapi.ServerErrorApi;
import cn.com.connext.msf.webapp.webapi.ServerHealthApi;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.servlet.Servlet;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Configuration
@ConditionalOnProperty(name = "msf.web.enable", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication
@ComponentScan("cn.com.connext.msf.webapp.config")
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@Import({ServerErrorApi.class, ServerHealthApi.class})
public class WebApplicationConfig implements WebMvcConfigurer {

    public WebApplicationConfig() {

    }

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new QueryInfoArgumentResolver());
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return (factory -> factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404")));
    }

    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.setTimeZone(TimeZone.getDefault());
        objectMapper.addMixIn(Page.class, PageMixIn.class);
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule()); // new module, NOT JSR310Module

        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);

        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
        return mappingJackson2HttpMessageConverter;
    }

    @Bean
    public Docket createSwaggerDocket(SwaggerConfigProperties swaggerConfigProperties) {

        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(swaggerConfigProperties.getAppName())
                .description(swaggerConfigProperties.getDescription())
                .version("")
                .build();

        List<ApiKey> list = new ArrayList<>();
        list.add(new ApiKey("Authorization", "Authorization", "header"));
        list.add(new ApiKey("TenantId", GlobalJwtConstant.JWT_HEADER_TENANT_ID, "header"));
        list.add(new ApiKey("ClientType", GlobalJwtConstant.JWT_HEADER_CLIENT_TYPE, "header"));
        list.add(new ApiKey("ClientId", GlobalJwtConstant.JWT_HEADER_CLIENT_Id, "header"));

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerConfigProperties.getApiPackage()))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .ignoredParameterTypes(QueryInfo.class)
                .securitySchemes(list);
    }

    @JsonDeserialize(as = QueryPage.class)
    private interface PageMixIn {
    }
}
