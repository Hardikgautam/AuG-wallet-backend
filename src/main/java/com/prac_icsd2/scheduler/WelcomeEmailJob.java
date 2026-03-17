package com.prac_icsd2.scheduler;

import java.time.LocalDate;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prac_icsd2.enums.SubscriptionType;
import com.prac_icsd2.serviceI.EmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WelcomeEmailJob implements Job{

	
	@Autowired 
	private EmailService emailService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDataMap dataMap = context.getMergedJobDataMap();
		
		String toEmail = dataMap.getString("toEmail");
		String name = dataMap.getString("name");
		String subType = dataMap.getString("subscriptionType");
		String startDatestr = dataMap.getString("startDate");
		String expiryDateStr = dataMap.getString("expiryDate");
		
		SubscriptionType subscriptionType = SubscriptionType.valueOf(subType);
		LocalDate startDate = LocalDate.parse(startDatestr);
		LocalDate expiryDate = LocalDate.parse(expiryDateStr);
		
		log.info("Quartz sending welcomeemailjonb for:{}",toEmail);
		emailService.sendWelcomeEmail(toEmail,name,subscriptionType,startDate,expiryDate);
	}

}
