package org.example;

import org.example.analyzer.JarMetricsAnalyzer;
import org.example.model.Metrics;
import org.example.output.ConsoleOutputFormatter;
import org.example.output.JsonOutputFormatter;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.exit(1);
        }

        String jarFilePath = args[0];
        String outputFilePath = null;

        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--output") && i + 1 < args.length) {
                outputFilePath = args[i + 1];
                i++;
            }
        }

        File jarFile = new File(jarFilePath);
        if (!jarFile.exists()) {
            System.err.println("Ошибка: JAR файл не найден: " + jarFilePath);
            System.exit(1);
        }

        if (!jarFile.isFile()) {
            System.err.println("Ошибка: Указанный путь не является файлом: " + jarFilePath);
            System.exit(1);
        }

        try {
            System.out.println("Анализ JAR файла: " + jarFilePath);

            JarMetricsAnalyzer analyzer = new JarMetricsAnalyzer();
            Metrics metrics = analyzer.analyze(jarFilePath);

            ConsoleOutputFormatter consoleFormatter = new ConsoleOutputFormatter();
            consoleFormatter.print(metrics);

            if (outputFilePath != null) {
                JsonOutputFormatter jsonFormatter = new JsonOutputFormatter();
                jsonFormatter.writeToFile(metrics, outputFilePath);
                System.out.println("Метрики сохранены в файл: " + outputFilePath);
            }

        } catch (IOException e) {
            System.err.println("Ошибка при анализе JAR файла: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
