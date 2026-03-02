package com.example.vehicle_auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class VehicleAuctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleAuctionApplication.class, args);
	}

}
