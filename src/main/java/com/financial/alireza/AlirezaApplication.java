package com.financial.alireza;

import com.financial.alireza.transactions.config.TransferFeeProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TransferFeeProperties.class)
public class AlirezaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlirezaApplication.class, args);
	}

}
