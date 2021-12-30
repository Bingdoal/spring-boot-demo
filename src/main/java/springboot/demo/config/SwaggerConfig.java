package springboot.demo.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Value("${swagger.enable}")
    private boolean swaggerEnable;

    @Bean
    public Docket swaggerSetting() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Spring boot demo")
                .description("Restful api of spring boot demo")
                .version("0.0")
                .build();

        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .ignoredParameterTypes(Pageable.class)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build().enable(swaggerEnable);
    }
}
