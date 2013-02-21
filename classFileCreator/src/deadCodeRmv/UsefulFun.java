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
package deadCodeRmv;

import deadCodeRmv.dataObjs.*;
import java.util.*;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.dataObjs.attribute.*;
import takatuka.classreader.dataObjs.constantPool.MethodRefInfo;
import takatuka.classreader.dataObjs.constantPool.NameAndTypeInfo;
import takatuka.classreader.logic.constants.*;
import takatuka.util.*;
import takatuka.verifier.logic.factory.*;

/**
 *
 * It has the functions that will be needed to do dead-code removal.
 * 
 * @author Faisal Aslam
 */
public class UsefulFun {

    /**
     * do not create its object
     * just use its method directly as they are all
     * static.
     */
    private UsefulFun() {
    }

    /**
     * Given a class file fully qualified name it return the main
     * method written inside that file.
     * 
     * @param classFileName
     * @return 
     */
    public static MethodInfoDCR getMainFunction(String classFileName) {
        Oracle oracle = Oracle.getInstanceOf();
        ClassFile cFile = oracle.getClass(classFileName);
        if (cFile == null) {
            return null;
        }
        MethodInfoDCR method = (MethodInfoDCR) oracle.getMethodOrField(cFile, "main", "([Ljava/lang/String;)V", true);
        if (method != null && method.getAccessFlags().isStatic()) {
            return method;
        }
        /**
         * if no such method in the class file.
         */
        return null;
    }

    /**
     * return the size of operand stack within a method frame.
     * @param method
     * @return 
     */
    public static int getOperandStackSize(MethodInfoDCR method) {
        CodeAtt codeAtt = method.getCodeAtt();
        if (codeAtt != null) {
            return 0;
        }
        return codeAtt.getMaxStack().intValueUnsigned();
    }

    /**
     * return the size of local variable within a methods frame.
     * 
     * @param method
     * @return 
     */
    public static int getLocalVariableSize(MethodInfoDCR method) {
        CodeAtt codeAtt = method.getCodeAtt();
        if (codeAtt != null) {
            return 0;
        }
        return codeAtt.getMaxLocals().intValueUnsigned();
    }

    /**
     * give you all the class read from the input path
     * or created by you.
     * 
     * @return 
     */
    public static Collection<ClassFileDCR> getAllClassFiles() {
        ArrayList<ClassFileDCR> toRet = new ArrayList<ClassFileDCR>();
        ClassFileController cfContr = ClassFileController.getInstanceOf();
        for (int index = 0; index < cfContr.getCurrentSize(); index++) {
            toRet.add((ClassFileDCR) cfContr.get(index));
        }
        return toRet;
    }

    /**
     * return all the methods of a given class.
     * @param cFile
     * @return 
     */
    public static Collection<MethodInfoDCR> getallMethods(ClassFileDCR cFile) {
        ArrayList<MethodInfoDCR> toRet = new ArrayList<MethodInfoDCR>();
        MethodInfoController cfContr = cFile.getMethodInfoController();
        for (int index = 0; index < cfContr.getCurrentSize(); index++) {
            toRet.add((MethodInfoDCR) cfContr.get(index));
        }
        return toRet;

    }

    /**
     * remove a method from a class file.
     * @param cFile
     * @param method 
     */
    public static void removeMethod(ClassFileDCR cFile, MethodInfoDCR method) {
        String inputMethodKey = method.getKey();
        ArrayList<MethodInfoDCR> toRet = new ArrayList<MethodInfoDCR>();
        MethodInfoController cfContr = cFile.getMethodInfoController();
        for (int index = 0; index < cfContr.getCurrentSize(); index++) {
            MethodInfo toRemove = (MethodInfoDCR) cfContr.get(index);
            if (toRemove.getKey().equals(inputMethodKey)) {
                cfContr.remove(index);
                break;
            }
        }
    }

    /**
     * remove a given class
     * @param cFile 
     */
    public static void removeClass(ClassFileDCR cFile) {
        ClassFileController cfContr = ClassFileController.getInstanceOf();
        cfContr.remove(cFile);
    }

