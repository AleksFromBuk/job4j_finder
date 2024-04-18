package ru.job4j.filesearch;

import ru.job4j.filesearch.util.ArgsName;
import ru.job4j.filesearch.util.SaveResult;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        ArgsName names = ArgsName.of(args);
        Predicate<Path> predicate = ArgsName.configureForSearch(names);
        FileSearch fileSearch = new FileSearchImpl(predicate);
        fileSearch.searchFiles(names.get("d"), names.get("n"), names.get("t"), names.get("o"));
        List<Path> findResult = fileSearch.getResult();
        SaveResult.printResult(findResult, names.get("o"));
    }
}
