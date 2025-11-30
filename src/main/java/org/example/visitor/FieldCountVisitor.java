package org.example.visitor;

import org.example.model.ClassInfo;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

import static org.objectweb.asm.Opcodes.ASM9;

public class FieldCountVisitor extends ClassVisitor {
    private final ClassInfo classInfo;

    public FieldCountVisitor(ClassInfo classInfo) {
        super(ASM9);
        this.classInfo = classInfo;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        classInfo.setFieldCount(classInfo.getFieldCount() + 1);
        return super.visitField(access, name, descriptor, signature, value);
    }
}
