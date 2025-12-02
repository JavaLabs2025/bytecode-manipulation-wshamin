package org.example.visitor;

import org.example.model.ClassInfo;
import org.example.model.MethodInfo;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.objectweb.asm.Opcodes.ASM9;

public class OverriddenMethodsVisitor extends ClassVisitor {
    private final ClassInfo classInfo;
    private final Map<String, ClassInfo> classInfoMap;
    private int overriddenMethodsCount;

    public OverriddenMethodsVisitor(ClassInfo classInfo, Map<String, ClassInfo> classInfoMap) {
        super(ASM9);
        this.classInfo = classInfo;
        this.classInfoMap = classInfoMap;
        this.overriddenMethodsCount = 0;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
            String[] exceptions) {
        if (!name.equals("<init>") && !name.equals("<clinit>")) {
            String methodSignature = name + descriptor;

            if (isMethodOverridden(methodSignature, classInfo.getSuperName())) {
                overriddenMethodsCount++;
            }
        }

        return super.visitMethod(access, name, descriptor, signature, exceptions);
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
