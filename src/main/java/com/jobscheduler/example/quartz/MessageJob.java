package com.jobscheduler.example.quartz;

import com.jobscheduler.example.Templates;
import com.jobscheduler.example.models.Message;
import com.jobscheduler.example.repositories.MessageRepository;
import com.jobscheduler.example.repositories.PeopleRepository;
import com.jobscheduler.example.types.MessageStep;
import com.jobscheduler.example.types.UserType;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(MessageJob.class);

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private PeopleRepository peopleRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String messageId = dataMap.getString("messageId");
        log.info("Executing job for message id {}", messageId);
        Message message = messageRepository.findMessageByConversationIdOrderByIdDesc(messageId).get(0);
        if (message != null) {

            if (message.getSentBy().getUserType() == UserType.CUSTOMER || message.getStep() == MessageStep.END) {
                // Delete job
                try {
                    context.getScheduler().deleteJob(new JobKey(messageId));
                    TriggerKey triggerKey = new TriggerKey(messageId);
                    context.getScheduler().unscheduleJob(triggerKey);
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
            } else {
                // send message customer
                // People staff = peopleRepository.findById(message.getse)
                switch (message.getStep()){
                    //case NEW:
                    case START:
                    case SECOND:
                        System.out.println(Templates.template2
                                .replace("<Customer.Firstname>", message.getSendTo().getFirstName())
                                .replace("<Employee.name>", message.getSentBy().getFirstName()));
                        messageRepository.findById(message.getId()).ifPresent(m ->{
                            m.setStep(MessageStep.OLD);
                            messageRepository.save(m);
                        });
                        Message thirdMsg = message;
                        thirdMsg.setId(null);
                        thirdMsg.setMessage(Templates.template2);
                        thirdMsg.setMessageTime(LocalDateTime.now());
                        thirdMsg.setStep(MessageStep.THIRD);
                        thirdMsg.setParentMessage(message);
                        messageRepository.save(thirdMsg);
                        break;
                    case THIRD:
                        System.out.println(Templates.template3
                                .replace("<Customer.Firstname>", message.getSendTo().getFirstName())
                                .replace("<Employee.name>", message.getSentBy().getFirstName()));
                        messageRepository.findById(message.getId()).ifPresent(m ->{
                            m.setStep(MessageStep.OLD);
                            messageRepository.save(m);
                        });

                        Message finalMsg = message;
                        finalMsg.setId(null);
                        finalMsg.setMessage(Templates.template3);
                        finalMsg.setMessageTime(LocalDateTime.now());
                        finalMsg.setStep(MessageStep.END);
                        finalMsg.setParentMessage(message);
                        messageRepository.save(finalMsg);
                        break;
                }
            }
        }
    }
}
