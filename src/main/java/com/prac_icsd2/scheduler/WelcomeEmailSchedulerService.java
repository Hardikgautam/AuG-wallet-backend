package com.prac_icsd2.scheduler;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.prac_icsd2.enums.SubscriptionType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WelcomeEmailSchedulerService {

	@Autowired
	private Scheduler scheduler;
	
	@Value("${app.welcome.email.delay-minutes:1}")
	private int delayMinutes;
	
	public void scheduleWelcomeEmail(String toEmail,String name, SubscriptionType subscriptionType, LocalDate startDate , LocalDate expiryDate) {
		
		try {
			String jobId = "welcomeEmail_"+UUID.randomUUID();
			
			JobDetail jobDetail = JobBuilder.newJob(WelcomeEmailJob.class)
                    .withIdentity(jobId, "welcome-email-group")
                    .usingJobData("toEmail", toEmail)
                    .usingJobData("name", name)
                    .usingJobData("subscriptionType", subscriptionType.name())
                    .usingJobData("startDate", startDate.toString())
                    .usingJobData("expiryDate", expiryDate.toString())
                    .storeDurably(false)
                    .build();
			
			Date triggerTime = new Date (System.currentTimeMillis() + (long) delayMinutes * 60 * 1000);
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(jobId + "_trigger", "welcome-email-group")
                .startAt(triggerTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withRepeatCount(0)  
                        .withMisfireHandlingInstructionFireNow())
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        log.info("Scheduled welcome email for {} to fire in {} minutes", toEmail, delayMinutes);

    } catch (SchedulerException e) {
        log.error("Failed to schedule welcome email for {}: {}", toEmail, e.getMessage());
    }
		}
	}

