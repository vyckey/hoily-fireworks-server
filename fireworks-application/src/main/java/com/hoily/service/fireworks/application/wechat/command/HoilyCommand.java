package com.hoily.service.fireworks.application.wechat.command;

import lombok.Data;
import picocli.CommandLine;

/**
 * Hoily command
 *
 * @author vyckey
 */
@Data
@CommandLine.Command(name = "hoily", mixinStandardHelpOptions = true)
public class HoilyCommand implements Runnable {
    public static final String HOILY_CMD = "/hoily";

    private boolean help;

    public static boolean isCommand(String content) {
        return content.startsWith(HOILY_CMD);
    }

    @Override
    public void run() {
    }
}
