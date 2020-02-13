package org.summerframework.ddd.core.command;

/**
 * 命令执行总线
 */
public interface CommandBus {

    /**
     * 执行命令并返回结果
     */
    <T> CommandResult<T> execute(ObjectCommand<T> command);

    /**
     * 执行命令
     */
    CommandResult<Void> execute(Command command);

    /**
     * 执行命令(用于反射或者其他特殊封装时).
     * 
     * @param serviceName    命令所在服务的名称
     * @param commandName    命令名称
     * @param commandVersion 命令的版本
     * @param commandData    JSON格式的命令数据
     * @return JSON格式的响应数据
     */
    String invoke(String serviceName, String commandName, String commandVersion, String commandData);

}