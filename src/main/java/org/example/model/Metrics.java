package org.example.model;

public class Metrics {
    private int maxInheritanceDepth;
    private double avgInheritanceDepth;
    private int totalAbcMetric;
    private double avgOverriddenMethods;
    private double avgFieldsPerClass;

    public Metrics() {
        this.maxInheritanceDepth = 0;
        this.avgInheritanceDepth = 0.0;
        this.totalAbcMetric = 0;
        this.avgOverriddenMethods = 0.0;
        this.avgFieldsPerClass = 0.0;
    }

    public int getMaxInheritanceDepth() {
        return maxInheritanceDepth;
    }

    public void setMaxInheritanceDepth(int maxInheritanceDepth) {
        this.maxInheritanceDepth = maxInheritanceDepth;
    }

    public double getAvgInheritanceDepth() {
        return avgInheritanceDepth;
    }

    public void setAvgInheritanceDepth(double avgInheritanceDepth) {
        this.avgInheritanceDepth = avgInheritanceDepth;
    }

    public int getTotalAbcMetric() {
        return totalAbcMetric;
    }

    public void setTotalAbcMetric(int totalAbcMetric) {
        this.totalAbcMetric = totalAbcMetric;
    }

    public double getAvgOverriddenMethods() {
        return avgOverriddenMethods;
    }

    public void setAvgOverriddenMethods(double avgOverriddenMethods) {
        this.avgOverriddenMethods = avgOverriddenMethods;
    }

    public double getAvgFieldsPerClass() {
        return avgFieldsPerClass;
    }

    public void setAvgFieldsPerClass(double avgFieldsPerClass) {
        this.avgFieldsPerClass = avgFieldsPerClass;
    }

    @Override
    public String toString() {
        return "Metrics{" +
                "maxInheritanceDepth=" + maxInheritanceDepth +
                ", avgInheritanceDepth=" + avgInheritanceDepth +
                ", totalAbcMetric=" + totalAbcMetric +
                ", avgOverriddenMethods=" + avgOverriddenMethods +
                ", avgFieldsPerClass=" + avgFieldsPerClass +
                '}';
    }
}
