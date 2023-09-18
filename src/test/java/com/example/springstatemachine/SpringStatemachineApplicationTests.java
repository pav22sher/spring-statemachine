package com.example.springstatemachine;

import com.example.springstatemachine.statemachine.Events;
import com.example.springstatemachine.statemachine.States;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SpringStatemachineApplicationTests {

    private StateMachine<States, Events> stateMachine;

    @Autowired
    private StateMachineFactory<States, Events> stateMachineFactory;

    @BeforeEach
    public void setUp() {
        stateMachine = stateMachineFactory.getStateMachine();
    }

    @Test
    public void contextLoads() {
        Assertions.assertThat(stateMachine).isNotNull();
    }

    @Test
    public void initialStateTest() {
        // Asserts
        Assertions.assertThat(stateMachine.getInitialState().getId()).isEqualTo(States.BACKLOG);
    }

    @Test
    public void firstStepTest() {
        // Act
        stateMachine.sendEvent(Events.START_FEATURE);
        // Asserts
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.IN_PROGRESS);
    }

    @Test
    public void testGreenWay() {
        // Arrange
        // Act
        stateMachine.sendEvent(Events.START_FEATURE);
        stateMachine.sendEvent(Events.DEPLOY);
        stateMachine.sendEvent(Events.FINISH_FEATURE);
        stateMachine.sendEvent(Events.QA_CHECKED_UC);
        // Asserts
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.DONE);
    }

    @Test
    public void testWrongWay() {
        // Arrange
        // Act
        stateMachine.sendEvent(Events.START_FEATURE);
        stateMachine.sendEvent(Events.QA_CHECKED_UC);
        // Asserts
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.IN_PROGRESS);
    }

    @Test
    public void rockStarTest() {
        // Act
        stateMachine.sendEvent(Events.ROCK_STAR_DOUBLE_TASK);
        // Asserts
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.TESTING);
    }

    @Test
    public void testingUnreachableWithoutDeploy() {
        // Arrange & Act
        stateMachine.sendEvent(Events.START_FEATURE);
        stateMachine.sendEvent(Events.FINISH_FEATURE);
        stateMachine.sendEvent(Events.QA_CHECKED_UC); // not accepted!
        // Asserts
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.IN_PROGRESS);
    }

    @Test
    public void testDeployFromBacklog() {
        // Arrange
        // Act
        stateMachine.sendEvent(Events.DEPLOY);
        // Asserts
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.BACKLOG);
        Assertions.assertThat(stateMachine.getExtendedState().getVariables().get("deployed")).isEqualTo(true);
    }

}
