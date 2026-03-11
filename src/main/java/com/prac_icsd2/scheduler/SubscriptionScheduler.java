package com.prac_icsd2.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.prac_icsd2.enums.SubscriptionType;
import com.prac_icsd2.model.Customer;
import com.prac_icsd2.repo.CustomerRepo;
import com.prac_icsd2.repo.SubscriptionRepo;
import com.prac_icsd2.serviceI.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor

public class SubscriptionScheduler {

	
	@Autowired
	EmailService emailService;
	@Autowired
	CustomerRepo customerrepo;
	@Autowired
	SubscriptionRepo subrepo;
	
	@Scheduled(cron="0 * * * * *")
	public void sendOneDayExpiryReminder() {
		LocalDate tommorow = LocalDate.now().plusDays(1);
		try {
			List<Customer> expiringTommorow = customerrepo
					.findBySubscription_PlanExpiryDateAndSubscription_SubscriptionTypeNot(
							tommorow,
							SubscriptionType.EXPIRED);
			
			for(Customer c:expiringTommorow) {
				String firstName = c.getFirstName();
				String lastName = c.getLastName();
				String name = firstName.concat(" "+lastName);
				String email = c.getEmailId();
				LocalDate expiryDate = c.getSubscription().getPlanExpiryDate();
				
				emailService.sendExpiryReminderEmail(email, name, expiryDate);
				log.info("Reminder sent to :{} for expiry on {}",email,expiryDate);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Error at scheduling while sending sendOneDayExpiryReminder"+e);
		}	}
	
	
	@Scheduled(cron="0 0 0 * * *")
	public void markExpiredPlans() {
		LocalDate today = LocalDate.now();
		
//		List<Customer> expired = customerrepo
//				.findBySubscription_PlanExpiryDateBeforeAndSubscription_SubscriptionTypeNot(
//						today,SubscriptionType.EXPIRED
//						);
//		for(Customer c:expired) {
//			if(c.getSubscription()!=null) {
//			c.getSubscription.setSubscriptionType(SubscriptionType.EXPIRED);
//			customerrepo.save(c);
//			log.info("Plan marked expired for :{}",c.getEmailId());
//		}
		
	//}
		
		int updatedCount = subrepo.bulkMarkExpired(today, SubscriptionType.EXPIRED);
		
		log.info("Batch update Complete. {} subscription marked as Expired ",updatedCount);
	}
}
