package org.example.analyzer;

import org.example.model.Metrics;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarMetricsAnalyzer {

    public Metrics analyze(String jarFilePath) throws IOException {
        MetricsCollector collector = new MetricsCollector();

        try (JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                if (entry.getName().endsWith(".class")) {
                    collector.processClass(jarFile.getInputStream(entry));
                }
            }
        }

        return collector.calculateMetrics();
    }
}
