package org.example.model;

public class MethodInfo {
    private final String name;
    private final String descriptor;
    private int assignmentCount;

    public MethodInfo(String name, String descriptor) {
        this.name = name;
        this.descriptor = descriptor;
        this.assignmentCount = 0;
    }

    public String getName() {
        return name;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public int getAssignmentCount() {
        return assignmentCount;
    }

    public void setAssignmentCount(int assignmentCount) {
        this.assignmentCount = assignmentCount;
    }

    public void incrementAssignmentCount() {
        this.assignmentCount++;
    }

    public String getSignature() {
        return name + descriptor;
    }

    @Override
    public String toString() {
        return "MethodInfo{" +
                "name='" + name + '\'' +
                ", descriptor='" + descriptor + '\'' +
                ", assignmentCount=" + assignmentCount +
                '}';
    }
}
