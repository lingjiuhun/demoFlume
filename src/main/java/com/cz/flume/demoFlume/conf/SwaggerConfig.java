package com.cz.flume.demoFlume.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @describe: Swagger 配置
 * @date 2018/06/19
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SecurityScheme apiKey() {
        return new ApiKey("access_token", "accessToken", "header");
    }

    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cz.flume.demoFlume"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(setTokenParam());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("项目 API")
                .version("1.0")
                .build();
    }

    private List<Parameter> setTokenParam() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("query").required(false).build();
        pars.add(tokenPar.build());
        return pars;
    }


    /*@PostConstruct
    public void setObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        objectMapper.registerModule(module);

        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {

            @Override
            public boolean isAnnotationBundle(Annotation ann) {
                if (ann.annotationType() == JSONField.class) {
                    return true;
                }
                return super.isAnnotationBundle(ann);
            }

            @Override
            public PropertyName findNameForSerialization(Annotated a) {
                PropertyName nameForSerialization = super.findNameForSerialization(a);
                if (nameForSerialization == null || nameForSerialization == PropertyName.USE_DEFAULT) {
                    JSONField jsonField = _findAnnotation(a, JSONField.class);
                    if (jsonField != null) {
                        return PropertyName.construct(jsonField.name());
                    }
                }
                return nameForSerialization;
            }

            @Override
            public PropertyName findNameForDeserialization(Annotated a) {
                PropertyName nameForDeserialization = super.findNameForDeserialization(a);
                if (nameForDeserialization == null || nameForDeserialization == PropertyName.USE_DEFAULT) {
                    JSONField jsonField = _findAnnotation(a, JSONField.class);
                    if (jsonField != null) {
                        return PropertyName.construct(jsonField.name());
                    }
                }
                return nameForDeserialization;
            }
        });

        ObjectMapperConfigured objectMapperConfigured = new ObjectMapperConfigured(applicationContext, objectMapper);
        applicationContext.publishEvent(objectMapperConfigured);
    }*/
}
