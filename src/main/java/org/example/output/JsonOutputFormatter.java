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
                json.append(String.format("  \"maxInheritanceDepth\": %d,\n",
                                metrics.getMaxInheritanceDepth()));
                json.append(String.format("  \"avgInheritanceDepth\": %.2f,\n",
                                metrics.getAvgInheritanceDepth()));
                json.append(String.format("  \"totalAbcMetric\": %d,\n",
                                metrics.getTotalAbcMetric()));
                json.append(String.format("  \"avgOverriddenMethods\": %.2f,\n",
                                metrics.getAvgOverriddenMethods()));
                json.append(String.format("  \"avgFieldsPerClass\": %.2f\n",
                                metrics.getAvgFieldsPerClass()));
                json.append("}");

                return json.toString();
        }
}
