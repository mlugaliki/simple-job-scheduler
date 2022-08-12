package com.jobscheduler.example.controllers;

import com.jobscheduler.example.models.MessageDto;
import com.jobscheduler.example.services.MessageService;
import com.jobscheduler.example.services.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class HomeController {
    private final PeopleService peopleService;
    private final MessageService messageService;

    @GetMapping("/")
    public String home(Model model) {
        var people = peopleService.getCustomers();
        model.addAttribute("customers", people);
        return "index";
    }

    @GetMapping("/conversations/{id}")
    public String sendMessage(Model model, @PathVariable Long id) throws Exception {
        messageService.startConversation(id);
        var customer = peopleService.getCustomerById(id);
        var messages = messageService.fetchAllMessages(id);
        model.addAttribute("customer", customer);
        model.addAttribute("messages", messages);
        model.addAttribute("form", new MessageDto());
        return "conversations";
    }

    @PostMapping("/conversations/{id}")
    public String saveMessage(Model model, @ModelAttribute MessageDto messageDto, @PathVariable Long id) {
        messageService.saveMessage(messageDto, id);
        return "redirect:/conversations/"+id;
    }

    @GetMapping("/refresh/{id}")
    public String refresh(Model model, @PathVariable Long id) {
        return "redirect:/conversations/"+id;
    }
}
