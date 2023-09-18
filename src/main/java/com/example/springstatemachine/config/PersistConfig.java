package com.example.springstatemachine.config;

import com.example.springstatemachine.persist.InMemoryStateMachinePersist;
import com.example.springstatemachine.statemachine.Events;
import com.example.springstatemachine.statemachine.States;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.UUID;

@Configuration
public class PersistConfig {
    @Bean
    public StateMachinePersist<States, Events, UUID> inMemoryPersist() {
        return new InMemoryStateMachinePersist();
    }

    @Bean
    public StateMachinePersister<States, Events, UUID> persister(StateMachinePersist<States, Events, UUID> defaultPersist) {
        return new DefaultStateMachinePersister<>(defaultPersist);
    }
}
