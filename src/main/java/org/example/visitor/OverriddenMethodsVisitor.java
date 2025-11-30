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

/**
 * Visitor для подсчета переопределенных методов в классе.
 */
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
        // Пропускаем конструкторы и статические инициализаторы
        if (!name.equals("<init>") && !name.equals("<clinit>")) {
            String methodSignature = name + descriptor;

            // Проверяем, переопределяет ли этот метод метод суперкласса
            if (isMethodOverridden(methodSignature, classInfo.getSuperName())) {
                overriddenMethodsCount++;
            }
        }

        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    /**
     * Проверяет, переопределяет ли метод с заданной сигнатурой метод из
     * суперкласса.
     * 
     * @param methodSignature сигнатура метода (имя + дескриптор)
     * @param superClassName  имя суперкласса
     * @return true, если метод переопределяет метод суперкласса
     */
    private boolean isMethodOverridden(String methodSignature, String superClassName) {
        if (superClassName == null || superClassName.equals("java/lang/Object")) {
            // Для Object проверяем только стандартные методы
            return isObjectMethod(methodSignature);
        }

        // Ищем суперкласс в нашем JAR
        ClassInfo superClassInfo = classInfoMap.get(superClassName);

        if (superClassInfo != null) {
            // Проверяем методы суперкласса из нашего JAR
            Set<String> superMethodSignatures = getSuperClassMethodSignatures(superClassInfo);
            if (superMethodSignatures.contains(methodSignature)) {
                return true;
            }

            // Рекурсивно проверяем родителя суперкласса
            return isMethodOverridden(methodSignature, superClassInfo.getSuperName());
        } else {
            // Пытаемся загрузить класс через ClassLoader
            try {
                Set<String> externalSuperMethods = getExternalClassMethods(superClassName);
                return externalSuperMethods.contains(methodSignature);
            } catch (Exception e) {
                // Не удалось загрузить класс, пропускаем
                return false;
            }
        }
    }

    /**
     * Проверяет, является ли метод одним из стандартных методов Object.
     */
    private boolean isObjectMethod(String methodSignature) {
        return methodSignature.equals("toString()Ljava/lang/String;") ||
                methodSignature.equals("equals(Ljava/lang/Object;)Z") ||
                methodSignature.equals("hashCode()I") ||
                methodSignature.equals("clone()Ljava/lang/Object;") ||
                methodSignature.equals("finalize()V");
    }

    /**
     * Получает сигнатуры всех методов суперкласса из нашего JAR.
     */
    private Set<String> getSuperClassMethodSignatures(ClassInfo superClassInfo) {
        Set<String> signatures = new HashSet<>();
        for (MethodInfo method : superClassInfo.getMethods()) {
            signatures.add(method.getSignature());
        }
        return signatures;
    }

    /**
     * Получает сигнатуры методов внешнего класса через ClassReader.
     */
    private Set<String> getExternalClassMethods(String className) throws IOException {
        Set<String> methodSignatures = new HashSet<>();

        // Пытаемся загрузить класс как ресурс
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

    /**
     * Возвращает количество переопределенных методов.
     */
    public int getOverriddenMethodsCount() {
        return overriddenMethodsCount;
    }
}
