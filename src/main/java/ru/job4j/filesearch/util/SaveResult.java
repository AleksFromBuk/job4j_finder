package ru.job4j.filesearch.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

public class SaveResult {
    private static final Logger LOG = LoggerFactory.getLogger(SaveResult.class.getName());

    public static void printResult(List<Path> res, String outputPath) {
        try (var writer = new PrintWriter(new FileWriter(outputPath, StandardCharsets.UTF_8, true))) {
           res.forEach(writer::println);
        } catch (IOException e) {
            LOG.error("I/O error", e);
        }
    }
}
