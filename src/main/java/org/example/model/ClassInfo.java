package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {
    private final String name;
    private final String superName;
    private final List<MethodInfo> methods;
    private int fieldCount;
    private int inheritanceDepth;
    private int overriddenMethodsCount;

    public ClassInfo(String name, String superName) {
        this.name = name;
        this.superName = superName;
        this.methods = new ArrayList<>();
        this.fieldCount = 0;
        this.inheritanceDepth = -1;
        this.overriddenMethodsCount = 0;
    }

    public String getName() {
        return name;
    }

    public String getSuperName() {
        return superName;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public void addMethod(MethodInfo method) {
        this.methods.add(method);
    }

    public int getFieldCount() {
        return fieldCount;
    }

    public void setFieldCount(int fieldCount) {
        this.fieldCount = fieldCount;
    }

    public int getInheritanceDepth() {
        return inheritanceDepth;
    }

    public void setInheritanceDepth(int inheritanceDepth) {
        this.inheritanceDepth = inheritanceDepth;
    }

    public int getOverriddenMethodsCount() {
        return overriddenMethodsCount;
    }

    public void setOverriddenMethodsCount(int overriddenMethodsCount) {
        this.overriddenMethodsCount = overriddenMethodsCount;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "name='" + name + '\'' +
                ", superName='" + superName + '\'' +
                ", methods=" + methods.size() +
                ", fieldCount=" + fieldCount +
                ", inheritanceDepth=" + inheritanceDepth +
                '}';
    }
}
