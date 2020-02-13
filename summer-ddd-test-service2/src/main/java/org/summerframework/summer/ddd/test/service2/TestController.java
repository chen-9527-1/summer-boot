package org.summerframework.summer.ddd.test.service2;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.summerframework.ddd.core.command.CommandResult;
import org.summerframework.ddd.core.command.annotation.CommandService;

@RestController
public class TestController {

    @CommandService
    public CommandResult<String> testCommand(@RequestBody RemoteCommand command) throws Exception {
        CommandResult<String> result = CommandResult.SUCCESS(command.getName());
        return result;
        // throw new Exception("测试抛出错误");
    }

}
