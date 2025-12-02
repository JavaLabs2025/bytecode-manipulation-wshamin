package org.example.visitor;

import org.example.model.ClassInfo;
import org.example.model.MethodInfo;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class AbcMetricVisitor extends ClassVisitor {
    private final ClassInfo classInfo;

    public AbcMetricVisitor(ClassInfo classInfo) {
        super(ASM9);
        this.classInfo = classInfo;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
            String[] exceptions) {
        MethodInfo methodInfo = new MethodInfo(name, descriptor);
        classInfo.addMethod(methodInfo);

        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new AbcMethodVisitor(mv, methodInfo);
    }

    private static class AbcMethodVisitor extends MethodVisitor {
        private final MethodInfo methodInfo;

        public AbcMethodVisitor(MethodVisitor methodVisitor, MethodInfo methodInfo) {
            super(ASM9, methodVisitor);
            this.methodInfo = methodInfo;
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            if (opcode == ISTORE || opcode == LSTORE || opcode == FSTORE ||
                    opcode == DSTORE || opcode == ASTORE) {
                methodInfo.incrementAssignmentCount();
            }
            super.visitVarInsn(opcode, var);
        }
    }
}
