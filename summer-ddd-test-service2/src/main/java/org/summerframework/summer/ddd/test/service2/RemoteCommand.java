package org.summerframework.summer.ddd.test.service2;

import org.summerframework.ddd.core.command.Command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoteCommand implements Command {

    private String name;

}
