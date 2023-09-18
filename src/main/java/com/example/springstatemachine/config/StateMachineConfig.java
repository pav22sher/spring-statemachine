package com.example.springstatemachine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true) //автозапуск
                .listener(listener());
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to) {
                System.out.println("State change to " + to.getId());
            }
        };
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states
                .withStates()
                .initial(States.SI) //начальное состояние
                .end(States.SF) //конечное состояние
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal()
                .source(States.SI)
                .target(States.S1)
                .event(Events.E1)
                .guardExpression("true")
                .and()

                .withExternal()
                .source(States.S1)
                .target(States.S2)
                .event(Events.E2)
                .guard(guard())
                .action(action(), errorAction());
    }

    @Bean
    public Guard<States, Events> guard() {
        return context -> true;
    }

    @Bean
    public Action<States, Events> action() {
        return context -> {
            System.out.println("Action");
            throw new RuntimeException("MyError");
        };
    }

    @Bean
    public Action<States, Events> errorAction() {
        return context -> {
            // RuntimeException("MyError") added to context
            Exception exception = context.getException();
            System.out.println(exception.getMessage());;
        };
    }

    /*
    //выбор first/then/last эквивалентна if/elseif/else
    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
                .withChoice()
                .source(States.S1)
                .first(States.S2, s2Guard())
                .then(States.S3, s3Guard())
                .last(States.S4);
    }
    //вилка
    @Override
    public void configure(StateMachineTransitionConfigurer<States2, Events> transitions)
            throws Exception {
        transitions
                .withFork()
                .source(States2.S2)
                .target(States2.S22)
                .target(States2.S32);
    }
    //соединение
    @Override
	public void configure(StateMachineTransitionConfigurer<States2, Events> transitions)
			throws Exception {
		transitions
			.withJoin()
				.source(States2.S2F)
				.source(States2.S3F)
				.target(States2.S4)
				.and()
			.withExternal()
				.source(States2.S4)
				.target(States2.S5);
	}
    */
}
