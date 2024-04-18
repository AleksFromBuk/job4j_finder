package ru.job4j.filesearch;

import java.nio.file.Path;
import java.util.List;

public interface FileSearch {
    void searchFiles(String directory, String fileName, String searchType, String output);

    List<Path> getResult();
}