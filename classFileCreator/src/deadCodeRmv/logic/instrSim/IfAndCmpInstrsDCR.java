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

import java.util.*;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.logic.constants.JavaInstructionsOpcodes;
import takatuka.verifier.dataObjs.*;
import takatuka.verifier.dataObjs.attribute.*;
import takatuka.verifier.logic.DFA.*;

/**
 *
 *
 * @author Faisal Aslam
 */
public class IfAndCmpInstrsDCR extends IfAndCmpInstrs {

    private static final IfAndCmpInstrsDCR myObj = new IfAndCmpInstrsDCR();

    public static IfAndCmpInstrs getInstanceOf(Vector<Long> nextPossibleInstructionsIds_,
            OperandStack stack_, MethodInfo currentMethod_) {
        init(nextPossibleInstructionsIds_, stack_, currentMethod_);
        return myObj;
    }

    /**
     * It is called by: 
     * 
     * IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE, IFNONNULL, IFNULL
     * 
     * Sometime it has two parameters and sometime one. 
     * 
     * You could use nextPossibleInstructionsIds to change what should be
     * the next instruction to execute. -1 means the very next instruction
     * The super function adds two next instruction to execute. You might
     * need to change it based on the value.
     * 
     * 
     * @param inst
     * @param isReference
     * @param ifCmd 
     */
    protected void ifCmdInstruction(VerificationInstruction inst,
            boolean isReference, boolean ifCmd) {

        if (inst.getOpCode() == JavaInstructionsOpcodes.IFNE) {
            System.out.println("In IFNE Code");
            /**
             * save the top of the stack for the future use
             */
            Type type = stack.peep();
            /**
             * Now do the instruction simulation
             * and bytecode verification. After instruction simulation top
             * of the stack will be changed. No worries as we have already saved it above
             * for our later use.
             */
            super.ifCmdInstruction(inst, isReference, ifCmd);
             nextPossibleInstructionsIds.clear();
            if (type.value != null && ((Integer) type.value) != 0) {
                
                nextPossibleInstructionsIds.add(this.getTargetInstrIds(inst).elementAt(0));
            } else if (type.value != null && ((Integer) type.value) == 0) {
                
                nextPossibleInstructionsIds.add(new Long(-1));
            } else {
                
                nextPossibleInstructionsIds.add(this.getTargetInstrIds(inst).elementAt(0));
                nextPossibleInstructionsIds.add(new Long(-1));
            }
        }
        
        /**
         *  add you code here students for IFEQ
         */
        
        //IFEQ code
        if (inst.getOpCode() == JavaInstructionsOpcodes.IFEQ) {
            System.out.println("In IFEQ Code");
            /**
             * save the top of the stack for the future use
             */
            Type type = stack.peep();
            /**
             * Now do the instruction simulation
             * and bytecode verification. After instruction simulation top
             * of the stack will be changed. No worries as we have already saved it above
             * for our later use.
             */
            super.ifCmdInstruction(inst, isReference, ifCmd);
             nextPossibleInstructionsIds.clear();
            if (type.value != null && ((Integer) type.value) == 0) {
                
                nextPossibleInstructionsIds.add(this.getTargetInstrIds(inst).elementAt(0));
            } else if (type.value != null && ((Integer) type.value) != 0) {
                nextPossibleInstructionsIds.add(new Long(-1));
            } else {
                
                nextPossibleInstructionsIds.add(this.getTargetInstrIds(inst).elementAt(0));
                nextPossibleInstructionsIds.add(new Long(-1));
            }
        }
            

    }
}
