package com.prac_icsd2.serviceI;

import java.time.LocalDate;

import com.prac_icsd2.enums.SubscriptionType;

public interface EmailService {
	
	void sendWelcomeEmail(String toEmail,String name, SubscriptionType subscriptionType, LocalDate startDate,LocalDate expiryDate );
	
	void sendExpiryReminderEmail(String toEmail,String name, LocalDate expiryDate);
}
