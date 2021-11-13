package com.example.messagedispatcher;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

@EnableIntegration
@Configuration
@Slf4j
public class IntegrationConfig {

    @Autowired
    @Qualifier("directChannel")
    private MessageChannel messageChannel;

    @Bean
    public MessageChannel directChannel(){
        return new DirectChannel();
    }


    @ServiceActivator(inputChannel = "directChannel")
    public void doStuff(Message message)
    {
        log.info("Coming from Handler 1" + (String)message.getPayload());
    }

    @ServiceActivator(inputChannel = "directChannel")
    public void doStuff2(Message message)
    {
        log.info("Coming from Handler 2" + (String)message.getPayload());
    }


    @Bean
    CommandLineRunner sendStuffToChannel(){
       return args -> {
           for (int i = 0; i < 100; i++) {
               messageChannel.send(MessageBuilder.withPayload("message n°" + i).build());
           }
       };
    }

}
