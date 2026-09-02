package com.example.vehicle_auction;

import com.example.vehicle_auction.domain.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class VehicleAuctionApplicationTests {

	@MockitoBean
	private NotificationRepository notificationRepository;

	@Test
	void contextLoads() {
	}

}
