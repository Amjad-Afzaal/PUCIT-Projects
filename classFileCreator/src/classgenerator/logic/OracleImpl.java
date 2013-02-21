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
 * Please contact Faisal Aslam Copyright 2012
 * if you need additional information or have any questions.
 */
package classgenerator.logic;

import java.util.*;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.dataObjs.attribute.*;
import takatuka.classreader.dataObjs.constantPool.*;
import takatuka.classreader.logic.constants.*;
import takatuka.classreader.logic.factory.*;

/**
 *
 *
 * @author Faisal Aslam
 */
public class OracleImpl implements Oracle {

    private static final String SOURCE_ATT_NAME = "SourceFile";
    private static final String CODE_ATT_NAME = "Code";

    private CodeAtt createEmptyCodeAtt(ClassFile cFile) {
        try {
            FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
            int cpIndex = addUTF8InCP(cFile, CODE_ATT_NAME);
            Un cpIndexUn = factoryFacade.createUn(cpIndex).trim(2);
            return factoryFacade.createCodeAttribute(cpIndexUn);
        } catch (Exception d) {
            d.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public int addUTF8InCP(ClassFile cFile, String str) throws Exception {
        FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
        UTF8Info utf8Info = factoryFacade.createUTF8Info();
        byte[] classNameArray = str.getBytes("UTF-8");
        utf8Info.setBytes(factoryFacade.createUn(classNameArray));
        Un length = factoryFacade.createUn(classNameArray.length).trim(2);
        utf8Info.setLength(length);
        int utf8CPIndex = addInCP(cFile, utf8Info);
        return utf8CPIndex;
    }

    public int addClassInfoInCP(ClassFile cFile, int nameUTF8Index) throws Exception {
        FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
        MultiplePoolsFacade cp = cFile.getConstantPool();
        ClassInfo classInfo = factoryFacade.createClassInfo();
        classInfo.setIndex(nameUTF8Index);
        //classInfo.setClassName(name);
        //classInfo.setIsInterface(false);
        return addInCP(cFile, classInfo);
    }

    public void addSourceFileAttribute(ClassFile cFile, String sourceFileName) throws Exception {

        FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();

        int attNameCPIndex = addUTF8InCP(cFile, SOURCE_ATT_NAME);
        int nameSrcFileIndex = addUTF8InCP(cFile, sourceFileName);

        Un attNameCPIndexUn = factoryFacade.createUn(attNameCPIndex).trim(2);
        Un nameSrcFileIndexUn = factoryFacade.createUn(nameSrcFileIndex).trim(2);
        Un attLengthUn = factoryFacade.createUn(2).trim(4);
        SourceFileAtt sourceFileAtt = factoryFacade.createSourceFileAttribute(attNameCPIndexUn,
                attLengthUn, nameSrcFileIndexUn);
        cFile.addAttributeInfo(sourceFileAtt);
    }

    private Un addPointerInClass(ClassFile cFile, String name) throws Exception {

        FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();

        /**
         * Step 1: Add UTF-8 Info in the constant pool according to 
         * the given class-file name.
         */
        int utf8CPIndex = addUTF8InCP(cFile, name);

        /**
         * Step 2: The following code adds Classinfo corresponding to the newly added 
         * UTF8.
         */
        int classInfoCPIndex = addClassInfoInCP(cFile, utf8CPIndex);

        /**
         * Step 3: create the pointer 
         */
        Un pointer = factoryFacade.createUn(classInfoCPIndex).trim(2);
        return pointer;
    }

    public void addThisOrSuperPointer(ClassFile cFile, String name, boolean isThisPointer) throws Exception {
        Un classPointer = addPointerInClass(cFile, name);
        if (isThisPointer) {
            cFile.setThisClass(classPointer);
        } else {
            cFile.setSuperClass(classPointer);
        }
    }

    public int addInCP(ClassFile cFile, Object object) throws Exception {
        ConstantPool cp = (ConstantPool) cFile.getConstantPool();
        for (int cpIndex = 1; cpIndex < cp.getCurrentSize(-1); cpIndex++) {
            Object obj = cp.get(cpIndex, -1);
            if (obj.equals(object)) {
                return cpIndex;
            }
        }
        int index = cp.add(object, -1);
        if (cp.getMaxSize() < index + 1) {
            cp.setMaxSize(index + 1);
        }
        return index;
    }

    private String getParameterStr(ArrayList<FieldType> callingParmeterTypes) {
        String ret = "";
        if (callingParmeterTypes == null) {
            return "";
        }
        Iterator<FieldType> it = callingParmeterTypes.iterator();
        while (it.hasNext()) {
            FieldType type = it.next();
            ret += type.toString();
        }
        return ret;
    }

    public MethodInfo addFunction(ClassFile cFile, String functionName, ArrayList<FieldType> callingParmeterTypes, FieldType returnType) {
        return addFunction(AccessFlagValues.ACC_PUBLIC, cFile, functionName, callingParmeterTypes, returnType);
    }

    public MethodInfo addFunction(int AccessFlagValue, ClassFile cFile, String functionName, ArrayList<FieldType> callingParmeterTypes, FieldType returnType) {
        MethodInfo method = null;
        try {
            FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
            method = factoryFacade.createMethodInfo(cFile);
            method.setAccessFlags(factoryFacade.createUn(AccessFlagValue).trim(2));

            /**
             * Adding name and desc indexes in CP
             */
            int nameUTF8CPIndex = addUTF8InCP(cFile, functionName);
            if (returnType == null) {
                returnType = new FieldType(FieldTypes.VOID);
            }
            String descStr = "(" + getParameterStr(callingParmeterTypes) + ")" + returnType;
            int descUTF8CPIndex = addUTF8InCP(cFile, descStr);


            /**
             * Adding name and desc CP indexes in the function
             */
            Un nameUTF8CPIndexUn = factoryFacade.createUn(nameUTF8CPIndex).trim(2);
            Un descUTF8CPIndexUn = factoryFacade.createUn(descUTF8CPIndex).trim(2);
            method.setNameIndex(nameUTF8CPIndexUn);
            method.setDescriptorIndex(descUTF8CPIndexUn);

            cFile.getMethodInfoController().add(method);
            CodeAtt codeAtt = createEmptyCodeAtt(cFile);
            method.getAttributeController().add(codeAtt);

        } catch (Exception d) {
            d.printStackTrace();
            System.exit(1);
        }
        return method;
    }

    public MethodInfo addFunction(ClassFile cFile, String functionName) {
        return addFunction(cFile, functionName, null, null);
    }

    public AccessFlags generateAccessFlag(ArrayList<Integer> accessFlagValues) {
        try {
            FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
            Iterator<Integer> it = accessFlagValues.iterator();
            int ret = 0;
            while (it.hasNext()) {
                int accessFlag = it.next();
                ret = ret | accessFlag;
            }
            Un accessFlagUn = factoryFacade.createUn(ret).trim(2);
            return factoryFacade.createAccessFlag(accessFlagUn);
        } catch (Exception d) {
            d.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public void addInstruction(ClassFile cFile, MethodInfo method, Instruction instr) {
        try {
            FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
            CodeAtt codeAtt = method.getCodeAtt();
            if (codeAtt == null) {
                method.getAttributeController().add(instr.getParentCodeAtt());
            }
            Vector<Instruction> existingInstr = codeAtt.getInstructions();
            existingInstr.add(instr);
            codeAtt.setInstructions(existingInstr);
            

        } catch (Exception d) {
            d.printStackTrace();
            System.exit(1);
        }

    }

    public ClassFile createEmptyClassFile(String className, String javaSourceName) throws Exception {
        FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
        int magicnumber = 0xcafebabe;
        int minorVersion = 0; //00
        int majorVersion = 0x32; //00

        ClassFile cFile = factoryFacade.createClassFile(factoryFacade.createConstantPool(),
                className);
        /**
         * Setting the magicnumber and major and minor.
         * A user should not be able to change magic number later on.
         */
        cFile.setMagicNumber(factoryFacade.createUn(magicnumber).trim(4));
        cFile.setMinorVersion(factoryFacade.createUn(minorVersion).trim(2));
        cFile.setMajorVersion(factoryFacade.createUn(majorVersion).trim(2));
        /*
         * Setting the public access file. A user may change it later on.
        CONSTRUCTOR_NAME*/
        AccessFlags accessFlag = factoryFacade.createAccessFlag(factoryFacade.createUn(AccessFlags.ACC_PUBLIC).trim(2));
        cFile.setAccessFlags(accessFlag);


        addThisOrSuperPointer(cFile, className, true);
        addThisOrSuperPointer(cFile, Constants.OBJECT_NAME, false);

        addSourceFileAttribute(cFile, javaSourceName);
        ClassFileController.getInstanceOf().add(cFile);

        return cFile;
    }

    public String getSuperClassName(ClassFile cFile) {
        int cpIndexForSuperClassInfo = cFile.getSuperClass().intValueUnsigned();
        MultiplePoolsFacade cp = cFile.getConstantPool();
        ClassInfo superClassInfo = (ClassInfo) cp.get(cpIndexForSuperClassInfo);
        return ((UTF8Info) cp.get(superClassInfo.getIndex().intValueUnsigned())).convertBytes();
    }

    public int findClassInfoFromCP(ClassFile cFile, String className) {
        MultiplePoolsFacade cp = cFile.getConstantPool();
        for (int loop = 1; loop < cp.getCurrentSize(); loop++) {
            Object obj = cp.get(loop);
            if (obj instanceof ClassInfo) {
                ClassInfo cInfo = (ClassInfo) obj;
                int nameCPIndex = cInfo.getIndex().intValueUnsigned();
                UTF8Info nameUTF8 = (UTF8Info) cp.get(nameCPIndex);
                String checkName = nameUTF8.convertBytes();
                if (checkName.equals(className)) {
                    return loop;
                }
            }
        }
        return -1;
    }

    public MethodInfo addDefaultConstructor(ClassFile cFile) throws Exception {

        FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
        MethodInfo constructor = addFunction(cFile, Constants.CONSTRUCTOR_NAME);
        /**
         * add ALOAD_0 instruction
         */
        CodeAtt codeAtt = createEmptyCodeAtt(cFile);
        Instruction aload_0 = factoryFacade.createInstruction(JavaInstructionsOpcodes.ALOAD_0, codeAtt);
        addInstruction(cFile, constructor, aload_0);

        /**
         * Adding a methodRef for generating invokespecial
         */
        int indexOfSuperClassInfo = findClassInfoFromCP(cFile, getSuperClassName(cFile));
        int methodRefInfo = addMethodRefInCP(cFile, indexOfSuperClassInfo,
                Constants.CONSTRUCTOR_NAME, Constants.VOID_DESCIPTIOM);
        Un methodRefInfoUn = factoryFacade.createUn(methodRefInfo).trim(2);
        /**
         * Adding invokeSpecial instruction
         */
        Instruction invokeSpecial = factoryFacade.createInstruction(
                JavaInstructionsOpcodes.INVOKESPECIAL,
                methodRefInfoUn, codeAtt);
        addInstruction(cFile, constructor, invokeSpecial);

        /**
         * Adding return instruction
         */
        Instruction retInstruction = factoryFacade.createInstruction(JavaInstructionsOpcodes.RETURN, codeAtt);
        addInstruction(cFile, constructor, retInstruction);
        constructor.getCodeAtt().setMaxStack(1);
        constructor.getCodeAtt().setMaxLocals(1);
        return constructor;

    }

    public int findUTF8FromCP(ClassFile cFile, String utf8) {
        MultiplePoolsFacade cp = cFile.getConstantPool();
        for (int loop = 1; loop < cp.getCurrentSize(); loop++) {
            Object obj = cp.get(loop);
            if (obj instanceof UTF8Info) {
                UTF8Info cInfo = (UTF8Info) obj;
                String checkName = cInfo.convertBytes();
                if (checkName.equals(utf8)) {
                    return loop;
                }
            }
        }
        return -1;
    }

    private int addMethodOrFieldRefInCP(ClassFile cFile, int cInfoCPIndex,
            String methodName, String methodDesc, boolean isMethodInfo) {
        try {
            FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
            int nAtInfoIndex = addNameAndTypeInfo(cFile, methodName, methodDesc);
            Un classInfoIndexUn = factoryFacade.createUn(cInfoCPIndex).trim(2);
            Un nAtInfoIndexUn = factoryFacade.createUn(nAtInfoIndex).trim(2);
            ReferenceInfo refInfo = null;
            if (isMethodInfo) {
                refInfo = factoryFacade.createMethodRefInfo(classInfoIndexUn, nAtInfoIndexUn);
            } else {
                refInfo = factoryFacade.createFieldRefInfo(classInfoIndexUn, nAtInfoIndexUn);
            }
            refInfo.setIndex(classInfoIndexUn);
            refInfo.setNameAndType(nAtInfoIndexUn);
            return addInCP(cFile, refInfo);
        } catch (Exception d) {
            d.printStackTrace();
            System.exit(1);
        }
        return 0; //should never come here.
    }

    public int addMethodRefInCP(ClassFile cFile, int cInfoCPIndex,
            String methodName, String methodDesc) {
        return addMethodOrFieldRefInCP(cFile, cInfoCPIndex, methodName, methodDesc, true);
    }

    public int addFieldRefInCP(ClassFile cFile, int cInfoCPIndex, String methodName, String methodDesc) {
        return addMethodOrFieldRefInCP(cFile, cInfoCPIndex, methodName, methodDesc, false);
    }

    public int addNameAndTypeInfo(ClassFile cFile, String name, String Desc) {
        try {
            FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
            int nameIndex = addUTF8InCP(cFile, name);
            int descIndex = addUTF8InCP(cFile, Desc);

            Un nameIndexUn = factoryFacade.createUn(nameIndex).trim(2);
            Un descIndexUn = factoryFacade.createUn(descIndex).trim(2);
            NameAndTypeInfo nAtInfo = factoryFacade.createNameAndTypeInfo();
            nAtInfo.setIndex(nameIndexUn);
            nAtInfo.setDescriptorIndex(descIndexUn);
            return addInCP(cFile, nAtInfo);
        } catch (Exception d) {
            d.printStackTrace();
            System.exit(1);
        }
        return 0; //should not come here.
    }

    public MethodInfo addMainFunction(ClassFile cFile) {
        /**
         *  first creating and adding the parameters of the function
         */
        FieldType stringArrayType = new FieldType(FieldTypes.ARRAY);
        stringArrayType.addClassName("java/lang/String");
        ArrayList<FieldType> param = new ArrayList();
        param.add(stringArrayType);

        /**
         * Now setting access flags
         */
        ArrayList<Integer> accessFlagValues = new ArrayList();
        accessFlagValues.add(AccessFlags.ACC_PUBLIC);
        accessFlagValues.add(AccessFlags.ACC_STATIC);
        AccessFlags accessFlag = generateAccessFlag(accessFlagValues);

        MethodInfo method = addFunction(accessFlag.intValueSigned(), cFile,
                "main", param, null);
        return method;
    }

    public int addStringInfoInCP(ClassFile cFile, String name) {
        int ret = 0;
        try {
            FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
            int utf8InfoIndex = addUTF8InCP(cFile, name);
            StringInfo strInfo = factoryFacade.createStringInfo();
            strInfo.setIndex(utf8InfoIndex);
            ret = addInCP(cFile, strInfo);
        } catch (Exception d) {
            System.out.println(d);
            System.exit(1);
        }
        return ret;
    }

    public void print(ClassFile cFile, MethodInfo method, String toPrint) {
        try {
            FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
            /**
             * adding information about System.out
             */
            int classInfoIndex = addClassInfoInCP(cFile, addUTF8InCP(cFile, "java/lang/System"));
            int fieldRefIndex = addFieldRefInCP(cFile, classInfoIndex, "out", "Ljava/io/PrintStream;");
            Instruction getStaticInstr = factoryFacade.createInstruction(JavaInstructionsOpcodes.GETSTATIC,
                    factoryFacade.createUn(fieldRefIndex).trim(2), method.getCodeAtt());
            addInstruction(cFile, method, getStaticInstr);

            /**
             * LDC helloWorld
             */
            int strInfoCPIndex = addStringInfoInCP(cFile, toPrint);
            Instruction ldcInstr = factoryFacade.createInstruction(JavaInstructionsOpcodes.LDC,
                    factoryFacade.createUn(strInfoCPIndex).trim(1), method.getCodeAtt());
            addInstruction(cFile, method, ldcInstr);

            /**
             * invokeSpecial println
             */
            classInfoIndex = addClassInfoInCP(cFile, addUTF8InCP(cFile,
                    "java/io/PrintStream"));
            int methodRefInfoCPIndex = addMethodRefInCP(cFile, classInfoIndex, "println", "(Ljava/lang/String;)V");
            Instruction invokeVirtual = factoryFacade.createInstruction(JavaInstructionsOpcodes.INVOKEVIRTUAL,
                    factoryFacade.createUn(methodRefInfoCPIndex).trim(2),
                    method.getCodeAtt());
            addInstruction(cFile, method, invokeVirtual);

        } catch (Exception d) {
            d.printStackTrace();
            System.exit(1);
        }
    }

    public void print(ClassFile cFile, MethodInfo method, int LVIndex, boolean isInt) {
        try {
            FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();

            /**
             * adding information about System.out
             */
            int classInfoIndex = addClassInfoInCP(cFile, addUTF8InCP(cFile, "java/lang/System"));
            int fieldRefIndex = addFieldRefInCP(cFile, classInfoIndex, "out", "Ljava/io/PrintStream;");
            Instruction getStaticInstr = factoryFacade.createInstruction(JavaInstructionsOpcodes.GETSTATIC,
                    factoryFacade.createUn(fieldRefIndex).trim(2), method.getCodeAtt());
            addInstruction(cFile, method, getStaticInstr);

            /**
             * ILOAD Or FLOAD 
             */
            String outFundDes = "(I)V";
            int opcode = JavaInstructionsOpcodes.ILOAD;
            if (!isInt) {
                opcode = JavaInstructionsOpcodes.FLOAD;
                outFundDes = "(F)V";
            }

            Instruction ldcInstr = factoryFacade.createInstruction(opcode,
                    factoryFacade.createUn(LVIndex).trim(1),
                    method.getCodeAtt());
            addInstruction(cFile, method, ldcInstr);

            /**
             * invokeSpecial println
             */
            classInfoIndex = addClassInfoInCP(cFile, addUTF8InCP(cFile,
                    "java/io/PrintStream"));
            int methodRefInfoCPIndex = addMethodRefInCP(cFile, classInfoIndex, "println", outFundDes);
            Instruction invokeVirtual = factoryFacade.createInstruction(JavaInstructionsOpcodes.INVOKEVIRTUAL,
                    factoryFacade.createUn(methodRefInfoCPIndex).trim(2),
                    method.getCodeAtt());
            addInstruction(cFile, method, invokeVirtual);

        } catch (Exception d) {
            d.printStackTrace();
            System.exit(1);
        }

    }

    public void createObj(ClassFile cFile, MethodInfo method,
            int cpClassInfoIndex, int defaultConstrCPIndex) {
        FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
        try {
            Instruction newInstr = factoryFacade.createInstruction(JavaInstructionsOpcodes.NEW,
                    factoryFacade.createUn(cpClassInfoIndex).trim(2), method.getCodeAtt());
            addInstruction(cFile, method, newInstr);
            addInstruction(cFile, method, factoryFacade.createInstruction(JavaInstructionsOpcodes.DUP,
                    method.getCodeAtt()));
            addInstruction(cFile, method, factoryFacade.createInstruction(JavaInstructionsOpcodes.INVOKESPECIAL,
                    factoryFacade.createUn(defaultConstrCPIndex).trim(2),
                    method.getCodeAtt()));


        } catch (Exception d) {
            d.printStackTrace();
            System.exit(1);
        }
    }

    public String getUTF8FromCP(ClassFile cFile, int cpIndex) {
        MultiplePoolsFacade cp = cFile.getConstantPool();
        UTF8Info utf8 = (UTF8Info) cp.get(cpIndex);
        return utf8.convertBytes();
    }
}
