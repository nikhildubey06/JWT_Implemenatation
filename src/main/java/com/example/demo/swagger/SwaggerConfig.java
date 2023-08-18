package com.example.demo.swagger;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.*;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration

@EnableSwagger2

public class SwaggerConfig extends WebMvcConfigurationSupport {

	@Override

	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry

				.addResourceHandler("swagger-ui.html")

				.addResourceLocations("classpath:/META-INF/resources/");

		registry

				.addResourceHandler("/webjars/**")

				.addResourceLocations("classpath:/META-INF/resources/webjars/");

	}

	public static final String AUTHORIZATION_HEADER = "Authorization";

	private ApiKey apiKey() {
		return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
	}

	@Bean

	public Docket apiDocket() {

		return new Docket(DocumentationType.SWAGGER_2)

				.apiInfo(getApiInfo())

				.securityContexts(Arrays.asList(securityContext())).securitySchemes(Arrays.asList(apiKey()))

				.select()

				.apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))

				.paths(PathSelectors.any())

				.build();

	}

	private ApiInfo getApiInfo() {

		return new ApiInfoBuilder()

				.title("Swagger API Doc")

				.description("More description about the API")

				.version("1.0.0")

				.build();

	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
	}

}