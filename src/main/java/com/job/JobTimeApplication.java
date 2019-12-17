package com.job;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;

import static cn.hutool.core.util.ClassLoaderUtil.getClassLoader;

/**
 * @author keith
 * @version 1.0
 * @date 2019-10-28
 */
@SpringBootApplication
@MapperScan("com.job.mapper")
@EnableScheduling
public class JobTimeApplication {

    /**
     * 商户号
     */
    private static final String keyPassword = "1482664972";
    @Bean
    public RestTemplate restTemplate() {
        InputStream keyStream =this.getClass().getClassLoader().getResourceAsStream("apiclient_cert.p12");
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(keyStream, keyPassword.toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        SSLContext sslContext;
        try {
            sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, keyPassword.toCharArray()).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | UnrecoverableKeyException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).build();
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        httpRequestFactory.setConnectTimeout(5000);
        httpRequestFactory.setReadTimeout(3000);
        return new RestTemplate(httpRequestFactory);
    }

    public static void main(String[] args) {
        System.out.println("-----");
        SpringApplication.run(JobTimeApplication.class, args);
    }

}
