package com.example.javafxapp.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogUtils {
    private static final String LOG_DIR = "logs";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        createLogDirectory();
    }

    private static void createLogDirectory() {
        try {
            Path logPath = Paths.get(LOG_DIR);
            if (!Files.exists(logPath)) {
                Files.createDirectories(logPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logError(String message, Exception e) {
        String logMessage = String.format("[ERROR] %s - %s: %s%n",
                LocalDateTime.now().format(formatter),
                message,
                e.getMessage());
        writeToLog(logMessage);
        e.printStackTrace();
    }

    public static void logInfo(String message) {
        String logMessage = String.format("[INFO] %s - %s%n",
                LocalDateTime.now().format(formatter),
                message);
        writeToLog(logMessage);
    }

    public static void logWarning(String message) {
        String logMessage = String.format("[WARNING] %s - %s%n",
                LocalDateTime.now().format(formatter),
                message);
        writeToLog(logMessage);
    }

    private static void writeToLog(String message) {
        try {
            String fileName = String.format("%s/app_%s.log",
                    LOG_DIR,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            Path logFile = Paths.get(fileName);
            if (!Files.exists(logFile)) {
                Files.createFile(logFile);
            }

            Files.write(logFile,
                    message.getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}