package org.summerframework.ddd.core.command;

import java.util.Map;

/**
 * 命令上下文
 */
public interface CommandContext {

    Map<String, String> getValues();

}
