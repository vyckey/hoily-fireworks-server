package com.hoily.service.fireworks.application.wechat.command;

import picocli.CommandLine;

/**
 * description is here
 *
 * @author vyckey
 */
public class CommandLineExceptionHandler implements
        CommandLine.IParameterExceptionHandler,
        CommandLine.IExecutionExceptionHandler {
    private CommandLine.ParameterException parameterException;
    private Exception executionException;

    @Override
    public int handleParseException(CommandLine.ParameterException e, String[] strings) throws Exception {
        this.parameterException = e;
        return 1;
    }

    @Override
    public int handleExecutionException(Exception e, CommandLine commandLine, CommandLine.ParseResult parseResult) throws Exception {
        this.executionException = e;
        return 2;
    }

    public Exception getException() {
        return parameterException != null ? parameterException : executionException;
    }
}
