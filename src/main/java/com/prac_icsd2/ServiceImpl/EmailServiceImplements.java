package com.prac_icsd2.ServiceImpl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.prac_icsd2.enums.SubscriptionType;
import com.prac_icsd2.serviceI.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImplements implements EmailService{
	
	@Autowired
	 JavaMailSender emailSender;
	
	@Override
	public void sendWelcomeEmail(String toEmail, String name, SubscriptionType subscriptionType, LocalDate startDate,
			LocalDate expiryDate) {
		// TODO Auto-generated method stub
		
		try {
			SimpleMailMessage msg  = new SimpleMailMessage();
			msg.setTo(toEmail);
			msg.setSubject("Welcome!"+name+"Your Subscription Plan:"+subscriptionType.name()+"Is Activated...");
			msg.setText("Welcome! Email id:"+toEmail +"You have :"+subscriptionType.name()+"From Date:"+startDate+"_to_"+expiryDate +"Your Plan is Active"+"Enjoy Your Plan!\n\n");
			emailSender.send(msg);
			
			log.info("Welcome email sent to :{}", toEmail);
		}catch(Exception e) {
			log.error("Failed to send Welcome Email to {} : {} ",toEmail, e.getMessage());
		}
	}

	@Override
	public void sendExpiryReminderEmail(String toEmail, String name, LocalDate expiryDate) {
		// TODO Auto-generated method stub
		
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(toEmail);
			msg.setSubject("Your Plan Expires Tommorow!");
			msg.setText("Hi"+name+",\n\n"+
					"This is a Reminder that your Plan is Expired on :"+expiryDate + "\n\n"+
					"Renew Your Current Subscription to Avaoid Any Interruption."
					);
			emailSender.send(msg);
			log.info("Expiry remainder send to : {}, toEmail");
		}catch(Exception e) {
			log.error("Failed to send expiry email to {} :{} ",toEmail,e.getMessage());
		}
	}


}
