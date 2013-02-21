/*
 * Copyright 2012 Faisal Aslam.
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
 * Please contact Faisal Aslam (faisal.aslam AT gmail.com)
 * if you need additional information or have any questions.
 */
package deadCodeRmv.logic.instrSim;

import deadCodeRmv.dataObjs.InstructionDCR;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.logic.constants.JavaInstructionsOpcodes;
import takatuka.verifier.dataObjs.*;
import takatuka.verifier.dataObjs.attribute.VerificationInstruction;
import takatuka.verifier.logic.DFA.*;
import takatuka.verifier.logic.Messages;
import takatuka.verifier.logic.exception.VerifyErrorExt;
import takatuka.verifier.logic.factory.FrameFactoryPlaceHolder;
import takatuka.verifier.logic.factory.VerificationFrameFactory;

/**
 *
 *
 * @author Faisal Aslam
 */
public class MathInstrsDCR extends MathInstrs {

    private static final MathInstrsDCR myObj = new MathInstrsDCR();

    public static MathInstrs getInstanceOf(OperandStack stack_,
            MethodInfo currentMethod_, int currentPC) {
        init(stack_, currentMethod_, currentPC);
        return myObj;
    }

    protected void mathInstruction(OperandStack stack, int val1Type, int val2Type,
            int resultType, int opcode, VerificationInstruction currentInstr) {
        /**
         * add you code here STUDENT:
         */
        VerificationFrameFactory frameFactory = FrameFactoryPlaceHolder.getInstanceOf().getFactory();
        Type val1 = stack.pop();
        Type val2 = stack.pop();
        Type val1ShouldBe = frameFactory.createType(val1Type);
        Type val2ShouldBe = frameFactory.createType(val2Type);

        if (!Type.isCompatableTypes(val1, val1ShouldBe) ||
                !Type.isCompatableTypes(val2, val2ShouldBe)) {
            /**
             * We check here the block sizes instead of exact type as TakaTuka specific load and store
             * instructions do not differentiate between them.
             */
            throw new VerifyErrorExt(Messages.STACK_INVALID);
        }
        if(val1.value != null && val2.value != null && currentInstr.getOpCode() == JavaInstructionsOpcodes.IADD)
        {
            resultType = (Integer) (val1.value) + (Integer) (val2.value);
            
        }
        if(val1.value != null && val2.value != null && currentInstr.getOpCode() == JavaInstructionsOpcodes.IMUL)
        {
            resultType = (Integer) (val1.value) * (Integer) (val2.value);
            
        }
        
        Type toSet = frameFactory.createType(resultType);
        toSet.value = resultType;
        stack.push(toSet);
    }
}
