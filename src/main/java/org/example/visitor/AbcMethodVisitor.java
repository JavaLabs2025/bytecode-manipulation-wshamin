package org.example.visitor;

import org.example.model.MethodInfo;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class AbcMethodVisitor extends MethodVisitor {
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
