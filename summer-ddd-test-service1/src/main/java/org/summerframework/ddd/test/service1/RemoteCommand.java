package org.summerframework.ddd.test.service1;

import org.summerframework.ddd.core.command.ObjectCommand;
import org.summerframework.ddd.core.command.annotation.CommandClient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CommandClient(serviceName = "test-service2")
public class RemoteCommand implements ObjectCommand<String> {

    private String name;

}
