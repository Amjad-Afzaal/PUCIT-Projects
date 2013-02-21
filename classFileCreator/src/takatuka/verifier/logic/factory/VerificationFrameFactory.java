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
package takatuka.verifier.logic.factory;

import takatuka.classreader.dataObjs.MethodInfo;
import takatuka.verifier.dataObjs.*;
import takatuka.verifier.logic.DFA.*;
import java.util.*;

/**
 * 
 * Description:
 * <p>
 * </p> 
 * @author Faisal Aslam
 * @version 1.0
 */
public class VerificationFrameFactory {

    private static final VerificationFrameFactory frameFactory = new VerificationFrameFactory();


    protected VerificationFrameFactory() {
        super();
    }

    public static VerificationFrameFactory getInstanceOf() {
        return frameFactory;
    }

    /**
     *
     * @param maxStack
     * @return
     */
    public OperandStack createOperandStack(int maxStack) {
        return new OperandStack(maxStack);
    }

    /**
     *
     * @param maxStack
     * @return
     */
    public LocalVariables createLocalVariables(int maxStack) {
        return new LocalVariables(maxStack);
    }

    /**
     *
     * @return
     */
    public DataFlowAnalyzer createDataFlowAnalyzer() {
        return DataFlowAnalyzer.getInstanceOf();
    }

    /**
     *
     * @return
     */
    public Type createType() {
        return new Type();
    }

    /**
     *
     * @param isReference
     * @return
     */
    public Type createType(boolean isReference) {
        return new Type(isReference);
    }

    /**
     *
     * @param type
     * @return
     */
    public Type createType(int type) {
        return new Type(type);
    }

    /**
     *
     * @param type
     * @param isReference
     * @param newId
     * @return
     */
    public Type createType(int type, boolean isReference, int newId) {
        return new Type(type, isReference, newId);
    }


    /**
     *
     * @param frame
     * @param currentMethod
     * @return
     */
    public BytecodeVerifier createBytecodeVerifier(Frame frame, MethodInfo currentMethod,
            Vector callingParameters) {
        return new BytecodeVerifier(frame, currentMethod, callingParameters);
    }

    public ExceptionHandler createExceptionHandler(OperandStack stack,
            Vector<Long> nextPossibleInstructionsIds, MethodInfo currentMethod,
            int currentPC) {
        return ExceptionHandler.getInstanceOf(stack, nextPossibleInstructionsIds, currentMethod, currentPC);
    }
    
    /**
     * 
     * @param stack
     * @param currentMethod
     * @param invokeInsrsInterpreter
     * @return
     */
    public FieldInstrs createFieldInstrsInterpreter(OperandStack stack,
            MethodInfo currentMethod, InvokeInstrs invokeInsrsInterpreter) {
        return FieldInstrs.getInstanceOf(stack, currentMethod, invokeInsrsInterpreter);
    }

    /**
     *
     * @param nextPossibleInstructionsIds
     * @param stack
     * @param currentMethod
     * @return
     */
    public IfAndCmpInstrs createIfAndCmpInstrsInterpreter(Vector<Long> nextPossibleInstructionsIds,
            OperandStack stack, MethodInfo currentMethod) {
        return IfAndCmpInstrs.getInstanceOf(nextPossibleInstructionsIds, stack, currentMethod);
    }


    /**
     * 
     * @param frame
     * @param currentMethod
     * @param methodCallingParameters
     * @param nextPossibleInstructionsIds
     * @return
     */
    public InvokeInstrs createInvokeInstrsInterpreter(Frame frame,
            MethodInfo currentMethod,
            Vector methodCallingParameters,
            Vector nextPossibleInstructionsIds) {
        return InvokeInstrs.getInstanceOf(frame, currentMethod,
                methodCallingParameters,
                nextPossibleInstructionsIds);
    }

    /**
     * 
     * @param frame
     * @param currentMethod
     * @param callingParams
     * @return
     */
    public LoadAndStoreInstrs createLoadAndStoreInstrsInterpreter(Frame frame,
            MethodInfo currentMethod, Vector callingParams) {
        return LoadAndStoreInstrs.getInstanceOf(frame, currentMethod,
                callingParams);
    }

    
    /**
     * 
     * @param stack
     * @param currentMethod
     * @param currentPC
     * @return
     */
    public MathInstrs createMathInstrsInterpreter(OperandStack stack,
            MethodInfo currentMethod, int currentPC) {
        return MathInstrs.getInstanceOf(stack, currentMethod, currentPC);
    }


    /**
     * 
     * @param nextPossibleInstructionsIds
     * @param stack
     * @param currentMethodStatic
     * @param localVar
     * @param currentPC
     * @param returnTypes
     * @return
     */
    public MiscInstrs createMiscInstrsInterpreter(Vector<Long> nextPossibleInstructionsIds,
            OperandStack stack, MethodInfo currentMethodStatic, LocalVariables localVar,
            int currentPC, HashSet<Type> returnTypes) {
        return MiscInstrs.getInstanceOf(nextPossibleInstructionsIds,
                stack, currentMethodStatic,
                localVar, currentPC, returnTypes);
    }

    public ObjCreatorInstrs createObjCreatorInstrsInterpreter(OperandStack stack,
            MethodInfo currentMethod) {
        return ObjCreatorInstrs.getInstanceOf(stack, currentMethod);
    }

    /**
     * 
     * @param stack
     * @param currentMethod
     * @return
     */
    public PureStackInstrs createPureStackInstrsInterpreter(OperandStack stack,
            MethodInfo currentMethod) {
        return PureStackInstrs.getInstanceOf(stack, currentMethod);
    }
}
