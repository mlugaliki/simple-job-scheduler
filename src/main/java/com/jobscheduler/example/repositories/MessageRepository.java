package com.jobscheduler.example.repositories;

import com.jobscheduler.example.models.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findAllBySendToId(Long id);
    @Query("SELECT m FROM Message m WHERE m.sendTo.id=:id AND m.parentMessage.id = null order by m.id")
    Message findAllBySendToIdAndParentMessageIsNull(@Param("id") Long id);

    List<Message> findMessageByConversationIdOrderByIdDesc(String conversationId);
    List<Message> findMessageByConversationIdOrderByIdAsc(String conversationId);
}
