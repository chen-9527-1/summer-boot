package org.summerframework.ddd.test.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.summerframework.ddd.core.command.CommandBus;
import org.summerframework.ddd.core.command.CommandResult;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
public class CommandBusTest {

    @Autowired
    private CommandBus commandBus;

    @Test
    public void testExecute() throws Exception {
        TestCommand command = new TestCommand();
        String name = "test";
        command.setName(name);
        CommandResult<String> resultObject = commandBus.execute(command);
        System.out.print(resultObject.getMessage());
        TestCase.assertEquals(resultObject.getData(), name);
    }

}
