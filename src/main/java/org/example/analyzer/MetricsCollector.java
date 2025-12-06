package org.example.analyzer;

import org.example.model.ClassInfo;
import org.example.model.Metrics;
import org.example.model.MethodInfo;
import org.example.visitor.*;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MetricsCollector {
    private final Map<String, ClassInfo> classInfoMap;
    private final InheritanceVisitor inheritanceVisitor;

    public MetricsCollector() {
        this.classInfoMap = new HashMap<>();
        this.inheritanceVisitor = new InheritanceVisitor(classInfoMap);
    }

    public void processClass(InputStream classStream) throws IOException {
        ClassReader classReader = new ClassReader(classStream);
        String className = classReader.getClassName();

        ClassInfo classInfo = classInfoMap.computeIfAbsent(className,
                k -> new ClassInfo(className, classReader.getSuperName()));

        ClassMetricsVisitor metricsVisitor = new ClassMetricsVisitor(classInfo, classInfoMap);
        classReader.accept(metricsVisitor, 0);

        classInfo.setOverriddenMethodsCount(metricsVisitor.getOverriddenMethodsCount());
    }

    public Metrics calculateMetrics() {
        Metrics metrics = new Metrics();

        if (classInfoMap.isEmpty()) {
            return metrics;
        }

        int totalInheritanceDepth = 0;
        int maxInheritanceDepth = 0;
        int totalAbcMetric = 0;
        int totalFields = 0;
        int totalOverriddenMethods = 0;
        int classCount = classInfoMap.size();

        for (ClassInfo classInfo : classInfoMap.values()) {
            int depth = inheritanceVisitor.calculateInheritanceDepth(classInfo.getName());
            classInfo.setInheritanceDepth(depth);
            totalInheritanceDepth += depth;
            maxInheritanceDepth = Math.max(maxInheritanceDepth, depth);

            for (MethodInfo method : classInfo.getMethods()) {
                totalAbcMetric += method.getAssignmentCount();
            }

            totalFields += classInfo.getFieldCount();

            totalOverriddenMethods += classInfo.getOverriddenMethodsCount();
        }

        metrics.setMaxInheritanceDepth(maxInheritanceDepth);
        metrics.setAvgInheritanceDepth((double) totalInheritanceDepth / classCount);
        metrics.setTotalAbcMetric(totalAbcMetric);
        metrics.setAvgOverriddenMethods((double) totalOverriddenMethods / classCount);
        metrics.setAvgFieldsPerClass((double) totalFields / classCount);

        return metrics;
    }

    public Map<String, ClassInfo> getClassInfoMap() {
        return classInfoMap;
    }
}
