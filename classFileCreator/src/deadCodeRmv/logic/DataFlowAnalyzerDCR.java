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
package deadCodeRmv.logic;

import deadCodeRmv.UsefulFun;
import deadCodeRmv.dataObjs.InstructionDCR;
import deadCodeRmv.dataObjs.MethodInfoDCR;
import frameSizeCalc.logic.*;
import takatuka.classreader.dataObjs.*;
import takatuka.verifier.dataObjs.*;
import takatuka.verifier.logic.DFA.*;
import java.util.*;
import takatuka.verifier.dataObjs.attribute.*;

/**
 *
 *
 * @author Faisal Aslam
 */
public class DataFlowAnalyzerDCR extends DataFlowAnalyzerFSC {

    private static final DataFlowAnalyzerDCR dfa = new DataFlowAnalyzerDCR();

    public static DataFlowAnalyzer getInstanceOf() {
        return dfa;
    }

    @Override
    protected void workAfterMethodExecuted(MethodInfo method,
            Frame myFrame, OperandStack callerStack,
            HashSet<Type> returnTypes,
            Vector methodCallingParameters) throws Exception {
        super.workAfterMethodExecuted(method, myFrame, callerStack, returnTypes, methodCallingParameters);
        String methodName = UsefulFun.getMethodName((MethodInfoDCR) method);
        /**
         * Keep the method that is analyzed
         */
        ((MethodInfoDCR) method).setShouldKeep(true);
    }

    @Override
    protected void workAfterInstructionExecuted(MethodInfo method, Frame myFrame,
            Vector nextInstrId,
            VerificationInstruction currentInstr,
            Vector allInstruction, Vector methodCallingParamerters) throws Exception {
        super.workAfterInstructionExecuted(method, myFrame, nextInstrId, currentInstr,
                allInstruction, methodCallingParamerters);
        /**
         * Keep the instruction that is analyzed.
         */
        ((InstructionDCR) currentInstr).setShouldKeep(true);

    }
}
