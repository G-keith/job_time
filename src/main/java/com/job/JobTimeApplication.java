package com.job;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @author keith
 * @version 1.0
 * @date 2019-10-28
 */
@SpringBootApplication
@MapperScan("com.job.mapper")
public class JobTimeApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobTimeApplication.class, args);
    }

}
