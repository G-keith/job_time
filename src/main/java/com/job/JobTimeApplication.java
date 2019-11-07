package com.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author keith
 * @version 1.0
 * @date 2019-10-28
 */
@SpringBootApplication
@MapperScan("com.job.mapper")
public class JobTimeApplication {

//    @Bean
//    public ObjectMapper objectMapper() {
//        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//    }
    public static void main(String[] args) {
        SpringApplication.run(JobTimeApplication.class, args);
    }

}
