package com.jobscheduler.example.services;

import com.jobscheduler.example.Templates;
import com.jobscheduler.example.configurations.QuartzConfig;
import com.jobscheduler.example.models.Message;
import com.jobscheduler.example.models.MessageDto;
import com.jobscheduler.example.quartz.MessageJob;
import com.jobscheduler.example.repositories.MessageRepository;
import com.jobscheduler.example.repositories.PeopleRepository;
import com.jobscheduler.example.types.MessageStep;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final PeopleRepository peopleRepository;
    private final MessageRepository messageRepository;
    private final QuartzConfig quartzConfig;

    public void startConversation(Long id) throws Exception {
        var inDb = fetchAllMessages(id);
        if (inDb != null && inDb.size() > 0) {
            return;
        }

        Message message = new Message();
        message.setMessage(Templates.template1);
        message.setMessageTime(LocalDateTime.now());
        message.setStep(MessageStep.START);
        message.setSendTo(peopleRepository.findById(id).get());
        message.setSentBy(peopleRepository.findById(4L).get());
        message.setConversationId(UUID.randomUUID().toString());
        messageRepository.save(message);
        scheduler(message);
    }

    public List<Message> fetchAllMessages(Long id) {
        var msg = messageRepository.findAllBySendToIdAndParentMessageIsNull(id);
        if (msg == null){
            return null;
        }

        var testMsg = messageRepository.findMessageByConversationIdOrderByIdAsc(msg.getConversationId());
        testMsg.forEach(m -> m.setTime(DateTimeFormatter.ofPattern("HH:mm").format(m.getMessageTime())));
        return testMsg;
    }

    public void saveMessage(MessageDto messageDto, Long id) {
        var parent = messageRepository.findAllBySendToIdAndParentMessageIsNull(id);
        if (parent != null) {
            Message message = new Message();
            message.setMessage(messageDto.getMessage());
            message.setMessageTime(LocalDateTime.now());
            message.setStep(MessageStep.END);
            message.setSendTo(peopleRepository.findById(4L).get());
            message.setSentBy(peopleRepository.findById(id).get());
            message.setParentMessage(parent);
            message.setConversationId(parent.getConversationId());
            messageRepository.save(message);
        }
    }

    private void scheduler(Message message) throws Exception {
        // Creating JobDetail instance
        String id = message.getConversationId();
        JobDetail jobDetail = JobBuilder.newJob(MessageJob.class).withIdentity(id).build();
        jobDetail.getJobDataMap().put("messageId", id);
        LocalDateTime triggerJobAt = message.getMessageTime().plusMinutes(1);
        Date dt = Date.from(triggerJobAt.atZone(ZoneId.systemDefault()).toInstant());

        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity(id)
                .startAt(dt).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(2)
                        .withMisfireHandlingInstructionFireNow().repeatForever())
                .build();
        // Getting scheduler instance
        Scheduler scheduler = quartzConfig.schedulerFactoryBean().getScheduler();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }
}
