package org.example.output;

import org.example.model.Metrics;

public class ConsoleOutputFormatter {

        public void print(Metrics metrics) {
                String separator = "============================================================";
                System.out.println(separator);
                System.out.println("         МЕТРИКИ АНАЛИЗА БАЙТКОДА");
                System.out.println(separator);
                System.out.println();

                System.out.printf("%-45s %d%n",
                                "Максимальная глубина наследования:",
                                metrics.getMaxInheritanceDepth());

                System.out.printf("%-45s %.2f%n",
                                "Средняя глубина наследования:",
                                metrics.getAvgInheritanceDepth());

                System.out.printf("%-45s %d%n",
                                "Метрика ABC (присваивания):",
                                metrics.getTotalAbcMetric());

                System.out.printf("%-45s %.2f%n",
                                "Среднее количество переопределенных методов:",
                                metrics.getAvgOverriddenMethods());

                System.out.printf("%-45s %.2f%n",
                                "Среднее количество полей в классе:",
                                metrics.getAvgFieldsPerClass());

                System.out.println();
                System.out.println(separator);
        }
}
