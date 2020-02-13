package org.summerframework.ddd.core.command.standard;

import java.util.Map;

import org.summerframework.ddd.core.command.CommandContext;

public class EmptyCommandContext implements CommandContext {

    @Override
    public Map<String, String> getValues() {
        return null;
    }

}
