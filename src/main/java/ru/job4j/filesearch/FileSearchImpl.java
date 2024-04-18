package ru.job4j.filesearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import static java.nio.file.FileVisitResult.CONTINUE;

public class FileSearchImpl implements FileSearch {
    private static final Logger LOG = LoggerFactory.getLogger(FileSearchImpl.class.getName());
    private final List<Path> listResult = new ArrayList<>();

    private final Predicate<Path> filter;

    public FileSearchImpl(Predicate<Path> filter) {
        this.filter = filter;
    }

    public List<Path> getResult() {
        return listResult;
    }

    @Override
    public void searchFiles(String directory, String fileName, String searchType, String output) {
        try {
            Files.walkFileTree(Path.of(directory), EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE,
                    new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path directory,
                                                                 BasicFileAttributes attributes) {
                            return CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                            if (filter.test(file)) {
                                listResult.add(file.toAbsolutePath());
                            }
                            return CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFileFailed(Path file, IOException exc) {
                            LOG.error("Error accessing file: " + file.toString() + System.lineSeparator(), exc);
                            return CONTINUE;
                        }
                    });
        } catch (Exception e) {
            LOG.error("Exception error", e);
        }
    }
}
