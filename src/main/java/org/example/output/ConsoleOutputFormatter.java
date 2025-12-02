package org.example.output;

import org.example.model.Metrics;

public class ConsoleOutputFormatter {

        public void print(Metrics metrics) {
                String separator = "============================================================";
                System.out.println(separator);
                System.out.println("МЕТРИКИ АНАЛИЗА БАЙТКОДА");
                System.out.println(separator);
                System.out.println("Максимальная глубина наследования: " + metrics.getMaxInheritanceDepth());
                System.out.println("Средняя глубина наследования: " + metrics.getAvgInheritanceDepth());
                System.out.println("Метрика ABC (присваивания): " + metrics.getTotalAbcMetric());
                System.out.println("Среднее количество переопределенных методов: " + metrics.getAvgOverriddenMethods());
                System.out.println("Среднее количество полей в классе: " + metrics.getAvgFieldsPerClass());
                System.out.println(separator);
        }
}
