package com.thedevlair.business.courses;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Business Courses API", version = "1.0.0"))
@EnableCaching
public class BusinessCoursesV1Application {

    public static void main(String[] args) {
        SpringApplication.run(BusinessCoursesV1Application.class, args);
    }

}