    /**
     * Apply DFA on a method
     * @param toAnalyze 
     */
    public static void DFA(MethodInfoDCR toAnalyze) {
        VerificationFrameFactory frameFactory = FrameFactoryPlaceHolder.getInstanceOf().getFactory();
        ClassFile.currentClassToWorkOn = toAnalyze.getClassFile();
        frameFactory.createDataFlowAnalyzer().execute(toAnalyze, null, null);

    }

    /**
     * returns a class file given its fully qualified name.
     * @param classFileName
     * @return 
     */
    public static ClassFileDCR getClassByName(String classFileName) {
        Oracle oracle = Oracle.getInstanceOf();
        ClassFileDCR cFile = (ClassFileDCR) oracle.getClass(classFileName);
        return cFile;
    }

    /**
     * return method info from a given class file.
     * @param cFile
     * @param methodName
     * @param methodDesc
     * @return 
     */
    public static MethodInfoDCR getMethod(ClassFile cFile, String methodName, String methodDesc) {
        Oracle oracle = Oracle.getInstanceOf();
        MethodInfoDCR method = (MethodInfoDCR) oracle.getMethodOrField(cFile, methodName, methodDesc, true);
        return method;
    }

    /**
     * returns name of a method
     * @param method
     * @return 
     */
    public static String getMethodName(MethodInfoDCR method) {
        Oracle oracle = Oracle.getInstanceOf();
        return oracle.methodOrFieldName(method, method.getClassFile().getConstantPool());
    }

    /**
     * returns description of a method
     * @param method
     * @return 
     */
    public static String getMethodDescription(MethodInfoDCR method) {
        Oracle oracle = Oracle.getInstanceOf();
        return oracle.methodOrFieldDescription(method, method.getClassFile().getConstantPool());
    }

    /**
     * Return all the instructions of a method.
     * @param method
     * @return 
     */
    public static Collection<InstructionDCR> getAllInstructions(MethodInfoDCR method) {
        ArrayList<InstructionDCR> toRet = new ArrayList<InstructionDCR>();
        Vector<Instruction> instrVec = method.getInstructions();
        for (int loop = 0; loop < instrVec.size(); loop++) {
            toRet.add((InstructionDCR) instrVec.elementAt(loop));
        }
        return toRet;
    }

    /**
     * return a class file from a given new instruction.
     * If class file is not in the input path or the instruction
     * is not new then it returns false.
     * 
     * @param newInstr
     * @return 
     */
    public static ClassFileDCR getClassFileFromNewInstr(Instruction newInstr) {
        ClassFileDCR cFile = (ClassFileDCR) newInstr.getMethod().getClassFile();
        Oracle oracle = Oracle.getInstanceOf();
        if (newInstr.getOpCode() != JavaInstructionsOpcodes.NEW
                && newInstr.getOpCode() != JavaInstructionsOpcodes.ANEWARRAY) {
            return null;
        }
        int cpIndex = newInstr.getOperandsData().intValueUnsigned();
        MultiplePoolsFacade cp = cFile.getConstantPool();
        return (ClassFileDCR) oracle.getClass(cpIndex, cp);
    }

    /**
     * return the class file used by the invoke instruction.
     * 
     * @param invokeInstr
     * @return 
     */
    public static ClassFile getClassFileOfInvokeInstr(
            Instruction invokeInstr) {
        ClassFileDCR cFile = (ClassFileDCR) invokeInstr.getMethod().getClassFile();
        Oracle oracle = Oracle.getInstanceOf();
        if (!invokeInstr.getMnemonic().startsWith("INVOKE")) {
            return null;
        }
        int cpIndex = invokeInstr.getOperandsData().trim(2).intValueUnsigned();
        MultiplePoolsFacade cp = cFile.getConstantPool();
        MethodRefInfo methodRefInfo = (MethodRefInfo) cp.get(cpIndex);
        int classInfoCPIndex = methodRefInfo.getIndex().intValueSigned();
        ClassFile methodClassFile = oracle.getClass(classInfoCPIndex, cp);
        return methodClassFile;
    }

