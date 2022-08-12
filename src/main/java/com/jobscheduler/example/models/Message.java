package com.jobscheduler.example.models;

import com.jobscheduler.example.types.MessageStep;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Message {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private Long id;

    private String message;
    private LocalDateTime messageTime;
    @OneToOne
    private People sentBy;
    @OneToOne
    private People sendTo;
    @OneToOne
    private Message parentMessage;
    @Enumerated(EnumType.STRING)
    private MessageStep step;

    private String conversationId;
    private String time;
    public String getMessage() {
        if(message == null){
            return message;
        }
        return this.message
                .replace("<Customer.Firstname>", sendTo.getFirstName())
                .replace("<Employee.name>", sentBy.getFirstName());
    }
}
