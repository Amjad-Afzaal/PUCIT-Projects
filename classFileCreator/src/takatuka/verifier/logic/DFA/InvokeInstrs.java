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

import takatuka.util.*;
import java.util.*;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.dataObjs.constantPool.*;
import takatuka.classreader.logic.constants.*;
import takatuka.verifier.dataObjs.*;
import takatuka.verifier.dataObjs.attribute.*;
import takatuka.verifier.logic.*;
import takatuka.verifier.logic.exception.*;
import takatuka.classreader.logic.util.*;
import takatuka.verifier.logic.factory.*;

/**
 * <p>Title: </p>
 * <p>Description:
 *
 * To verify invokestatic, invokespecial, invokevirtual, invokeinterface....
 * 
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class InvokeInstrs {

    protected static MethodInfo currentMethod = null;
    protected static Vector methodCallingParameters = null;
    private static final InvokeInstrs myObj = new InvokeInstrs();
    protected static final Oracle oracle = Oracle.getInstanceOf();
    protected static OperandStack stack = null;
    protected static LocalVariables localVar = null;
    protected static Vector<Long> nextPossibleInstructionsIds = null;

    protected InvokeInstrs() {
    }

    public static void init(Frame frame_,
            MethodInfo currentMethod_,
            Vector methodCallingParameters_,
            Vector nextPossibleInstructionsIds_) {
        currentMethod = currentMethod_;
        methodCallingParameters = methodCallingParameters_;
        stack = frame_.getOperandStack();
        localVar = frame_.getLocalVariables();
        nextPossibleInstructionsIds = nextPossibleInstructionsIds_;

    }

    public static InvokeInstrs getInstanceOf(Frame frame,
            MethodInfo currentMethod,
            Vector methodCallingParameters,
            Vector nextPossibleInstructionsIds) {
        init(frame, currentMethod,
                methodCallingParameters, nextPossibleInstructionsIds);
        return myObj;
    }

    public MethodInfo getCurrentMethod() {
        return currentMethod;
    }

    public Vector getCallingPara() {
        return methodCallingParameters;
    }

    /**
     * it return 0 is void.
     * return -1 if reference and -2 if arrayreference
     * and return ret otherwise.
     *
     * @param descr
     * @return
     */
    private static int getMethodDummyReturnType(char descr) {
        int type = 0;
        if (descr == 'V') {
            return Type.VOID;
        }
        if (descr == '[') {
            type = -2;  //array reference.
        } else if (descr == 'L') {
            type = -1; //reference
        } else if (descr == 'B') {
            type = Type.BYTE;
        } else if (descr == 'C') {
            type = Type.CHAR;
        } else if (descr == 'I') {
            type = Type.INTEGER;
        } else if (descr == 'S') {
            type = Type.SHORT;
        } else if (descr == 'F') {
            type = Type.FLOAT;
        } else if (descr == 'Z') {
            type = Type.BOOLEAN;
        } else if (descr == 'D') {
            type = Type.DOUBLE;
        } else if (descr == 'J') {
            type = Type.LONG;
        }
        return type;
    }

    private static int getReferenceType(String name, int refIndex, MultiplePoolsFacade cp) {
        int cur = refIndex;
        int start = cur + 1;
        String refName = name.substring(start, name.indexOf(";", start));
        return Oracle.getInstanceOf().getClassInfoByName(refName, cp);
    }

    public static Type getMethodDummyReturnType(String descr, MultiplePoolsFacade cp) {
        VerificationFrameFactory frameFactory = FrameFactoryPlaceHolder.getInstanceOf().getFactory();
        descr = descr.substring(descr.indexOf(")") + 1);
        int index = 0;
        int retType = getMethodDummyReturnType(descr.charAt(index));
        boolean isReference = false;
        boolean isArrayRef = false;
        while (retType == -2) {
            isArrayRef = true;
            isReference = true;
            index++;
            retType = getMethodDummyReturnType(descr.charAt(index));
        }
        if (retType == -1) { // reference
            isReference = true;
            retType = getReferenceType(descr, index, cp);
        }
        Type toReturn = frameFactory.createType(retType, isReference, -1);
        if (isArrayRef) {
            toReturn.setIsArray();
        }
        return toReturn;
    }

    protected void jumpToNxtFun(Vector parms, ReferenceInfo ref,
            boolean isStatic, boolean isSpecial,
            String methodDesc,
            VerificationInstruction currentInstr, String methodNameForDebug) throws Exception {
        VerificationFrameFactory frameFactory = FrameFactoryPlaceHolder.getInstanceOf().getFactory();
        //test();
        Type retType = getMethodDummyReturnType(methodDesc,
                currentInstr.getMethod().getClassFile().getConstantPool());
        if (retType.isIntOrShortOrByteOrBooleanOrCharType()) {
            retType = frameFactory.createType(Type.INTEGER);
        }
        if (!retType.isReference() && retType.getType() == Type.VOID) {
            //do nothing
        } else {
            stack.push(retType);
        }
    }

    /**
     * For invokeVirtual check
     * -- it should not be an <init> or <clinit> method.
     */
    protected void verifyMethod(MultiplePoolsFacade pOne, ReferenceInfo ref,
            NameAndTypeInfo nAt,
            int opCode) {
        boolean isSpecial = opCode == JavaInstructionsOpcodes.INVOKESPECIAL;
        boolean isVirtual = opCode == JavaInstructionsOpcodes.INVOKEVIRTUAL;
        boolean isStatic = opCode == JavaInstructionsOpcodes.INVOKESTATIC;
        boolean isInstanceInit = Oracle.getInstanceOf().isInstanceInitMethod(ref, pOne);
        boolean isClassInterInit = Oracle.getInstanceOf().isClassInterfaceInitMethod(ref, pOne);
        if ((isVirtual && (isInstanceInit || isClassInterInit)) || (!isSpecial && (isInstanceInit || isClassInterInit))) {
            throw new VerifyErrorExt(Messages.INOVKE_VIRTUAL_VERIFICATION);
        } else if (isStatic) {
            ClassFile cFile = oracle.getClass(ref.getIndex(), pOne);
            String key = MethodInfo.createKey(nAt.getDescriptorIndex(),
                    nAt.getIndex(), pOne);
            MethodInfo method = cFile.hasMethod(key);
            while (method == null && cFile.getSuperClass().intValueSigned() != 0) {
                cFile = Oracle.getInstanceOf().getSuperClass(cFile);
                method = cFile.hasMethod(key);
            }
            if (method == null || !method.getAccessFlags().isStatic()
                    || method.getAccessFlags().isAbstract()) {
                throw new VerifyErrorExt(Messages.INOVKE_STATIC_VERIFICATION);
            }

        }

    }

    protected void invokeInstruction(String methodDesc, int opCode, ReferenceInfo ref,
            VerificationInstruction currentInstr,
            String methodNameForDebug) {
        Vector parms = verifyDescription(methodDesc, stack);
        Type classPointer = null;
        if (opCode != JavaInstructionsOpcodes.INVOKESTATIC) {
            classPointer = stack.pop();
            parms.add(0, classPointer);
            if (!classPointer.isReference()) {
                throw new VerifyError(Messages.STACK_INVALID);
            }
        }
        //now call method to actually excute/verify.
        ClassFile oldClass = ClassFile.currentClassToWorkOn; //save the current class
        try {
            jumpToNxtFun(parms, ref,
                    opCode == JavaInstructionsOpcodes.INVOKESTATIC,
                    opCode == JavaInstructionsOpcodes.INVOKESPECIAL,
                    methodDesc,
                    currentInstr,
                    methodNameForDebug);
        } catch (Exception d) {
            throw new VerifyErrorExt("");
        }
        //set the class back to original class
        ClassFile.currentClassToWorkOn = oldClass;
        //DataFlowAnalyzer.Debug_print("back to class =" + ClassFile.currentClassToWorkOn.getFullyQualifiedClassName());
    }

    /**
     *  ... , objectref, [arg1, [arg2 ...]]  ==> ...
     *  OR
     * ... , [arg1, [arg2 ...]]  ==> ...
     *
     *  Step 1: Get instruction operand of length 2 bytes
     *  Step 2: Index it in the constant pool and get corresponding methodRefInfo
     *  Step 3: Get class and method-description from methodRefInfo.
     *  Step 4: Pop right number of arguments based on method description. Verify their types based on method description
     *  Step 5: In case the instruction was not invoke static then pop this argument (objectref) and do following
     *        Step 5(a): go to this class and find function. If function is not found then find it in super classes
     *  Step 6: Go to that class method and start validating it. Leaving behind incomplete validation of current method.
     */
    protected void invokeInstruction(MultiplePoolsFacade pOne,
            VerificationInstruction inst, int opCode) {
        int tag = getInvokeTag(opCode);
        try {
            Un operandData = Un.cutBytes(2, ((Un) inst.getOperandsData().clone()));
            if (operandData.intValueUnsigned() == Short.MAX_VALUE) {
                nextPossibleInstructionsIds.clear();
                return;
            }
            ReferenceInfo ref = (ReferenceInfo) pOne.get(operandData.intValueUnsigned(),
                    tag);
            NameAndTypeInfo nAt = (NameAndTypeInfo) pOne.get(ref.getNameAndTypeIndex().
                    intValueUnsigned(), CPTagValues.CONSTANT_NameAndType);
            pOne = inst.getMethod().getClassFile().getConstantPool();
            verifyMethod(pOne, ref, nAt, opCode);
            UTF8Info utf8 = (UTF8Info) pOne.get(nAt.getDescriptorIndex().intValueUnsigned(),
                    CPTagValues.CONSTANT_Utf8);
            String methodDesc = utf8.convertBytes();
            utf8 = (UTF8Info) pOne.get(nAt.getIndex().intValueUnsigned(),
                    CPTagValues.CONSTANT_Utf8);
            String methodName = utf8.convertBytes();
            //Miscellaneous.println("\n\n********* going to call method. name=" + methodName + ", desc =" + methodDesc);
            DataFlowAnalyzer.Debug_print1("\n\n********* going to call method. name=" + methodName + ", desc =" + methodDesc);
            if (methodName.contains("getMoteInfoDriver")) {
                //Miscellaneous.println("stop here 2324");
            }
            invokeInstruction(methodDesc, opCode, ref, inst, methodName);
        } catch (Exception d) {
            throw new VerifyErrorExt(Messages.STACK_INVALID);
        }
    }

    /**
     * As described
     * http://java.sun.com/docs/books/jvms/second_edition/html/Instructions2.doc6.html
     * in invokevirtual instruction description
     *
     * @param pointerToStart
     * @param methodKey
     * @return
     */
    public static final ClassFile methodClassLookup(MultiplePoolsFacade pOne,
            int pointerToStart, String methodKey) {
        ClassFile cFile = null;
        if (pointerToStart < 0) {
            pointerToStart = 0;
        }
        cFile = oracle.getClass(pointerToStart, pOne);
        if (cFile == null) {
            DataFlowAnalyzer.Debug_print1("Cannot find classfile with this pointer "
                    + pointerToStart + " for method " + methodKey);
            return null;
        }
        //Miscellaneous.println("cfile name = " + cFile.getFullyQualifiedClassName());
        return methodClassLookup(pOne, cFile, methodKey);
    }

    /**
     * As described
     * http://java.sun.com/docs/books/jvms/second_edition/html/Instructions2.doc6.html
     * in invokevirtual instruction description
     *
     * @param cFile
     * @param methodKey
     * @return
     */
    private static final ClassFile methodClassLookup(MultiplePoolsFacade pOne,
            ClassFile cFile, String methodKey) {
        try {
            MethodInfo methodInfo = cFile.hasMethod(methodKey);
            if (methodInfo != null) {
                return cFile;
            }

            int superClass = cFile.getSuperClass().intValueUnsigned();
            if (superClass != 0) {
                return methodClassLookup(pOne, superClass, methodKey);
            }

        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }

        return null;
    }

    protected Vector verifyDescription(String descStr, OperandStack stack) {
        Type argType = null;
        int cur = 1;
        Vector ret = new Vector();
        while (descStr.charAt(cur) != ')') {
            if (descStr.charAt(cur) != '[') {
                argType = stack.pop();
                ret.add(0, argType);
            }
            if (descStr.charAt(cur) == 'L') { //reference
                //note: somekind of type check could be added here but not always...
                while (descStr.charAt(cur) != ';') {
                    cur++;
                }
            }
            cur++;
        }
        return ret;
    }

    protected Type createNewIdForString(VerificationInstruction currentInstr) {
        //1 get ClassId of String class
        ClassFile stringClass = oracle.getClass("java/lang/String");
        if (stringClass == null) {
            Miscellaneous.printlnErr("String classed used but not found. Error #98 Exiting");
            Miscellaneous.exit();
        }
        VerificationFrameFactory frameFactory = FrameFactoryPlaceHolder.getInstanceOf().getFactory();

        Type ret = frameFactory.createType(true);
        ret.setReferenceType("java/lang/String;", -1, currentInstr.getMethod().
                getClassFile().getConstantPool());
        return ret;
    }

    protected int getInvokeTag(int opCode) {
        int tag = -1;
        if (opCode == JavaInstructionsOpcodes.INVOKEINTERFACE) {
            tag = CPTagValues.CONSTANT_InterfaceMethodref;
        } else if (opCode == JavaInstructionsOpcodes.GETFIELD
                || opCode == JavaInstructionsOpcodes.PUTFIELD
                || opCode == JavaInstructionsOpcodes.GETSTATIC
                || opCode == JavaInstructionsOpcodes.PUTSTATIC) {
            tag = CPTagValues.CONSTANT_Fieldref;
        } else {
            tag = CPTagValues.CONSTANT_Methodref;
        }

        return tag;
    }

    /**
     * The resolved method is selected for invocation unless isThreeSpecial returns true
     * if isThreeSpecial returns true then
     * actual method to be invoked is selected by the following lookup procedure using superclass of current class
     * 
     * @param currentClass
     * @param methodName
     * @param methodClass
     * @return
     * @throws Exception
     */
    protected int getTargetFunctionIfInvokeSpecial(ClassFile currentClass,
            String methodName, ClassFile methodClass) throws Exception {
        boolean isThreeSpecial = isThreeSpecial(currentClass, methodClass, methodName);
        if (isThreeSpecial) {
            methodClass = oracle.getSuperClass(currentClass);
            if (methodClass == null) {
                Miscellaneous.printlnErr("Cannot found super-class of class =" + currentClass.getFullyQualifiedClassName());
                Miscellaneous.exit();
            }

        }
        return methodClass.getThisClass().intValueUnsigned();
    }

    /**
     * see documentation of invokespecial at
     * http://java.sun.com/docs/books/jvms/second_edition/html/Instructions2.doc6.html
     * it says:
     * the resolved method is selected for invocation unless all of the following conditions are true:
     * 1) The ACC_SUPER flag (see Table 4.1, "Class access and property modifiers") is set for the current class.
     * 2) The class of the resolved method is a superclass of the current class.
     * 3) The resolved method is NOT an instance initialization method (3.9).
     *
     *
     * @param currentClass
     * @param methodClass
     * @param methodName
     * @return
     * @throws Exception
     */
    protected boolean isThreeSpecial(ClassFile currentClass, ClassFile methodClass,
            String methodName) throws Exception {
        boolean ACCSuper = currentClass.getAccessFlags().isACCSuper();
        //isSubClass (A, B) returns true if A is subclass of B. That means B is superclass of A
        boolean isSuper = Oracle.getInstanceOf().isSubClass(currentClass,
                methodClass);
        boolean isInstacceInit = Oracle.getInstanceOf().isInstanceInitMethod(methodName);
        return (ACCSuper && isSuper && !isInstacceInit);
    }
}
