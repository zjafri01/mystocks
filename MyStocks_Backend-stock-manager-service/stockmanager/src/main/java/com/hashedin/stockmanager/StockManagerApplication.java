package com.hashedin.stockmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaServer
public class StockManagerApplication {
	@Bean
	public RestTemplate getRestTemplate(){
//		HttpComponentsClientHttpRequestFactory reqFactory=new HttpComponentsClientHttpRequestFactory();
//		reqFactory.setConnectTimeout(3000);
//		return new RestTemplate(reqFactory);
		//return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		return restTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(StockManagerApplication.class, args);
	}

}
