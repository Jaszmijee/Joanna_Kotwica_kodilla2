package com.crud.tasks.service;

import com.crud.tasks.domain.Task;
import com.crud.tasks.repository.TaskRepository;
import com.crud.tasks.trello.config.AdminConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;

@Service
public class MailCreatorService {

    @Autowired
    private AdminConfig adminConfig;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;

    public String buildTrelloCardEmail(String message) {
        List<String> functionality = new ArrayList<>();
        functionality.add("You can manage your tasks");
        functionality.add("Provides connection with Trello Account");
        functionality.add("Application allows sending tasks to Trello");

        Context context = new Context();
        context.setVariable("message", message);
        context.setVariable("tasks_url", "http://localhost:8888/tasks_frontend/");
        context.setVariable("button", "Visit website");
        context.setVariable("admin_config", adminConfig);
        context.setVariable("preview", "CHECK YOUR NEW DAILY TASK!");
        context.setVariable("signature", adminConfig.getCompanyName());
        context.setVariable("show_button", false);
        context.setVariable("is_friend", true);
        context.setVariable("application_functionality", functionality);
        return templateEngine.process("mail/created-trello-card-mail", context);
    }


    public String buildScheduledMail(String message) {
        List<String> tasksNames = new ArrayList<>();
        List<Task> tasksList = taskRepository.findAll();
        for (Task t : tasksList) {
            tasksNames.add(t.getTitle());
        }

        Context context = new Context();
        context.setVariable("preview", "WHAT'S AWAITING YOU TODAY!");
        context.setVariable("message", message);
        context.setVariable("tasks_url", "http://localhost:8888/tasks_frontend/");
        context.setVariable("button", "Click here to see your tasks");
        context.setVariable("show_button", true);
        context.setVariable("admin_config", adminConfig);
        context.setVariable("is_boss", false);
        context.setVariable("employee_name", "Employee: insert_name_here");
        context.setVariable("list_of_tasks", tasksNames);
        context.setVariable("goodbye_message", "Have a very productive day!");
        context.setVariable("i_couldnt_resist_url", "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        context.setVariable("button1", "i_just_couldn't_resist :)");
        return templateEngine.process("mail/scheduled-email", context);
    }

}

