package org.example.output;

import org.example.model.Metrics;

import java.io.FileWriter;
import java.io.IOException;

public class JsonOutputFormatter {

        public void writeToFile(Metrics metrics, String outputPath) throws IOException {
                String json = toJson(metrics);

                try (FileWriter writer = new FileWriter(outputPath)) {
                        writer.write(json);
                }
        }

        public String toJson(Metrics metrics) {
                StringBuilder json = new StringBuilder();
                json.append("{\n");
                json.append("  \"maxInheritanceDepth\": ").append(metrics.getMaxInheritanceDepth()).append(",\n");
                json.append("  \"avgInheritanceDepth\": ").append(metrics.getAvgInheritanceDepth()).append(",\n");
                json.append("  \"totalAbcMetric\": ").append(metrics.getTotalAbcMetric()).append(",\n");
                json.append("  \"avgOverriddenMethods\": ").append(metrics.getAvgOverriddenMethods()).append(",\n");
                json.append("  \"avgFieldsPerClass\": ").append(metrics.getAvgFieldsPerClass()).append("\n");
                json.append("}");

                return json.toString();
        }
}
