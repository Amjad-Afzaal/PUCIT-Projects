/*
 * Copyright 2012 Christian Schindelhauer, Peter Thiemann, Faisal Aslam, Luminous Fennell and Gidon Ernst.
 * All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License version 3 for more details (a copy is
 * included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU General Public License
 * version 3 along with this work; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 *
 * Please contact Faisal Aslam
 * (faisal.aslam AT gmail.com)
 * if you need additional information or have any questions.
 */
package takatuka.verifier.logic.DFA;

import java.util.*;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.logic.constants.*;
import takatuka.verifier.dataObjs.*;
import takatuka.verifier.dataObjs.attribute.*;
import takatuka.verifier.logic.*;
import takatuka.verifier.logic.exception.*;
import takatuka.verifier.logic.factory.*;

/**
 * <p>Title: </p>
 * <p>Description:
 * To verifiy load and store related instructions.
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class LoadAndStoreInstrs {

    protected static OperandStack stack = null;
    protected static LocalVariables localVar = null;
    protected static MethodInfo currentMethod = null;
    protected static Vector methodCallingParameters = null;
    private static final LoadAndStoreInstrs myObj = new LoadAndStoreInstrs();

    /**
     * constructor is private
     */
    protected LoadAndStoreInstrs() {
    }

    protected static void init(Frame frame, MethodInfo currentMethod_,
            Vector methodCallingParameters_) {
        stack = frame.getOperandStack();
        localVar = frame.getLocalVariables();
        currentMethod = currentMethod_;
        methodCallingParameters = methodCallingParameters_;
    }

    public static LoadAndStoreInstrs getInstanceOf(Frame frame,
            MethodInfo currentMethod,
            Vector methodCallingParameters) {
        init(frame, currentMethod, methodCallingParameters);
        return myObj;
    }

    protected void loadArrayExecute(VerificationInstruction inst) {
        boolean isReference = false;
        int opcode = inst.getOpCode();
        int type = -1;
        if (opcode == JavaInstructionsOpcodes.AALOAD) {
            isReference = true;
        } else if (opcode == JavaInstructionsOpcodes.BALOAD) {
            type = Type.BYTE;
        } else if (opcode == JavaInstructionsOpcodes.SALOAD) {
            type = Type.SHORT;
        } else if (opcode == JavaInstructionsOpcodes.CALOAD) {
            type = Type.CHAR;
        } else if (opcode == JavaInstructionsOpcodes.IALOAD) {
            type = Type.INTEGER;
        } else if (opcode == JavaInstructionsOpcodes.DALOAD) {
            type = Type.DOUBLE;
        } else if (opcode == JavaInstructionsOpcodes.FALOAD) {
            type = Type.FLOAT;
        } else if (opcode == JavaInstructionsOpcodes.LALOAD) {
            type = Type.LONG;
        } else {
            throw new VerifyErrorExt(Messages.INVALID_BYTECODE + inst);
        }
        loadArrayInstruction(inst, type, isReference, inst);
    }

    protected void loadVarExecute(VerificationInstruction inst) {
        int opcode = inst.getOpCode();
        boolean isReference = false;
        int index = -1;
        int type = 1;
        if (opcode == JavaInstructionsOpcodes.ILOAD
                || opcode == JavaInstructionsOpcodes.ILOAD_0
                || opcode == JavaInstructionsOpcodes.ILOAD_1
                || opcode == JavaInstructionsOpcodes.ILOAD_2
                || opcode == JavaInstructionsOpcodes.ILOAD_3) {
            type = Type.INTEGER;
        } else if (opcode == JavaInstructionsOpcodes.FLOAD
                || opcode == JavaInstructionsOpcodes.FLOAD_0
                || opcode == JavaInstructionsOpcodes.FLOAD_1
                || opcode == JavaInstructionsOpcodes.FLOAD_2
                || opcode == JavaInstructionsOpcodes.FLOAD_3) {
            type = Type.FLOAT;
        } else if (opcode == JavaInstructionsOpcodes.LLOAD
                || opcode == JavaInstructionsOpcodes.LLOAD_0
                || opcode == JavaInstructionsOpcodes.LLOAD_1
                || opcode == JavaInstructionsOpcodes.LLOAD_2
                || opcode == JavaInstructionsOpcodes.LLOAD_3) {
            type = Type.LONG;
        } else if (opcode == JavaInstructionsOpcodes.DLOAD
                || opcode == JavaInstructionsOpcodes.DLOAD_0
                || opcode == JavaInstructionsOpcodes.DLOAD_1
                || opcode == JavaInstructionsOpcodes.DLOAD_2
                || opcode == JavaInstructionsOpcodes.DLOAD_3) {
            type = Type.DOUBLE;
        } else if (opcode == JavaInstructionsOpcodes.ALOAD
                || opcode == JavaInstructionsOpcodes.ALOAD_0
                || opcode == JavaInstructionsOpcodes.ALOAD_1
                || opcode == JavaInstructionsOpcodes.ALOAD_2
                || opcode == JavaInstructionsOpcodes.ALOAD_3) { //reference load
            isReference = true;
        } else {
            throw new VerifyErrorExt(Messages.INVALID_BYTECODE + inst);
        }
        loadVarInstruction(inst, type, isReference, getLoadStoreInstrIndex(opcode));

    }

    private int getLoadStoreInstrIndex(int opcode) {
        int index = -1;

        if (opcode == JavaInstructionsOpcodes.ILOAD_0
                || opcode == JavaInstructionsOpcodes.LLOAD_0
                || opcode == JavaInstructionsOpcodes.DLOAD_0
                || opcode == JavaInstructionsOpcodes.ALOAD_0
                || opcode == JavaInstructionsOpcodes.ISTORE_0
                || opcode == JavaInstructionsOpcodes.LSTORE_0
                || opcode == JavaInstructionsOpcodes.DSTORE_0
                || opcode == JavaInstructionsOpcodes.ASTORE_0
                || opcode == JavaInstructionsOpcodes.FSTORE_0
                || opcode == JavaInstructionsOpcodes.FLOAD_0) {
            index = 0;
        } else if (opcode == JavaInstructionsOpcodes.ILOAD_1
                || opcode == JavaInstructionsOpcodes.LLOAD_1
                || opcode == JavaInstructionsOpcodes.DLOAD_1
                || opcode == JavaInstructionsOpcodes.ALOAD_1
                || opcode == JavaInstructionsOpcodes.ISTORE_1
                || opcode == JavaInstructionsOpcodes.LSTORE_1
                || opcode == JavaInstructionsOpcodes.DSTORE_1
                || opcode == JavaInstructionsOpcodes.ASTORE_1
                || opcode == JavaInstructionsOpcodes.FSTORE_1
                || opcode == JavaInstructionsOpcodes.FLOAD_1) {
            index = 1;
        } else if (opcode == JavaInstructionsOpcodes.ILOAD_2
                || opcode == JavaInstructionsOpcodes.LLOAD_2
                || opcode == JavaInstructionsOpcodes.DLOAD_2
                || opcode == JavaInstructionsOpcodes.ALOAD_2
                || opcode == JavaInstructionsOpcodes.ISTORE_2
                || opcode == JavaInstructionsOpcodes.LSTORE_2
                || opcode == JavaInstructionsOpcodes.DSTORE_2
                || opcode == JavaInstructionsOpcodes.ASTORE_2
                || opcode == JavaInstructionsOpcodes.FSTORE_2
                || opcode == JavaInstructionsOpcodes.FLOAD_2) {
            index = 2;
        } else if (opcode == JavaInstructionsOpcodes.ILOAD_3
                || opcode == JavaInstructionsOpcodes.LLOAD_3
                || opcode == JavaInstructionsOpcodes.DLOAD_3
                || opcode == JavaInstructionsOpcodes.ALOAD_3
                || opcode == JavaInstructionsOpcodes.ISTORE_3
                || opcode == JavaInstructionsOpcodes.LSTORE_3
                || opcode == JavaInstructionsOpcodes.DSTORE_3
                || opcode == JavaInstructionsOpcodes.ASTORE_3
                || opcode == JavaInstructionsOpcodes.FSTORE_3
                || opcode == JavaInstructionsOpcodes.FLOAD_3) {
            index = 3;
        }
        return index;
    }

    protected void storeArrayExecute(VerificationInstruction inst) {
        boolean isReference = false;
        int type = -1;
        int opcode = inst.getOpCode();
        if (opcode == JavaInstructionsOpcodes.AASTORE) {
            isReference = true;
        } else if (opcode == JavaInstructionsOpcodes.BASTORE) {
            type = Type.BYTE;
        } else if (opcode == JavaInstructionsOpcodes.CASTORE) {
            type = Type.CHAR;
        } else if (opcode == JavaInstructionsOpcodes.SALOAD) {
            type = Type.SHORT;
        } else if (opcode == JavaInstructionsOpcodes.IASTORE) {
            type = Type.INTEGER;
        } else if (opcode == JavaInstructionsOpcodes.DASTORE) {
            type = Type.DOUBLE;
        } else if (opcode == JavaInstructionsOpcodes.FASTORE) {
            type = Type.FLOAT;
        } else if (opcode == JavaInstructionsOpcodes.LASTORE) {
            type = Type.LONG;
        } else if (opcode == JavaInstructionsOpcodes.SASTORE) {
            type = Type.SHORT;
        } else {
            throw new VerifyErrorExt(Messages.INVALID_BYTECODE + inst);
        }
        storeArrayInstruction(inst, type, isReference, inst);
    }

    protected void storeVarExecute(VerificationInstruction inst) {
        int opcode = inst.getOpCode();
        boolean isReference = false;
        int type = 1;
        if (opcode == JavaInstructionsOpcodes.ASTORE
                || opcode == JavaInstructionsOpcodes.ASTORE_0
                || opcode == JavaInstructionsOpcodes.ASTORE_1
                || opcode == JavaInstructionsOpcodes.ASTORE_2
                || opcode == JavaInstructionsOpcodes.ASTORE_3) { //reference store
            isReference = true;
        } else if (opcode == JavaInstructionsOpcodes.LSTORE
                || opcode == JavaInstructionsOpcodes.LSTORE_0
                || opcode == JavaInstructionsOpcodes.LSTORE_1
                || opcode == JavaInstructionsOpcodes.LSTORE_2
                || opcode == JavaInstructionsOpcodes.LSTORE_3) {
            type = Type.LONG;
        } else if (opcode == JavaInstructionsOpcodes.ISTORE
                || opcode == JavaInstructionsOpcodes.ISTORE_0
                || opcode == JavaInstructionsOpcodes.ISTORE_1
                || opcode == JavaInstructionsOpcodes.ISTORE_2
                || opcode == JavaInstructionsOpcodes.ISTORE_3) {
            type = Type.INTEGER;
        } else if (opcode == JavaInstructionsOpcodes.LSTORE
                || opcode == JavaInstructionsOpcodes.LSTORE_0
                || opcode == JavaInstructionsOpcodes.LSTORE_1
                || opcode == JavaInstructionsOpcodes.LSTORE_2
                || opcode == JavaInstructionsOpcodes.LSTORE_3) {
            type = Type.LONG;
        } else if (opcode == JavaInstructionsOpcodes.FSTORE
                || opcode == JavaInstructionsOpcodes.FSTORE_0
                || opcode == JavaInstructionsOpcodes.FSTORE_1
                || opcode == JavaInstructionsOpcodes.FSTORE_2
                || opcode == JavaInstructionsOpcodes.FSTORE_3) {
            type = Type.FLOAT;
        } else if (opcode == JavaInstructionsOpcodes.DSTORE
                || opcode == JavaInstructionsOpcodes.DSTORE_0
                || opcode == JavaInstructionsOpcodes.DSTORE_1
                || opcode == JavaInstructionsOpcodes.DSTORE_2
                || opcode == JavaInstructionsOpcodes.DSTORE_3) {
            type = Type.DOUBLE;
        } else {
            throw new VerifyErrorExt(Messages.INVALID_BYTECODE + inst);
        }
        storeVarInstruction(inst, type, isReference, getLoadStoreInstrIndex(opcode));
    }

    /**
     *   blockSize, int (i) ==> blockSize
     */
    protected void loadArrayInstruction(VerificationInstruction inst, int type,
            boolean isReference, VerificationInstruction instr) {
        VerificationFrameFactory frameFactory = FrameFactoryPlaceHolder.getInstanceOf().getFactory();
        Type index = stack.pop();
        Type arrayref = stack.pop();
        stack.push(frameFactory.createType(type, isReference, 0));
    }

    /**
     * blockSize =: VGet(operand)
     *     ==> blockSize
     */
    protected void loadVarInstruction(VerificationInstruction inst, int type,
            boolean isReference, int index) {
        VerificationFrameFactory frameFactory = FrameFactoryPlaceHolder.getInstanceOf().getFactory();
        Type loadType = null;
        if (index == -1) {
            index = inst.getOperandsData().intValueUnsigned();
        }
        Type typeShouldBe = frameFactory.createType(type, isReference, -1);
        loadType = localVar.get(index);
        if (!Type.isCompatableTypes(typeShouldBe, loadType)) {
            Type.isCompatableTypes(typeShouldBe, loadType);
            throw new VerifyErrorExt(Messages.STACK_INVALID + " " + inst);
        }
        stack.push((Type) loadType.clone());
    }

    protected void storeVarInstruction(VerificationInstruction inst, int type,
            boolean isReference, int index) {
        VerificationFrameFactory frameFactory = FrameFactoryPlaceHolder.getInstanceOf().getFactory();
        if (index == -1) {
            index = inst.getOperandsData().intValueUnsigned();
        }

        Type storeType = stack.pop();
        Type shouldBeType = frameFactory.createType(type, isReference, -1);
        if (!Type.isCompatableTypes(storeType, shouldBeType)) {
            throw new VerifyErrorExt(Messages.STACK_INVALID + ", " + inst + ", "
                    + storeType + ", " + shouldBeType);
        }
        localVar.set(index, storeType);
    }

    /**
     *    ... ,arrayref, index, value ==> ...
     *    VSet(value (index)
     */
    protected void storeArrayInstruction(VerificationInstruction inst, int type,
            boolean isReference, VerificationInstruction instr) {

        //pop values
        Type value = stack.pop();
        //should be int compatable
        Type index = stack.pop();
        Type arrayref = stack.pop();

        if (!index.isIntOrShortOrByteOrBooleanOrCharType()
                || (value.isReference() != isReference)
                || (!value.isOriginalFourByteLiteralValue()
                && value.getBlocks() != Type.getBlocks(type, isReference))) {
            throw new VerifyErrorExt(Messages.STACK_INVALID);
        }
    }
}
