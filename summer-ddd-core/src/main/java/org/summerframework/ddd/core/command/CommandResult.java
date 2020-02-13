package org.summerframework.ddd.core.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * 命令返回结果
 */
public final class CommandResult<T> {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 返回的代码
     */
    private String code;

    /**
     * 返回的消息
     */
    private String message;

    /**
     * 返回的数据
     */
    private T data;

    public CommandResult(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommandResult() {

    }

    public static <T> CommandResult<T> SUCCESS(T data) {
        return new CommandResult<T>(true, "", "", data);
    }

    public static <T> CommandResult<T> FAILURE(String code, String message) {
        return new CommandResult<T>(false, code, message, null);
    }

    public static <T> CommandResult<T> FAILURE(Throwable exception) {
        return new CommandResult<T>(false, "error", exception.getClass().getName() + ":" + exception.getMessage(),
                null);
    }

}