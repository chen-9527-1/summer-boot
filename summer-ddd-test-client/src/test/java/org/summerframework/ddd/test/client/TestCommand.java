package org.summerframework.ddd.test.client;

import org.summerframework.ddd.core.command.ObjectCommand;
import org.summerframework.ddd.core.command.annotation.CommandClient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CommandClient(serviceName = "test-service1")
public class TestCommand implements ObjectCommand<String> {

    private String name;

}
