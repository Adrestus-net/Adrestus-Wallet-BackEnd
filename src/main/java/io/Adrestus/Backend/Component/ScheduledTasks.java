package io.Adrestus.Backend.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private SimpMessagingTemplate template;

    private int counter = 0;

    @Scheduled(fixedRate = 2000)
    public void fireGreeting() {
        this.template.convertAndSend("/topic/user", "adssdas" + counter);
        counter++;
    }
}
