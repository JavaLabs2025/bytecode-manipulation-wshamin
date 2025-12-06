package org.example.visitor;

import org.example.model.ClassInfo;
import org.example.model.MethodInfo;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.objectweb.asm.Opcodes.*;

public class ClassMetricsVisitor extends ClassVisitor {
    private final ClassInfo classInfo;
    private final Map<String, ClassInfo> classInfoMap;
    private int overriddenMethodsCount;

    public ClassMetricsVisitor(ClassInfo classInfo, Map<String, ClassInfo> classInfoMap) {
        super(ASM9);
        this.classInfo = classInfo;
        this.classInfoMap = classInfoMap;
        this.overriddenMethodsCount = 0;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        ClassInfo existingClassInfo = classInfoMap.computeIfAbsent(name, k -> new ClassInfo(name, superName));
        if (existingClassInfo.getSuperName() == null && superName != null) {
            classInfoMap.put(name, new ClassInfo(name, superName));
        }

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        classInfo.setFieldCount(classInfo.getFieldCount() + 1);
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
            String[] exceptions) {
        MethodInfo methodInfo = new MethodInfo(name, descriptor);
        classInfo.addMethod(methodInfo);

        if (!name.equals("<init>") && !name.equals("<clinit>")) {
            String methodSignature = name + descriptor;
            if (isMethodOverridden(methodSignature, classInfo.getSuperName())) {
                overriddenMethodsCount++;
            }
        }

        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new AbcMethodVisitor(mv, methodInfo);
    }

    private boolean isMethodOverridden(String methodSignature, String superClassName) {
        if (superClassName == null || superClassName.equals("java/lang/Object")) {
            return isObjectMethod(methodSignature);
        }

        ClassInfo superClassInfo = classInfoMap.get(superClassName);

        if (superClassInfo != null) {
            Set<String> superMethodSignatures = getSuperClassMethodSignatures(superClassInfo);
            if (superMethodSignatures.contains(methodSignature)) {
                return true;
            }
            return isMethodOverridden(methodSignature, superClassInfo.getSuperName());
        } else {
            try {
                Set<String> externalSuperMethods = getExternalClassMethods(superClassName);
                return externalSuperMethods.contains(methodSignature);
            } catch (Exception e) {
                return false;
            }
        }
    }

    private boolean isObjectMethod(String methodSignature) {
        return methodSignature.equals("toString()Ljava/lang/String;") ||
                methodSignature.equals("equals(Ljava/lang/Object;)Z") ||
                methodSignature.equals("hashCode()I") ||
                methodSignature.equals("clone()Ljava/lang/Object;") ||
                methodSignature.equals("finalize()V");
    }

    private Set<String> getSuperClassMethodSignatures(ClassInfo superClassInfo) {
        Set<String> signatures = new HashSet<>();
        for (MethodInfo method : superClassInfo.getMethods()) {
            signatures.add(method.getSignature());
        }
        return signatures;
    }

    private Set<String> getExternalClassMethods(String className) throws IOException {
        Set<String> methodSignatures = new HashSet<>();

        String resourceName = className + ".class";
        InputStream classStream = ClassLoader.getSystemResourceAsStream(resourceName);

        if (classStream != null) {
            try {
                ClassReader cr = new ClassReader(classStream);
                cr.accept(new ClassVisitor(ASM9) {
                    @Override
                    public MethodVisitor visitMethod(int access, String name, String descriptor,
                            String signature, String[] exceptions) {
                        if (!name.equals("<init>") && !name.equals("<clinit>")) {
                            methodSignatures.add(name + descriptor);
                        }
                        return super.visitMethod(access, name, descriptor, signature, exceptions);
                    }
                }, ClassReader.SKIP_CODE);
            } finally {
                classStream.close();
            }
        }

        return methodSignatures;
    }

    public int getOverriddenMethodsCount() {
        return overriddenMethodsCount;
    }
}
