package ru.job4j.filesearch.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ArgsName {
    private final static String NAME = "name";
    private final static String REGEX = "regex";
    private static final List<String> ERROR_MESSAGES = new ArrayList<>();
    private final Map<String, String> values = new HashMap<>();

    public String get(String key) {
        if (!values.containsKey(key)) {
            throw new IllegalArgumentException("This key: '" + key + "' is missing");
        }
        return values.get(key);
    }

    private void parse(String[] args) {
        for (String it : args) {
            String[] tmpIt = it.substring(1).split("=", 2);
            values.put(tmpIt[0], tmpIt[1]);
        }
    }

    private static boolean dataValidation(String[] args) {
        if (args.length == 0) {
            ERROR_MESSAGES.add("Arguments not passed to program");
            return true;
        }
        boolean res = false;
        for (String it : args) {
            if (!it.startsWith("-")) {
                ERROR_MESSAGES.add("Error: This argument '" + it + "' does not start with a '-' character");
                res = true;
            }
            if (!it.contains("=")) {
                ERROR_MESSAGES.add("Error: This argument '" + it + "' does not contain an equal sign");
                res = true;
            }
            String tmp = it.substring(1);
            String[] tmpIt = tmp.split("=", 2);
            if (tmpIt[0].isBlank()) {
                ERROR_MESSAGES.add("Error: This argument '" + it + "' does not contain a key");
                res = true;
            }
            if (tmpIt[1].isBlank()) {
                ERROR_MESSAGES.add("Error: This argument '" + it + "' does not contain a value");
                res = true;
            }
        }
        return res;
    }

    private static void printInfo() {
        System.out.println("Программа для поиска файлов в заданном каталоге и подкаталогах.");
        System.out.println("Использование: java -jar job4j_finder.jar -d=<директория> -n=<имя файла> [-t=<тип поиска>] [-o=<файл результата>]");
        System.out.println();
        System.out.println("Параметры:");
        System.out.println("  -d=<директория>   Директория, в которой начинать поиск.");
        System.out.println("  -n=<имя файла файла файла>    Имя файла, маска или регулярное выражение.");
        System.out.println("  -t=<тип поиска>   Тип поиска: mask (по маске), name (по полному совпадению имени), regex (по регулярному выражению). (Необязательно, по умолчанию: mask)");
        System.out.println("  -o=<файл>         Файл, в который будет записан результат поиска. (Необязательно)");
        System.out.println();
        System.out.println("Примеры использования:");
        System.out.println("  java -jar job4j_finder.jar -d=c:\\ -n=*.txt -t=mask -o=result.txt");
        System.out.println("  java -jar job4j_finder.jar -d=c:\\ -n=file.txt -t=name -o=result.txt");
        System.out.println("  java -jar job4j_finder.jar -d=c:\\ -n=.*\\.txt -t=regex -o=result.txt");
    }

    private static void checkCondition(boolean condition) {
        if (condition) {
            printInfo();
            StringBuilder commonMessage = new StringBuilder();
            ERROR_MESSAGES
                    .forEach(str -> commonMessage.append(str).append(System.lineSeparator()));
            throw new IllegalArgumentException(commonMessage.toString());
        }
    }

    public static ArgsName of(String[] args) {
        boolean check = dataValidation(args);
        checkCondition(check);
        ArgsName names = new ArgsName();
        names.parse(args);
        check = postIOParamValidate(names);
        checkCondition(check);
        return names;
    }

    private static boolean postIOParamValidate(ArgsName names) {
        boolean res = false;
        Path path = Paths.get(names.get("d"));
        if (!Files.isDirectory(path)) {
            ERROR_MESSAGES.add("Directory " + path + "does not exist");
            res = true;
        }
        return res;
    }

    public static Predicate<Path> configureForSearch(ArgsName names) {
        Predicate<Path> res;
        if (names.get("t").equals(NAME)) {
            res = file -> names.get("n").equals(file.getFileName().toString());
        } else if (names.get("t").equals(REGEX)) {
            res = file -> {
                String filename = file.getFileName().toString();
                return filename.matches(names.get("n"));
            };
        } else {
            String fileName = names.get("n");
            fileName = fileName.replace(".", "\\.").replace("*", ".*").replace("?", ".");
            String finalFileName = fileName;
            res = file -> file.getFileName().toString().matches(finalFileName);
        }
        return res;
    }
}
