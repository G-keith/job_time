package com.job;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * @author keith
 * @version 1.0
 * @date 2019-10-28
 */
@SpringBootApplication
@MapperScan("com.job.mapper")
@EnableScheduling
public class JobTimeApplication {
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(3000);
        return new RestTemplate(requestFactory);
    }

    public static void main(String[] args) {
        SpringApplication.run(JobTimeApplication.class, args);
    }

}
