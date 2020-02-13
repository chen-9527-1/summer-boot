package org.summerframework.ddd.test.service1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.summerframework.ddd.core.command.CommandBus;
import org.summerframework.ddd.core.command.CommandResult;
import org.summerframework.ddd.core.command.annotation.CommandWeb;

@RestController
public class TestController {

    @Autowired
    private CommandBus commandBus;

    @CommandWeb
    public CommandResult<String> testCommand(@RequestBody TestCommand command) throws Exception {
        // CommandResultObject<String> result =
        // CommandResultObject.SUCCESS(command.getName());
        RemoteCommand remoteCommand = new RemoteCommand();
        remoteCommand.setName(command.getName());
        CommandResult<String> result = commandBus.execute(remoteCommand);
        return result;
        // throw new Exception("测试抛出错误");
    }

}
