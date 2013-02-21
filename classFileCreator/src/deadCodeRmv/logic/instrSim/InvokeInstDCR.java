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

import deadCodeRmv.StartMeDCR;
import deadCodeRmv.UsefulFun;
import deadCodeRmv.dataObjs.ClassFileDCR;
import deadCodeRmv.dataObjs.InstructionDCR;
import deadCodeRmv.dataObjs.MethodInfoDCR;
import takatuka.verifier.logic.DFA.InvokeInstrs;
import java.util.*;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.dataObjs.constantPool.ReferenceInfo;
import takatuka.classreader.logic.constants.JavaInstructionsOpcodes;
import takatuka.verifier.dataObjs.*;
import takatuka.verifier.dataObjs.attribute.VerificationInstruction;

/**
 *
 *
 * @author Faisal Aslam
 */
public class InvokeInstDCR extends InvokeInstrs {

    private static final InvokeInstDCR myObj = new InvokeInstDCR();

    public static InvokeInstrs getInstanceOf(Frame frame,
            MethodInfo currentMethod,
            Vector methodCallingParameters,
            Vector nextPossibleInstructionsIds) {
        init(frame, currentMethod,
                methodCallingParameters, nextPossibleInstructionsIds);
        return myObj;
    }

    /**
     * You need to record all the possible method that this instruction 
     * can invoke using UsefulFun.getMethodInfoFromInvokeInstr and add
     * them to be analyzed next.
     * 
     * Eventually subset of those function will be kept given that many
     * class files with function analyzed will be deleted.
     * 
     * @param methodDesc
     * @param opCode
     * @param ref
     * @param currentInstr
     * @param methodNameForDebug 
     */
    protected void invokeInstruction(String methodDesc, int opCode, ReferenceInfo ref,
            VerificationInstruction currentInstr,
            String methodNameForDebug) {
        super.invokeInstruction(methodDesc, opCode, ref, currentInstr, methodNameForDebug);
        /**
         * Add you code here STUDENT:
         */
        InstructionDCR i = (InstructionDCR) currentInstr;
        if(i.getOpCode() == JavaInstructionsOpcodes.INVOKEVIRTUAL || i.getOpCode() == JavaInstructionsOpcodes.INVOKEINTERFACE
                || i.getOpCode() == JavaInstructionsOpcodes.INVOKESPECIAL)
        {
            Collection<MethodInfoDCR> allMethodsFromInvokeInstr = UsefulFun.getMethodInfoFromInvokeInstr(currentInstr);
            Iterator<MethodInfoDCR> iterate = allMethodsFromInvokeInstr.iterator();
            while(iterate.hasNext())    StartMeDCR.addFunctionTotoBeAnalyzed(iterate.next());
            
        }
        
        else if(i.getOpCode() == JavaInstructionsOpcodes.INVOKESTATIC)
        {
            Collection<MethodInfoDCR> allMethodsFromInvokeInstr = UsefulFun.getMethodInfoFromInvokeInstr(currentInstr);
            StartMeDCR.addFunctionTotoBeAnalyzed((allMethodsFromInvokeInstr.iterator()).next());
            ((ClassFileDCR)UsefulFun.getClassFileOfInvokeInstr(currentInstr)).setShouldKeep(true);
            Collection<ClassFileDCR> allSuperClasses1 = UsefulFun.getAllSuperClasses1(((ClassFileDCR)UsefulFun.getClassFileOfInvokeInstr(currentInstr)));
            Iterator<ClassFileDCR> iterate = allSuperClasses1.iterator();
            while(iterate.hasNext())    iterate.next().setShouldKeep(true);
            
        }
    }
}
