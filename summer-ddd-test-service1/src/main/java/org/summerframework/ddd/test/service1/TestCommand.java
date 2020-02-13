package org.summerframework.ddd.test.service1;

import org.summerframework.ddd.core.command.Command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestCommand implements Command {

    private String name;

}
