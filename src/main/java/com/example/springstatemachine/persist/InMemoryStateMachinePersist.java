package com.example.springstatemachine.persist;

import com.example.springstatemachine.statemachine.Events;
import com.example.springstatemachine.statemachine.States;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import java.util.HashMap;
import java.util.UUID;

public class InMemoryStateMachinePersist implements StateMachinePersist<States, Events, UUID> {
    private HashMap<UUID, StateMachineContext<States,Events>> storage = new HashMap<>();

    @Override
    public void write(StateMachineContext<States, Events> context, UUID contextObj) throws Exception {
        storage.put(contextObj, context);
    }

    @Override
    public StateMachineContext<States, Events> read(UUID contextObj) throws Exception {
        return storage.get(contextObj);
    }
}