    /**
     * return the method that can be reached from the invoke instruction
     * in the worst possible scenario
     * 
     * @param invokeInstr
     * @return 
     */
    public static Collection<MethodInfoDCR> getMethodInfoFromInvokeInstr(
            Instruction invokeInstr) {
        ClassFileDCR cFile = (ClassFileDCR) invokeInstr.getMethod().getClassFile();

        ArrayList<MethodInfoDCR> methodList = new ArrayList<MethodInfoDCR>();
        if (!invokeInstr.getMnemonic().startsWith("INVOKE")) {
            return null;
        }
        boolean isStatic = false;
        if (invokeInstr.getOpCode() == JavaInstructionsOpcodes.INVOKESTATIC) {
            isStatic = true;
        }
        int cpIndex = invokeInstr.getOperandsData().trim(2).intValueUnsigned();
        ClassFile methodClassFile = getClassFileFromNewInstr(invokeInstr);
        String methodKey = getMethodKey(cFile, cpIndex);
        if (!isStatic) {
            return getAllMethodOfSameSignuature(methodKey);
        } else {
            MethodInfoDCR method = findMethod(methodClassFile, methodKey);
            methodList.add(method);
        }

        return methodList;
    }

    private static MethodInfoDCR findMethod(ClassFile methodClassFile, String methodkey) {
        MethodInfoController contr = methodClassFile.getMethodInfoController();
        for (int loop = 0; loop < contr.getCurrentSize(); loop++) {
            MethodInfoDCR method = (MethodInfoDCR) contr.get(loop);
            if (method.getKey().equals(method)) {
                return method;
            }
        }
        Oracle oracle = Oracle.getInstanceOf();
        ClassFile superClass = oracle.getSuperClass(methodClassFile);
        if (superClass != null) {
            return findMethod(superClass, methodkey);
        }
        return null;
    }

    private static ArrayList<MethodInfoDCR> getAllMethodOfSameSignuature(String methodKey) {
        Oracle oracle = Oracle.getInstanceOf();
        ArrayList<MethodInfoDCR> methodList = new ArrayList<MethodInfoDCR>();
        Vector<MethodInfo> methodInfoVec = oracle.getAllMethodInfo();
        for (int index = 0; index < methodInfoVec.size(); index++) {
            MethodInfoDCR method = (MethodInfoDCR) methodInfoVec.get(index);
            String keyToCompare = method.getKey();
            if (methodKey.equals(keyToCompare)) {
                methodList.add(method);
            }
        }
        return methodList;
    }

    private static String getMethodKey(ClassFileDCR cFile, int cpIndexForMethodRef) {
        Oracle oracle = Oracle.getInstanceOf();
        MultiplePoolsFacade cp = cFile.getConstantPool();
        MethodRefInfo methodRefInfo = (MethodRefInfo) cp.get(cpIndexForMethodRef);
        int cpNATIndex = methodRefInfo.getNameAndTypeIndex().intValueUnsigned();
        NameAndTypeInfo nAtInfo = (NameAndTypeInfo) cp.get(cpNATIndex);
        int descUTF8Index = nAtInfo.getDescriptorIndex().intValueUnsigned();
        String desc = oracle.getUTF8(descUTF8Index, cp);
        int nameUTF8Index = nAtInfo.getIndex().intValueUnsigned();
        String name = oracle.getUTF8(nameUTF8Index, cp);
        return MethodInfo.createKey(desc, name);
    }

    /**
     * Remove a specific instruction from a method.
     * 
     * @param instrToRemove 
     */
    public static void removeInstruction(InstructionDCR instrToRemove) {
        MethodInfoDCR method = (MethodInfoDCR) instrToRemove.getMethod();
        Vector<Instruction> methodInstr = method.getInstructions();
        for (int loop = 0; loop < methodInstr.size(); loop++) {
            InstructionDCR instr = (InstructionDCR) methodInstr.get(loop);
            if (instr.getInstructionId() == instrToRemove.getInstructionId()) {
                methodInstr.remove(loop);
                break;
            }
        }
        method.getCodeAtt().setInstructions(methodInstr);
    }

    /**
     * return all the super classes of the current class.
     * @param cFile
     * @return 
     */
    public static Collection<ClassFileDCR> getAllSuperClasses1(ClassFileDCR cFile) {
        Vector<ClassFileDCR> toRet = new Vector<ClassFileDCR>();
        try {
            Oracle oracle = Oracle.getInstanceOf();
            oracle.getAllSuperClasses(cFile, toRet);
        } catch (Exception d) {
            d.printStackTrace();
            System.exit(1);
        }
        return toRet;
    }
}
