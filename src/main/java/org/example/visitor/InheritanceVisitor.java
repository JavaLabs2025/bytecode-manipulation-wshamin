package org.example.visitor;

import org.example.model.ClassInfo;
import org.objectweb.asm.ClassVisitor;

import java.util.Map;

import static org.objectweb.asm.Opcodes.ASM9;

public class InheritanceVisitor extends ClassVisitor {
    private final Map<String, ClassInfo> classInfoMap;

    public InheritanceVisitor(Map<String, ClassInfo> classInfoMap) {
        super(ASM9);
        this.classInfoMap = classInfoMap;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        ClassInfo classInfo = classInfoMap.computeIfAbsent(name, k -> new ClassInfo(name, superName));

        if (classInfo.getSuperName() == null && superName != null) {
            classInfoMap.put(name, new ClassInfo(name, superName));
        }

        super.visit(version, access, name, signature, superName, interfaces);
    }

    public int calculateInheritanceDepth(String className) {
        if (className == null || className.equals("java/lang/Object")) {
            return 0;
        }

        ClassInfo classInfo = classInfoMap.get(className);

        if (classInfo != null && classInfo.getInheritanceDepth() != -1) {
            return classInfo.getInheritanceDepth();
        }

        if (classInfo == null) {
            try {
                String javaClassName = className.replace('/', '.');
                Class<?> clazz = Class.forName(javaClassName);
                Class<?> superClass = clazz.getSuperclass();

                if (superClass == null) {
                    return 0;
                }

                String superClassName = superClass.getName().replace('.', '/');
                int depth = 1 + calculateInheritanceDepth(superClassName);
                return depth;
            } catch (ClassNotFoundException e) {
                return 1;
            }
        }

        String superName = classInfo.getSuperName();
        int depth = 1 + calculateInheritanceDepth(superName);

        classInfo.setInheritanceDepth(depth);

        return depth;
    }
}
