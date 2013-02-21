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
package frameSizeCalc.logic;

import frameSizeCalc.dataObjs.*;
import takatuka.classreader.dataObjs.*;
import takatuka.verifier.dataObjs.*;
import takatuka.verifier.logic.DFA.*;
import java.util.*;
import takatuka.util.Oracle;
import takatuka.verifier.dataObjs.attribute.*;

/**
 *
 *
 * @author Faisal Aslam
 */
public class DataFlowAnalyzerFSC extends DataFlowAnalyzer {

    private static final DataFlowAnalyzerFSC dfa = new DataFlowAnalyzerFSC();
    public static HashMap<String, FrameMaxInfo> maxStackPerMethod = new HashMap<String, FrameMaxInfo>();

    public static DataFlowAnalyzer getInstanceOf() {
        return dfa;
    }

    @Override
    protected void workBeforeMethodExecuted(MethodInfo method,
            Vector localVariables,
            OperandStack callerStack,
            VerificationInstruction lastInstrExecuted) {
        try {
            Oracle oracle = Oracle.getInstanceOf();
            String methodDesc = oracle.methodOrFieldDescription(method, method.getClassFile().getConstantPool());
            int parameterSize = ParameterSizeCalc.getParameterSize(methodDesc, method.getAccessFlags().isStatic());
            if (method.getCodeAtt().getMaxLocals().intValueSigned() < parameterSize) {
                method.getCodeAtt().setMaxLocals(parameterSize);
            }
        } catch (Exception d) {
            d.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    protected void workAfterMethodExecuted(MethodInfo method,
            Frame myFrame, OperandStack callerStack,
            HashSet<Type> returnTypes,
            Vector methodCallingParameters) throws Exception {
        super.workAfterMethodExecuted(method, myFrame, callerStack,
                returnTypes, methodCallingParameters);
               String key = methodKey(method);
        FrameMaxInfo stackMaxInfo = maxStackPerMethod.get(key);
        
        if (!method.getAccessFlags().isStatic() && stackMaxInfo.maxLocalsSize == 0) {
           stackMaxInfo.maxLocalsSize++;
        }
        method.getCodeAtt().setMaxStack(stackMaxInfo.maxStackSize);
        method.getCodeAtt().setMaxLocals(stackMaxInfo.maxLocalsSize);
    }

    @Override
    protected void workAfterInstructionExecuted(MethodInfo method, Frame myFrame,
            Vector nextInstrId,
            VerificationInstruction currentInstr,
            Vector allInstruction, Vector methodCallingParamerters) throws Exception {
        super.workAfterInstructionExecuted(method, myFrame, nextInstrId, currentInstr,
                allInstruction, methodCallingParamerters);
        OperandStackFSC opStk = (OperandStackFSC) myFrame.getOperandStack();
        LocalVariablesFSC lv = (LocalVariablesFSC) myFrame.getLocalVariables();
        int maxStack = opStk.getMaxSizeCaculated();
        int maxLocal = lv.getMaxSizeCaculated();
        String key = methodKey(method);
        FrameMaxInfo stackMaxInfo = maxStackPerMethod.get(key);
        if (stackMaxInfo == null) {
            stackMaxInfo = new FrameMaxInfo(method);
            maxStackPerMethod.put(key, stackMaxInfo);
        }
        if (stackMaxInfo.maxLocalsSize < maxLocal) {
            stackMaxInfo.maxLocalsSize = maxLocal;
        }
        if (stackMaxInfo.maxStackSize < maxStack) {
            stackMaxInfo.maxStackSize = maxStack;
        }

    }
}
