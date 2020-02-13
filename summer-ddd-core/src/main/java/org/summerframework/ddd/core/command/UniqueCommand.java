package org.summerframework.ddd.core.command;

/**
 * 命令接口(用于幂等判断场景)
 */
public interface UniqueCommand extends Command {

    /**
     * 命令的唯一ID
     */
    public String getCommandId();

}
