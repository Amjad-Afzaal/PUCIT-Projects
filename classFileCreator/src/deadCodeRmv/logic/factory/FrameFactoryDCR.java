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
package deadCodeRmv.logic.factory;

import deadCodeRmv.logic.*;
import deadCodeRmv.dataObjs.*;
import deadCodeRmv.logic.instrSim.*;
import frameSizeCalc.logic.*;
import takatuka.verifier.dataObjs.*;
import takatuka.verifier.logic.DFA.*;
import takatuka.verifier.logic.factory.*;
import java.util.*;
import takatuka.classreader.dataObjs.MethodInfo;

/**
 *
 *
 * @author Faisal Aslam
 */
public class FrameFactoryDCR extends FrameFactoryFSC {

    private static final FrameFactoryFSC myObj = new FrameFactoryDCR();

    public static VerificationFrameFactory getInstanceOf() {
        return myObj;
    }

    @Override
    public OperandStack createOperandStack(int maxStack) {
        return new OperandStackDCR(maxStack);
    }

    @Override
    public LocalVariables createLocalVariables(int maxStack) {
        return new LocalVariablesDCR(maxStack);
    }

    @Override
    public DataFlowAnalyzer createDataFlowAnalyzer() {
        return DataFlowAnalyzerDCR.getInstanceOf();
    }

    @Override
    public BytecodeVerifier createBytecodeVerifier(Frame frame, MethodInfo currentMethod,
            Vector callingParameters) {
        return new BytecodeSimulator(frame, currentMethod, callingParameters);
    }

    @Override
    public Type createType() {
        return new TypeDCR();
    }

    @Override
    public Type createType(boolean isReference) {
        return new TypeDCR(isReference);
    }

    @Override
    public Type createType(int type) {
        return new TypeDCR(type);
    }

    @Override
    public Type createType(int type, boolean isReference, int newId) {
        return new TypeDCR(type, isReference, newId);
    }
    
    

    
    @Override
    public IfAndCmpInstrs createIfAndCmpInstrsInterpreter(Vector<Long> nextPossibleInstructionsIds,
            OperandStack stack, MethodInfo currentMethod) {
        return IfAndCmpInstrsDCR.getInstanceOf(nextPossibleInstructionsIds, stack, currentMethod);
    }


    
    @Override
    public InvokeInstrs createInvokeInstrsInterpreter(Frame frame,
            MethodInfo currentMethod,
            Vector methodCallingParameters,
            Vector nextPossibleInstructionsIds) {
        return InvokeInstDCR.getInstanceOf(frame, currentMethod,
                methodCallingParameters,
                nextPossibleInstructionsIds);
    }

  
    
    
    @Override
    public MathInstrs createMathInstrsInterpreter(OperandStack stack,
            MethodInfo currentMethod, int currentPC) {
        return MathInstrsDCR.getInstanceOf(stack, currentMethod, currentPC);
    }

 
    @Override
    public MiscInstrs createMiscInstrsInterpreter(Vector<Long> nextPossibleInstructionsIds,
            OperandStack stack, MethodInfo currentMethodStatic, LocalVariables localVar,
            int currentPC, HashSet<Type> returnTypes) {
        return MiscInstrsDCR.getInstanceOf(nextPossibleInstructionsIds,
                stack, currentMethodStatic,
                localVar, currentPC, returnTypes);
    }

    
    @Override
    public ObjCreatorInstrs createObjCreatorInstrsInterpreter(OperandStack stack,
            MethodInfo currentMethod) {
        return ObjCreatorInstrsDCR.getInstanceOf(stack, currentMethod);
    }

    
    @Override
    public PureStackInstrs createPureStackInstrsInterpreter(OperandStack stack,
            MethodInfo currentMethod) {
        return PureStackInstrDCR.getInstanceOf(stack, currentMethod);
    }

}
